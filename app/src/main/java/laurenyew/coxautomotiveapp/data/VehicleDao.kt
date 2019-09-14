package laurenyew.coxautomotiveapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Interface w/ Room to handle SQL Queries
 */
@Dao
interface VehicleDao {
    @Insert
    suspend fun insert(vehicle: Vehicle)

    @Query("DELETE FROM vehicle_table")
    fun deleteAll()

    @Query("SELECT * FROM vehicle_table WHERE dealerId == :dealerId ORDER BY id ASC")
    fun getAllVehiclesForDealership(dealerId: Int): LiveData<List<Vehicle>>
}