package com.example.aluptak.timemanagermenu.entities;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by aluptak on 25/01/2016.
 */
public class DateHelper {

    /**
     * Convert a millisecond duration to a string format if its more than 1 hout put there hour
     * @author ALuptak
     * @param currentMillis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String formatTimeMillis(long currentMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(currentMillis);
        currentMillis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentMillis);
        currentMillis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentMillis);
        currentMillis -= TimeUnit.SECONDS.toMillis(seconds);
        long millis = TimeUnit.MILLISECONDS.toMillis(currentMillis);

        StringBuilder sb = new StringBuilder();
        if (hours >= 1) {
            sb.append((hours < 10) ? "0" : "");
            sb.append(hours);
            sb.append(":");
        }
        sb.append((minutes < 10) ? "0" : "");
        sb.append(minutes);
        sb.append(":");
        sb.append((seconds < 10) ? "0" : "");
        sb.append(seconds);
        sb.append(":");
        if (!(hours >= 1)) {
            sb.append(millis / 100);
        }

        return (sb.toString());
    }

    public static String getDayVerbose(Date date){
        return (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
    }

    public static String getYear(Date date){
       return (String) android.text.format.DateFormat.format("yyyy", date); //2013
    }

    public static String getMonthVerbose(Date date){
        return (String) android.text.format.DateFormat.format("MMM", date); //Jun
    }

    public static String getMonthNumber(Date date){
       return (String) android.text.format.DateFormat.format("MM", date); //06
    }

    public static String getActualTime(Date date){
        return (String) android.text.format.DateFormat.format("HH:mm:ss", date); //06
    }

}
