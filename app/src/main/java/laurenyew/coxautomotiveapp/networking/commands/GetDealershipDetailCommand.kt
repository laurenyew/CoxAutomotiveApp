package laurenyew.coxautomotiveapp.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.coxautomotiveapp.models.DealershipDetailModel
import laurenyew.coxautomotiveapp.networking.api.response.DealershipItemResponse
import retrofit2.Response

/**
 * This command makes the network call to get the dealership detail
 * Throws an exception if we fail to make the call
 */
class GetDealershipDetailCommand(private val dataSetId: String, private val dealerId: Int) :
    BaseNetworkCommand() {

    @Throws(RuntimeException::class)
    suspend fun execute(): DealershipDetailModel {
        val deferred = async {
            Log.d(TAG, "Executing $TAG")
            val call = api?.getDealershipDetail(dataSetId, dealerId)
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
    private fun parseResponse(response: Response<DealershipItemResponse>?): DealershipDetailModel {
        val data = response?.body()
        if (response?.code() != 200 || data == null) {
            throw RuntimeException("API call failed. Unable to find dealership for id $dealerId")
        } else {
            return DealershipDetailModel(data.id, data.name)
        }
    }

    companion object {
        val TAG: String = GetDealershipDetailCommand::class.java.simpleName
    }
}