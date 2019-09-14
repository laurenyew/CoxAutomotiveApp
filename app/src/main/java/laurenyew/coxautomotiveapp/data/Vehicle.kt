package laurenyew.coxautomotiveapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model for Vehicle Detail Info
 */
@Entity(tableName = "vehicle_table")
data class Vehicle(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "make") val make: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "dealerId") val dealerId: Int
)