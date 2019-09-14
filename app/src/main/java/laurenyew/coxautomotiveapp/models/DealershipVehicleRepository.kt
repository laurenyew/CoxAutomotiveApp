package laurenyew.coxautomotiveapp.models

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laurenyew.coxautomotiveapp.data.Dealership
import laurenyew.coxautomotiveapp.data.DealershipDao
import laurenyew.coxautomotiveapp.data.Vehicle
import laurenyew.coxautomotiveapp.data.VehicleDao
import laurenyew.coxautomotiveapp.networking.commands.GetDataSetIdCommand
import laurenyew.coxautomotiveapp.networking.commands.GetDealershipDetailCommand
import laurenyew.coxautomotiveapp.networking.commands.GetVehicleDetailCommand
import laurenyew.coxautomotiveapp.networking.commands.GetVehicleIdsCommand

/**
 * Repository that deals with
 * Making API calls, updating Room Database, & updating Live Data
 */
class DealershipVehicleRepository(
    private val dealershipDao: DealershipDao,
    private val vehicleDao: VehicleDao,
    private val scope: CoroutineScope
) {
    private var dealershipIds = HashSet<Int>()
    val allDealerships: LiveData<List<Dealership>> = dealershipDao.getAllDealerships()

    fun vehiclesForDealership(dealershipId: Int): LiveData<List<Vehicle>> =
        vehicleDao.getAllVehiclesForDealership(dealershipId)

    /**
     * Make the server call to load dealership / vehicle data
     *
     * 1) Get Dataset ID
     * 2) Load Vehicle IDs
     * 3) For Each Vehicle ID, Load Vehicle Details
     * 4) For Each Vehicle Detail DealerID, Load Dealership Details
     *
     * If any of these fail, update the status for the error
     */
    fun loadDealershipVehicleData() {
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    resetDatabase()
                    val dataSetId = getDataSetId()

                    //Get all the vehicles
                    val vehicleIds = getVehicleIds(dataSetId)
                    vehicleIds.forEach { vehicleId ->
                        loadVehicleDetails(dataSetId, vehicleId)
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error: Unable to Load Dealership Vehicle Data: ${ex.message}")
            }
        }
    }

    /**
     * Make sure to reset database (clearing all for this POC)
     * when we load so we can be sure to get clean data sets
     * NOTE: For a real app, we'd want to keep the database up to date in a smarter way
     * (remove / update items)
     */
    private fun resetDatabase() {
        dealershipIds.clear()
        dealershipDao.deleteAll()
        vehicleDao.deleteAll()
    }

    //region Networking calls
    /**
     * Load the vehicle details and load the dealership's details
     */
    private suspend fun loadVehicleDetails(dataSetId: String, vehicleId: Int) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val vehicleDetail = getVehicleDetail(dataSetId, vehicleId)
                val dealerId = vehicleDetail.dealerId

                if (updateDealerIdsWithUniqueId(dealerId)) {
                    loadDealershipDetails(dataSetId, dealerId)
                }

                //Insert the vehicle detail into the database
                insert(vehicleDetail)
            }
        }
    }

    private suspend fun loadDealershipDetails(dataSetId: String, dealerId: Int) {
        //Get all the dealerships (this can be launched in parallel)
        scope.launch {
            withContext(Dispatchers.IO) {
                val dealershipDetail =
                    getDealershipDetail(dataSetId, dealerId)
                //Insert Dealership Detail into the database
                insert(dealershipDetail)
            }
        }
    }

    /**
     * Make a network call to get the dataSetId
     */
    @Throws(RuntimeException::class)
    private suspend fun getDataSetId(): String {
        val command = GetDataSetIdCommand()
        try {
            return command.execute()
        } finally {
            command.finish()
        }
    }

    /**
     * Make a network call to get the vehicleIds
     */
    @Throws(RuntimeException::class)
    private suspend fun getVehicleIds(dataSetId: String): List<Int> {
        val command = GetVehicleIdsCommand(dataSetId)
        try {
            return command.execute()
        } finally {
            command.finish()
        }
    }

    /**
     * Make a network call to get the Vehicle Detail
     */
    @Throws(RuntimeException::class)
    private suspend fun getVehicleDetail(dataSetId: String, vehicleId: Int): Vehicle {
        val command = GetVehicleDetailCommand(dataSetId, vehicleId)
        try {
            return command.execute()
        } finally {
            command.finish()
        }
    }

    /**
     * Make a network call to get the dealership detail
     */
    @Throws(RuntimeException::class)
    private suspend fun getDealershipDetail(dataSetId: String, dealerId: Int): Dealership {
        val command = GetDealershipDetailCommand(dataSetId, dealerId)
        try {
            return command.execute()
        } finally {
            command.finish()
        }
    }
    //endregion

    //region Room Database calls
    @WorkerThread
    suspend fun insert(dealership: Dealership) {
        scope.launch {
            withContext(Dispatchers.IO) {
                dealershipDao.insert(dealership)
            }
        }
    }

    @WorkerThread
    suspend fun insert(vehicle: Vehicle) {
        scope.launch {
            withContext(Dispatchers.IO) {
                vehicleDao.insert(vehicle)
            }
        }
    }
    //endregion

    //region Concurrency State
    /**
     * Update the dealer ids set w/ dealer id
     * @return true if this was a unique dealerId, false otherwise
     */
    @Synchronized
    private fun updateDealerIdsWithUniqueId(dealerId: Int): Boolean {
        val hasUniqueDealerId = !dealershipIds.contains(dealerId)
        if (hasUniqueDealerId) {
            dealershipIds.add(dealerId)
        }
        return hasUniqueDealerId
    }
    //endregion

    companion object {
        val TAG = DealershipVehicleRepository::class.java.toString()
    }
}