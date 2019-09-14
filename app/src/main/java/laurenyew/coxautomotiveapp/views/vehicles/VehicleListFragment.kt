package laurenyew.coxautomotiveapp.views.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dealerships_list.emptyTextView
import kotlinx.android.synthetic.main.fragment_dealerships_list.loadingProgressBar
import kotlinx.android.synthetic.main.fragment_vehicle_list.*
import laurenyew.coxautomotiveapp.R
import laurenyew.coxautomotiveapp.data.Vehicle
import laurenyew.coxautomotiveapp.viewmodels.DealershipVehicleViewModel
import laurenyew.coxautomotiveapp.views.vehicles.adapters.VehicleItemsRecyclerViewAdapter


class VehicleListFragment : Fragment() {
    private var dealershipId: Int? = null
    private var dealershipName: String = ""
    private lateinit var vehicleViewModel: DealershipVehicleViewModel
    private var vehicleLiveData: LiveData<List<Vehicle>>? = null
    private var vehicleAdapter: VehicleItemsRecyclerViewAdapter? = null

    private val vehiclesObserver = Observer<List<Vehicle>> {
        vehicleAdapter?.updateData(it)
        val isEmpty = it.isEmpty()
        loadingProgressBar.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
        emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealershipId = it.getInt(ARG_DEALERSHIP_ID_PARAM)
            dealershipName = it.getString(ARG_DEALERSHIP_NAME_PARAM) ?: ""
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

        vehicleAdapter = VehicleItemsRecyclerViewAdapter()

        vehicleListTitleTextView.text =
            String.format(resources.getString(R.string.vehicle_list_text), dealershipName)

        vehiclesListRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            //Add separator lines between rows
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(dividerItemDecoration)
            adapter = vehicleAdapter
        }

        //Setup state
        dealershipId?.let { dealerId ->
            vehicleLiveData = vehicleViewModel.vehiclesForDealership(dealerId)
            vehicleLiveData?.observe(viewLifecycleOwner, vehiclesObserver)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vehicleAdapter?.onDestroy()
        vehicleLiveData?.removeObserver(vehiclesObserver)
        vehicleLiveData = null
    }

    override fun onDestroy() {
        super.onDestroy()
        vehicleAdapter = null
    }

    companion object {
        const val ARG_DEALERSHIP_ID_PARAM = "arg_dealership_id"
        const val ARG_DEALERSHIP_NAME_PARAM = "arg_dealership_name"

        @JvmStatic
        fun newInstance(dealershipId: Int?, dealershipName: String?) =
            VehicleListFragment().apply {
                arguments = Bundle().apply {
                    dealershipId?.let {
                        putInt(ARG_DEALERSHIP_ID_PARAM, dealershipId)
                    }
                    dealershipName?.let {
                        putString(ARG_DEALERSHIP_NAME_PARAM, dealershipName)
                    }
                }
            }
    }
}
