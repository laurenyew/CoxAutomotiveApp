package laurenyew.coxautomotiveapp.models

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laurenyew.coxautomotiveapp.data.*
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
    var status: MutableLiveData<Status> = MutableLiveData()

    val allDealerships: LiveData<List<Dealership>> = dealershipDao.getAllDealershps()

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
        status.value = Status(true, null)
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    resetDatabase()
                    val dataSetId = getDataSetId()

                    //Get all the vehicles
                    val vehicleIds = getVehicleIds(dataSetId)
                    var dealershipIds = hashSetOf<Int>()
                    vehicleIds.forEach { vehicleId ->
                        val vehicleDetail = getVehicleDetail(dataSetId, vehicleId)
                        //Insert the vehicle detail into the database
                        insert(vehicleDetail)
                        //Add to our dealership id list
                        dealershipIds.add(vehicleDetail.dealerId)
                    }

                    //Get all the dealerships
                    dealershipIds.forEach { dealershipId ->
                        val dealershipDetail = getDealershipDetail(dataSetId, dealershipId)
                        //Insert Dealership Detail into the database
                        insert(dealershipDetail)
                    }

                    status.value = Status(false, null)
                }
            } catch (ex: Exception) {
                Log.d(TAG, "Error: Unable to Load Dealership Vehicle Data: ${ex.message}")
                withContext(Dispatchers.Main) {
                    status.value = Status(false, ex)
                }
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
        dealershipDao.deleteAll()
        vehicleDao.deleteAll()
    }

    //region Networking calls
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
        dealershipDao.insert(dealership)
    }

    @WorkerThread
    suspend fun insert(vehicle: Vehicle) {
        vehicleDao.insert(vehicle)
    }
    //endregion

    companion object {
        val TAG = DealershipVehicleRepository::class.java.toString()
    }
}