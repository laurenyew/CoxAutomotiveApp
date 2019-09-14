package laurenyew.coxautomotiveapp.views.dealership.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import laurenyew.coxautomotiveapp.R
import laurenyew.coxautomotiveapp.views.dealership.adapters.data.DealershipItemDataDiffCallback
import laurenyew.coxautomotiveapp.views.dealership.adapters.data.DealershipItemDataWrapper
import laurenyew.coxautomotiveapp.views.dealership.adapters.viewholder.DealershipItemViewHolder
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Lauren Yew
 *
 * RecyclerViewAdapter for showing the dealership items
 * With performance updates (update only parts of list that have changed)
 */
class DealershipItemsRecyclerViewAdapter :
    RecyclerView.Adapter<DealershipItemViewHolder>(), CoroutineScope {

    private val job = Job()
    private var data: MutableList<DealershipItemDataWrapper> = ArrayList()
    private var pendingDataUpdates = ArrayDeque<List<DealershipItemDataWrapper>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    //RecyclerView Diff.Util (List Updates)
    fun updateData(newData: List<DealershipItemDataWrapper>?) {
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
    private fun updateDataInternal(newData: List<DealershipItemDataWrapper>?) {
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
        newData: List<DealershipItemDataWrapper>?,
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
        oldData: List<DealershipItemDataWrapper>?,
        newData: List<DealershipItemDataWrapper>?
    ): DiffUtil.Callback =
        DealershipItemDataDiffCallback(oldData, newData)
    //endregion

    //region RecyclerView.Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealershipItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dealership_item_preview_view, parent, false)
        return DealershipItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DealershipItemViewHolder, position: Int) {
        val item = data[position]
        holder.dealershipItemIdTextView.text = item.id
        holder.dealershipItemNameTextView.text = item.name
    }

    override fun getItemCount(): Int = data.size
//endregion
}