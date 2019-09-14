package laurenyew.coxautomotiveapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import laurenyew.coxautomotiveapp.R

private const val ARG_DEALERSHIP_ID_PARAM = "arg_dealership_id"

class VehicleListFragment : Fragment() {
    private var dealershipId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealershipId = it.getInt(ARG_DEALERSHIP_ID_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_vehicle_list, container, false)

    companion object {
        @JvmStatic
        fun newInstance(dealershipId: Int?) =
            VehicleListFragment().apply {
                arguments = Bundle().apply {
                    dealershipId?.let {
                        putInt(ARG_DEALERSHIP_ID_PARAM, dealershipId)
                    }
                }
            }
    }
}
