package laurenyew.coxautomotiveapp.views.dealership

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dealerships_list.*
import laurenyew.coxautomotiveapp.R
import laurenyew.coxautomotiveapp.data.Dealership
import laurenyew.coxautomotiveapp.viewmodels.DealershipVehicleViewModel
import laurenyew.coxautomotiveapp.views.dealership.adapters.DealershipItemsRecyclerViewAdapter
import laurenyew.coxautomotiveapp.views.vehicles.VehicleListFragment.Companion.ARG_DEALERSHIP_ID_PARAM
import laurenyew.coxautomotiveapp.views.vehicles.VehicleListFragment.Companion.ARG_DEALERSHIP_NAME_PARAM

/**
 * Shows the dealership list
 */
class DealershipsListFragment : Fragment() {

    private lateinit var dealershipViewModel: DealershipVehicleViewModel
    private var dealershipAdapter: DealershipItemsRecyclerViewAdapter? = null

    private val dealershipsObserver = Observer<List<Dealership>> {
        dealershipAdapter?.updateData(it)
        val isEmpty = it.isEmpty()
        loadingProgressBar.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
        emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dealershipViewModel =
            ViewModelProviders.of(this).get(DealershipVehicleViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dealerships_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar.visibility = View.VISIBLE

        dealershipAdapter =
            DealershipItemsRecyclerViewAdapter(dealerItemClicked = { dealerId, dealerName ->
                val bundle = bundleOf(
                    ARG_DEALERSHIP_ID_PARAM to dealerId,
                    ARG_DEALERSHIP_NAME_PARAM to dealerName
                )
                findNavController().navigate(
                    R.id.action_dealershipsListFragment_to_vehicleListFragment,
                    bundle
                )
            })

        dealershipsListRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            //Add separator lines between rows
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(dividerItemDecoration)
            adapter = dealershipAdapter
        }

        //Setup state
        dealershipViewModel.dealerships.observe(viewLifecycleOwner, dealershipsObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dealershipAdapter?.onDestroy()
        dealershipViewModel.dealerships.removeObserver(dealershipsObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        dealershipAdapter = null
    }
}
