import android.content.Context
import androidx.preference.PreferenceManager
import com.tw_fieldboss_alarm.R
import com.tw_fieldboss_alarm.SettingsActivity
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

class NsecTimerChecker// Main constructor
    (Nsec: Long) {
    var Nsec = Nsec;

    var golontime = mutableListOf("06:00:00", "12:00:00", "18:00:00", "23:59:59")
    var golmodaptime = mutableListOf("05:00:00","13:00:00", "21:00:00")
    var arkantime = mutableListOf("14:30:00", "21:30:00")
    var sperchendtime = mutableListOf("01:00:00", "04:00:00", "08:00:00", "16:00:00", "19:00:00", "23:00:00")
    var defensetime = mutableListOf("02:00:00", "04:00:00", "06:00:00", "08:00:00", "10:00:00", "12:00:00",
                                    "14:00:00", "16:00:00", "18:00:00", "20:00:00", "22:00:00", "23:59:59")

    private var _golontime = dateTimeConvert(golontime)
    private var _golmodaptime = dateTimeConvert(golmodaptime)
    private var _arkantime = dateTimeConvert(arkantime)
    private var _sperchendtime = dateTimeConvert(sperchendtime)
    private var _defensetime = dateTimeConvert(defensetime)

    var golonAlarmEnabled: Boolean = false
    var golmodapAlarmEnabled: Boolean = false
    var arkanAlarmEnabled: Boolean = false
    var sperchendAlarmEnabled: Boolean = false
    var defenseAlarmEnabled: Boolean = false
//    private var NsecDuration: Duration = Duration.ZERO // API level 26
    private lateinit var nSecDuration: Duration

//        val golonAlarmEnabled = SettingsActivity.
//        val sharedPref = activity?.getSharedPreferences(getString(R.string.golon), Context.MODE_PRIVATE)

    fun checkAlarmText(currentTime: Date,timerInterval:Long){
//        var alarmText: MutableList<String> = ArrayList()
        val alarmTextBuilder = java.lang.StringBuilder() // build string without create string instance for performance
        if (golonAlarmEnabled && isTimeToAlarm(currentTime,_golontime,Nsec,timerInterval)) {
            alarmTextBuilder.append("[베리넨 루미] 골론 ")
            alarmTextBuilder.append(makeMMssString(Nsec))
        }
    }

    private fun isTimeToAlarm(currentTime: Date, referenceTimes: MutableList<Date>, Nsec: Long, timerInterval: Long): Boolean
    {
        var sdf = SimpleDateFormat("HH:mm:ss",Locale.KOREA)
        referenceTimes.forEach{ referenceTime ->
            val alarmTime: Double = referenceTime.time - Nsec*1e3
            var offBeforeCurrentTime = (currentTime.time - timerInterval) - alarmTime
            var onAfterCurrentTime = (currentTime.time) - alarmTime
            if (offBeforeCurrentTime>0 && onAfterCurrentTime <=0) {return true;}
        }
        return false; // 결국 못찾았을 때
    }

    private fun dateTimeConvert(strings: MutableList<String>): MutableList<Date>
    { // 나중에 비교는 date상태에서 하자. System.currentTimeMillis() > sdf.parse(valid_until).getTime() 이런식
        var sdf = SimpleDateFormat("HH:mm:ss",Locale.KOREA) // 컨버터 정의는 한번만
        return strings.map{ sdf.parse(it) }.toMutableList()
    }

    private fun makeMMssString(Nsec: Long): String
    {
        var minutes = (Nsec / 60).toString();
        var seconds = (Nsec % 60).toString(); // remainder
        return StringBuilder().append(minutes).append("분 ")
            .append(seconds).append("초 전!!").append("\n").toString()
//        when (Build.VERSION.SDK_INT) {
//            in 1..18 -> return StringBuilder().append(duration.toString()).append(" 전!!").append("\n").toString()
//            else -> return StringBuilder().append(duration.toString()).append(" 전!!").append(System.lineSeparator()).toString()
//        }
    }
}