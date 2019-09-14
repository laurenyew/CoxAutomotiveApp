package laurenyew.coxautomotiveapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import laurenyew.coxautomotiveapp.data.*
import laurenyew.coxautomotiveapp.models.DealershipVehicleRepository

/**
 * View Model for Dealership + Vehicle Data
 * (Lifetime is across the application time)
 */
class DealershipVehicleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DealershipVehicleRepository
    val dealerships: LiveData<List<Dealership>>

    val status: LiveData<Status>

    init {
        val dealershipDao =
            DealershipRoomDatabase.getDatabase(application, viewModelScope).dealershipDao()
        val vehicleDao = VehicleRoomDatabase.getDatabase(application, viewModelScope).vehicleDao()
        repository = DealershipVehicleRepository(dealershipDao, vehicleDao, viewModelScope)
        dealerships = repository.allDealerships
        status = repository.status
    }

    fun vehiclesForDealership(dealerId: Int): LiveData<List<Vehicle>> =
        repository.vehiclesForDealership(dealerId)

    fun loadDealershipVehicleData() {
        repository.loadDealershipVehicleData()
    }
}