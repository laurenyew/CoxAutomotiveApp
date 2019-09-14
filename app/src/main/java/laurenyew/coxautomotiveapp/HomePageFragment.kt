package laurenyew.coxautomotiveapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home_page.*
import laurenyew.coxautomotiveapp.viewmodels.DealershipVehicleViewModel

/**
 * Home Page
 */
class HomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home_page, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDealershipsButton.setOnClickListener {
            openDealershipsView()
        }
    }

    private fun openDealershipsView() {
        //Start loading up the new vehicle data
        val viewModel = ViewModelProviders.of(this).get(DealershipVehicleViewModel::class.java)
        viewModel.loadDealershipVehicleData()
        findNavController().navigate(R.id.action_homePageFragment_to_dealershipsListFragment)
    }
}
