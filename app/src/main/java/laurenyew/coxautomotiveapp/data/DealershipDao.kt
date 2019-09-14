package laurenyew.coxautomotiveapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Interface w/ Room to handle SQL Queries
 */
@Dao
interface DealershipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dealership: Dealership)

    @Query("DELETE FROM dealership_table")
    fun deleteAll()

    @Query("SELECT * FROM dealership_table ORDER BY name ASC")
    fun getAllDealerships(): LiveData<List<Dealership>>
}