package laurenyew.coxautomotiveapp.views.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import laurenyew.coxautomotiveapp.R
import laurenyew.coxautomotiveapp.data.Vehicle
import laurenyew.coxautomotiveapp.views.vehicles.adapters.data.VehicleItemDataDiffCallback
import laurenyew.coxautomotiveapp.views.vehicles.adapters.viewholder.VehicleItemViewHolder
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Lauren Yew
 *
 * RecyclerViewAdapter for showing the vehicle items
 * With performance updates (update only parts of list that have changed)
 */
class VehicleItemsRecyclerViewAdapter() :
    RecyclerView.Adapter<VehicleItemViewHolder>(), CoroutineScope {

    private val job = Job()
    private var data: MutableList<Vehicle> = ArrayList()
    private var pendingDataUpdates = ArrayDeque<List<Vehicle>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    //RecyclerView Diff.Util (List Updates)
    fun updateData(newData: List<Vehicle>?) {
        if (isActive) {
            val data = newData ?: ArrayList()
            pendingDataUpdates.add(data)
            if (pendingDataUpdates.size <= 1) {
                updateDataInternal(data)
            }
        }
    }

    //If the adapter is destroyed, cancel any running jobs
    fun onDestroy() {
        job.cancel()
        pendingDataUpdates.clear()
    }

    /**
     * Handle the diff util update on a background thread
     * (this can take O(n) time so we don't want it on the main thread)
     */
    private fun updateDataInternal(newData: List<Vehicle>?) {
        val oldData = ArrayList(data)

        launch {
            val diffCallback = createDataDiffCallback(oldData, newData)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            if (isActive) {
                withContext(Dispatchers.Main) {
                    applyDataDiffResult(newData, diffResult)
                }
            }
        }
    }

    /**
     * UI thread callback to apply the diff result to the adapter
     * and take in the latest update
     */
    private fun applyDataDiffResult(
        newData: List<Vehicle>?,
        diffResult: DiffUtil.DiffResult
    ) {
        if (pendingDataUpdates.isNotEmpty()) {
            pendingDataUpdates.remove()
        }

        //Apply the data to the view
        data.clear()
        if (newData != null) {
            data.addAll(newData)
        }
        diffResult.dispatchUpdatesTo(this)

        //Take in the next latest update
        if (pendingDataUpdates.isNotEmpty()) {
            val latestDataUpdate = pendingDataUpdates.pop()
            pendingDataUpdates.clear()
            updateDataInternal(latestDataUpdate)
        }
    }

    private fun createDataDiffCallback(
        oldData: List<Vehicle>?,
        newData: List<Vehicle>?
    ): DiffUtil.Callback =
        VehicleItemDataDiffCallback(oldData, newData)
    //endregion

    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_item_preview_view, parent, false)
        return VehicleItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleItemViewHolder, position: Int) {
        val item = data[position]
        holder.yearTextView.text = item.year.toString()
        holder.makeTextView.text = item.make
        holder.modelTextView.text = item.model
        holder.vehicleIdTextView.text = item.id.toString()
        holder.dealershipIdTextView.text = item.dealerId.toString()
    }

    override fun getItemCount(): Int = data.size
//endregion
}