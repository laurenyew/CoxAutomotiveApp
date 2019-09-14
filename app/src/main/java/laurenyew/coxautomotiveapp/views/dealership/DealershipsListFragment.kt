package laurenyew.coxautomotiveapp.views.dealership

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
import laurenyew.coxautomotiveapp.R
import laurenyew.coxautomotiveapp.viewmodels.DealershipVehicleViewModel
import laurenyew.coxautomotiveapp.views.dealership.adapters.DealershipItemsRecyclerViewAdapter

/**
 * Shows the dealership list
 */
class DealershipsListFragment : Fragment() {

    private lateinit var dealershipViewModel: DealershipVehicleViewModel
    private var adapter: DealershipItemsRecyclerViewAdapter? = null

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

        dealershipsListRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            //Add separator lines between rows
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(dividerItemDecoration)


        }

        //Setup state
        dealershipViewModel.dealerships.observe(this, Observer {
            if (adapter == null) {
                adapter = DealershipItemsRecyclerViewAdapter()
                dealershipsListRecyclerView.adapter = adapter
            }
            adapter?.updateData(it)
            emptyTextView.visibility =
                if (it.isEmpty()
                    && dealershipViewModel.status.value?.loading == false
                ) View.VISIBLE else View.GONE
        })

        dealershipViewModel.status.observe(this, Observer {
            loadingProgressBar.visibility = if (it.loading) View.VISIBLE else View.GONE
            it.error?.let {
                Toast.makeText(context, R.string.error_loading_dealerships, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}
