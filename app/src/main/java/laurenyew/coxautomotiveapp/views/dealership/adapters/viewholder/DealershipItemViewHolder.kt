package laurenyew.coxautomotiveapp.views.dealership.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import laurenyew.coxautomotiveapp.R

class DealershipItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val dealershipItemIdTextView = view.findViewById<TextView>(R.id.dealershipItemIdTextView)
    val dealershipItemNameTextView = view.findViewById<TextView>(R.id.dealershipItemNameTextView)
}