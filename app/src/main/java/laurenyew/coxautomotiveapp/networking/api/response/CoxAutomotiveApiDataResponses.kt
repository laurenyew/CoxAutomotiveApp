package laurenyew.coxautomotiveapp.networking.api.response

import com.squareup.moshi.Json

/**
 * JSON Responses expected from the cox automotive api
 *
 * Using Moshi to parse Json
 */
data class DataSetIdResponse(
    @Json(name = "datasetId") val id: String
)

data class VehicleIdsResponse(
    @Json(name = "vehicleIds") val vehicles: List<Int>
)

data class VehicleItemResponse(
    @Json(name = "vehicleId") val id: Int,
    @Json(name = "year") val year: Int,
    @Json(name = "make") val make: String,
    @Json(name = "model") val model: String,
    @Json(name = "dealerId") val dealerId: Int
)

data class DealershipItemResponse(
    @Json(name = "dealerId") val id: Int,
    @Json(name = "name") val name: String
)