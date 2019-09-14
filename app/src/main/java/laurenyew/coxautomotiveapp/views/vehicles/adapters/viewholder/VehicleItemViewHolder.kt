package laurenyew.coxautomotiveapp.views.vehicles.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import laurenyew.coxautomotiveapp.R

class VehicleItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val yearTextView = view.findViewById<TextView>(R.id.vehicleYearTextView)
    val makeTextView = view.findViewById<TextView>(R.id.vehicleMakeTextView)
    val modelTextView = view.findViewById<TextView>(R.id.vehicleModelTextView)
    val vehicleIdTextView = view.findViewById<TextView>(R.id.vehicleIdTextView)
    val dealershipIdTextView = view.findViewById<TextView>(R.id.dealershipIdTextView)
}