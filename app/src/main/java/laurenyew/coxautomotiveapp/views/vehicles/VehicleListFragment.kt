package laurenyew.coxautomotiveapp.views.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dealerships_list.*
import kotlinx.android.synthetic.main.fragment_dealerships_list.emptyTextView
import kotlinx.android.synthetic.main.fragment_dealerships_list.loadingProgressBar
import kotlinx.android.synthetic.main.fragment_vehicle_list.*
import laurenyew.coxautomotiveapp.R
import laurenyew.coxautomotiveapp.viewmodels.DealershipVehicleViewModel
import laurenyew.coxautomotiveapp.views.vehicles.adapters.VehicleItemsRecyclerViewAdapter


class VehicleListFragment : Fragment() {
    private var dealershipId: Int? = null
    private lateinit var vehicleViewModel: DealershipVehicleViewModel
    private var adapter: VehicleItemsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealershipId = it.getInt(ARG_DEALERSHIP_ID_PARAM)
        }
        vehicleViewModel =
            ViewModelProviders.of(this).get(DealershipVehicleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_vehicle_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar.visibility = View.VISIBLE

        vehiclesListRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            //Add separator lines between rows
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(dividerItemDecoration)
        }

        //Setup state
        dealershipId?.let { dealerId ->
            vehicleViewModel.vehiclesForDealership(dealerId).observe(this, Observer {
                if (adapter == null) {
                    adapter = VehicleItemsRecyclerViewAdapter()
                    dealershipsListRecyclerView.adapter = adapter
                }
                adapter?.updateData(it)
                emptyTextView.visibility =
                    if (it.isEmpty()
                        && vehicleViewModel.status.value?.loading == false
                    ) View.VISIBLE else View.GONE
            })
        }

        vehicleViewModel.status.observe(this, Observer {
            loadingProgressBar.visibility = if (it.loading) View.VISIBLE else View.GONE
            it.error?.let {
                Toast.makeText(context, R.string.error_loading_dealerships, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    companion object {
        const val ARG_DEALERSHIP_ID_PARAM = "arg_dealership_id"

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
