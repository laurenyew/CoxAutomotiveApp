package laurenyew.coxautomotiveapp.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.coxautomotiveapp.data.Vehicle
import laurenyew.coxautomotiveapp.networking.api.response.VehicleItemResponse
import retrofit2.Response

/**
 * This command makes the network call to get the vehicle detail
 * Throws an exception if we fail to make the call
 */
class GetVehicleDetailCommand(private val dataSetId: String, private val vehicleId: Int) :
    BaseNetworkCommand() {

    @Throws(RuntimeException::class)
    suspend fun execute(): Vehicle {
        val deferred = async {
            Log.d(TAG, "Executing $TAG")
            val call = api?.getVehicleDetail(dataSetId, vehicleId)
            try {
                val response = call?.execute()
                parseResponse(response)
            } finally {
                //Clean up network call and cancel
                call?.cancel()
            }
        }
        return deferred.await()
    }

    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(response: Response<VehicleItemResponse>?): Vehicle {
        val data = response?.body()
        if (response?.code() != 200 || data == null) {
            throw RuntimeException("API call failed. Unable to find vehicle detail for datasetid: $dataSetId, vehicleid: $vehicleId")
        } else {
            return Vehicle(
                data.id,
                data.year,
                data.make,
                data.model,
                data.dealerId
            )
        }
    }

    companion object {
        val TAG: String = GetVehicleDetailCommand::class.java.simpleName
    }
}