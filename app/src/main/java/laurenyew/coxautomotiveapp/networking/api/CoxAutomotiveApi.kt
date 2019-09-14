package laurenyew.coxautomotiveapp.networking.api

import laurenyew.coxautomotiveapp.networking.api.response.DataSetIdResponse
import laurenyew.coxautomotiveapp.networking.api.response.DealershipItemResponse
import laurenyew.coxautomotiveapp.networking.api.response.VehicleIdsResponse
import laurenyew.coxautomotiveapp.networking.api.response.VehicleItemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Lauren Yew
 * Retrofit api
 */
interface CoxAutomotiveApi {
    /**
     * Get the Dataset id (used to query the server for all info)
     */
    @Throws(RuntimeException::class)
    @GET("datasetid")
    fun getDataSetId(): Call<DataSetIdResponse?>?

    /**
     * Get a list of vehicle ids
     */
    @Throws(RuntimeException::class)
    @GET("{datasetId}/vehicles")
    fun getVehicleIds(@Path("datasetId") dataSetId: String)
            : Call<VehicleIdsResponse>?

    /**
     * Get Vehicle Details
     */
    @Throws(RuntimeException::class)
    @GET("{datasetId}/vehicles/{vehicleId}")
    fun getVehicleDetail(@Path("datasetId") dataSetId: String, @Path("vehicleId") vehicleId: Int)
            : Call<VehicleItemResponse>?

    /**
     * Get the vehicle list for a given data set id
     */
    @Throws(RuntimeException::class)
    @GET("{datasetId}/dealers/{dealerId}")
    fun getDealershipDetail(@Path("datasetId") dataSetId: String, @Path("dealerId") dealerId: Int)
            : Call<DealershipItemResponse>?
}