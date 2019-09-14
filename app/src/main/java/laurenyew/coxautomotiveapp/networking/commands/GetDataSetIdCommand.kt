package laurenyew.coxautomotiveapp.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.coxautomotiveapp.networking.api.response.DataSetIdResponse
import retrofit2.Response

/**
 * This command makes the server call to get the data set id
 * It throws a runtime exception if it was unable to
 */
class GetDataSetIdCommand : BaseNetworkCommand() {

    @Throws(RuntimeException::class)
    suspend fun execute(): String {
        val deferred = async {
            Log.d(TAG, "Executing $TAG")
            val call = api?.getDataSetId()
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
    private fun parseResponse(response: Response<DataSetIdResponse?>?): String {
        val data = response?.body()
        if (response?.code() != 200 || data == null) {
            throw RuntimeException("API call failed. Unable to find data set id")
        } else {
            return data.id
        }
    }

    companion object {
        val TAG: String = GetDataSetIdCommand::class.java.simpleName
    }
}