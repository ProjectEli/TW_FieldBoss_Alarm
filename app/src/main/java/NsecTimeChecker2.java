//import android.content.Context;
//import android.content.SharedPreferences;
//
//import androidx.preference.Preference;
//import androidx.preference.PreferenceManager;
//
//import com.tw_fieldboss_alarm.SettingsActivity;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//public class NsecTimeChecker {
//    List<String> golontime = Arrays.asList("06:00:00", "12:00:00", "18:00:00", "23:59:59");
//    List<String> golmodaptime = Arrays.asList("05:00:00","13:00:00", "21:00:00");
//    List<String> arkantime = Arrays.asList("14:30:00", "21:30:00S");
//    List<String> sperchendtime = Arrays.asList("01:00:00", "04:00:00", "08:00:00", "16:00:00", "19:00:00", "23:00:00");
//    List<String> defensetime = Arrays.asList("02:00:00", "04:00:00", "06:00:00", "08:00:00", "10:00:00", "12:00:00",
//            "14:00:00", "16:00:00", "18:00:00", "20:00:00", "22:00:00", "23:59:59");
//
//    private List<Date> _golontime = dateTimeConvert(golontime);
//    private List<Date> _golmodaptime = dateTimeConvert(golmodaptime);
//    private List<Date> _arkantime = dateTimeConvert(arkantime);
//    private List<Date> _sperchendtime = dateTimeConvert(sperchendtime);
//    private List<Date> _defensetime = dateTimeConvert(defensetime);
//
//    public bool golonAlarmEnabled;
//    public bool golmodapAlarmEnabled;
//    public bool arkanAlarmEnabled;
//    public bool sperchendAlarmEnabled;
//    public bool defenseAlarmEnabled;
//
//    public NsecTimeChecker(int Nsec)
//    {
//        Initialize(Nsec);
//    }
//
//    private void Initialize(int Nsec)
//    {
//        Context context = SettingsActivity.SettingsFragment;
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.SettingsFragment);
//        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
////        golonAlarmEnabled =
//    }
//
//    public List<String> checkAlarmText(Date currentTime)
//    {
//        List<String> AlarmText = new ArrayList<String>();
//    }
//
//    private List<Date> dateTimeConvert(List<String> strings) throws ParseException {
////        return strings.stream() // API level 24
////                .map(element -> new SimpleDateFormat("HH:mm:ss", Locale.KOREA).parse(element))
////                .collect(Collectors.toList())
////    }
////        Function<String,Date> stringsToDate = new Function<String,Date>() { // Also API level 24
////            @Override
////            public String apply(String string) {
////                return new SimpleDateFormat("HH:mm:ss", Locale.KOREA).parse(string);
////            }
////        }
////        return Lists.transform(strings, stringsToDate);
//
////        List<Date> dateTimeList = new ArrayList<Date>(); // API level also 24
////
////        strings.forEach(dateString -> dateTimeList.add(
////                SimpleDateFormat("HH:mm:ss", Locale.KOREA).parse(dateString)
////        ));
//        List<Date> dateTimeList = new ArrayList<Date>();
//        for (String dateString: strings) {
//            dateTimeList.add(
//                    new SimpleDateFormat("HH:mm:ss", Locale.KOREA).parse(dateString)
//            );
//        }
//        return dateTimeList;
//    }
//
//    private String makeMMssString(Date date) {
//        return new SimpleDateFormat("%m분 %s초 전!").format(date);
//    }
//}
