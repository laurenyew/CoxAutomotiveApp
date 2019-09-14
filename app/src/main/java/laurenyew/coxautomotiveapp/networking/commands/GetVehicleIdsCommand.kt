package laurenyew.coxautomotiveapp.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.coxautomotiveapp.networking.api.response.VehicleIdsResponse
import retrofit2.Response

/**
 * This command makes the network call to get the vehicle ids
 * Throws an exception if we fail to make the call
 */
class GetVehicleIdsCommand(private val dataSetId: String) : BaseNetworkCommand() {

    @Throws(RuntimeException::class)
    suspend fun execute(): List<Int> {
        val deferred = async {
            Log.d(TAG, "Executing $TAG")
            val call = api?.getVehicleIds(dataSetId)
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
    private fun parseResponse(response: Response<VehicleIdsResponse?>?): List<Int> {
        val data = response?.body()
        if (response?.code() != 200 || data == null) {
            throw RuntimeException("API call failed. Unable to find vehicle ids")
        } else {
            return data.vehicles
        }
    }

    companion object {
        val TAG: String = GetVehicleIdsCommand::class.java.simpleName
    }
}