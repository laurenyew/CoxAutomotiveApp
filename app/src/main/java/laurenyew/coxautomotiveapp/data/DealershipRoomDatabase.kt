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
public abstract class DealershipRoomDatabase : RoomDatabase() {
    abstract fun dealershipDao(): DealershipDao

    /**
     * Handle clearing out the database when launching this scope
     */
    private class DealershipDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    clearDatabase(database.dealershipDao())
                }
            }
        }

        suspend fun clearDatabase(dao: DealershipDao) {
            dao.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DealershipRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): DealershipRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                //Create database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DealershipRoomDatabase::class.java,
                    "Dealership_database"
                ).addCallback(DealershipDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}