package com.tw_fieldboss_alarm.alarms

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmRoomDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        // 싱글톤으로 구현함
        @Volatile
        private var INSTANCE: AlarmRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AlarmRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmRoomDatabase::class.java,
                    "alarm_database"
                ).addCallback(AlarmDataBaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance // return instance
            }
        }

        private class AlarmDataBaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                Log.d("EliAlarmDb","DB 생성됨")
                super.onCreate(db)
                INSTANCE?.let { database -> scope.launch {
                    populateDatabase(database.alarmDao())
                } }
            }
        }

        suspend fun populateDatabase(alarmDao: AlarmDao) {
            alarmDao.deleteAll() // delete all content here

            // add sample alarms
//            var alarm = Alarm("오후 9시 00분, 골모답")
//            alarmDao.insert(alarm)
//            alarm = Alarm("오후 9시 30분, 아칸")
//            alarmDao.insert(alarm)
//            alarmDao.delete(alarm)
//            alarmDao.insert(Alarm("세 번째 알람"))
        }
    }
}