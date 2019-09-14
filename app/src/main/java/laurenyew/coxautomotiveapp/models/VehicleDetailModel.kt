package laurenyew.coxautomotiveapp.models

/**
 * Model for Vehicle Detail Info
 */
data class VehicleDetailModel(
    val id: Int,
    val year: Int,
    val make: String,
    val model: String,
    val dealerId: Int
)