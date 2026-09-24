package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CachedUserLocationEntity
import com.example.data.model.OfflineCacheMetadataEntity
import com.example.data.model.RestroomEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        RestroomEntity::class,
        CachedUserLocationEntity::class,
        OfflineCacheMetadataEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restroomDao(): RestroomDao
    abstract fun cachedLocationDao(): CachedLocationDao
    abstract fun offlineCacheMetadataDao(): OfflineCacheMetadataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dz_restrooms.db"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val items = InitialRestroomsData.sampleAlgerianRestrooms
                            INSTANCE?.restroomDao()?.insertAll(items)
                            INSTANCE?.offlineCacheMetadataDao()?.saveMetadata(
                                OfflineCacheMetadataEntity(
                                    id = 1,
                                    totalRestStopsCached = items.size,
                                    lastCachedTimestamp = System.currentTimeMillis(),
                                    cachedWilayasCount = items.map { it.wilaya }.distinct().size,
                                    isOfflineReady = true
                                )
                            )
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
