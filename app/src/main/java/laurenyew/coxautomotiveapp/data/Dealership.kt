package laurenyew.coxautomotiveapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model for Dealership Detail Info
 */
@Entity(tableName = "dealership_table")
data class Dealership(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String
)