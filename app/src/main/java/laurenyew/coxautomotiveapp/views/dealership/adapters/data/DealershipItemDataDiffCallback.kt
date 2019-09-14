package laurenyew.coxautomotiveapp.views.dealership.adapters.data

import androidx.recyclerview.widget.DiffUtil

/**
 * @author Lauren Yew
 *
 * DiffUtil.Callback that compares data wrappers
 */
open class DealershipItemDataDiffCallback(
    private val oldData: List<DealershipItemDataWrapper>?,
    private val newData: List<DealershipItemDataWrapper>?
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

        return oldItem?.name == newItem?.name
    }
}