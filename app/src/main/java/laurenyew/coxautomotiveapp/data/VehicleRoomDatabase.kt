package laurenyew.coxautomotiveapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Dealership::class], version = 1)
public abstract class VehicleRoomDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao

    /**
     * Handle clearing out the database when launching this scope
     */
    private class VehicleDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    clearDatabase(database.vehicleDao())
                }
            }
        }

        suspend fun clearDatabase(dao: VehicleDao) {
            dao.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: VehicleRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): VehicleRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                //Create database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VehicleRoomDatabase::class.java,
                    "Vehicle_database"
                ).addCallback(VehicleDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}