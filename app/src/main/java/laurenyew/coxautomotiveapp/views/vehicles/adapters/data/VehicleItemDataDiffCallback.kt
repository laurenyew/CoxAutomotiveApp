package laurenyew.coxautomotiveapp.views.vehicles.adapters.data

import androidx.recyclerview.widget.DiffUtil
import laurenyew.coxautomotiveapp.data.Vehicle

/**
 * @author Lauren Yew
 *
 * DiffUtil.Callback that compares data wrappers
 */
open class VehicleItemDataDiffCallback(
    private val oldData: List<Vehicle>?,
    private val newData: List<Vehicle>?
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData?.size ?: 0

    override fun getNewListSize(): Int = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData?.get(oldItemPosition)
        val newItem = newData?.get(newItemPosition)

        return oldItem?.dealerId == newItem?.dealerId
                && oldItem?.make == newItem?.make
                && oldItem?.model == newItem?.model
                && oldItem?.year == newItem?.year
    }
}