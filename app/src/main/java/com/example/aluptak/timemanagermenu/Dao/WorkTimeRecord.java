package com.example.aluptak.timemanagermenu.Dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aluptak on 27/01/2016.
 */
public class WorkTimeRecord implements Serializable {

    public static final String HH_MM_SS = "HH.mm.ss";
    public static final String ALTERNATIVEFORMATING = " 00.00.00";
    private Date arrivalDate;
    private Date leaveDate;
    private final long TUNE_NUMBER = 3600000;
    private Date overtime;
    private int dayOfWeek;
    private long overTimeMillis;
    final long TUNENUMBER = 3600000; // 1H time shift
    final long WORKING_PERIOD = 30600000; // 8:30 H
    private SimpleDateFormat formatter;

    public WorkTimeRecord(Date arrivalTimeDate, Date leaveTimeDate, Date overtime) {
        super();
        this.arrivalDate = arrivalTimeDate;
        this.leaveDate = leaveTimeDate;
        this.overtime = overtime;

    }

    public WorkTimeRecord() {
        this.formatter = new SimpleDateFormat(HH_MM_SS);
    }

    public WorkTimeRecord(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getArrivalTimeDate() {
        return arrivalDate;
    }

    public void setArrivalTimeDate(Date arrivalTimeDate) {
        this.arrivalDate = arrivalTimeDate;
    }

    public Date getLeaveTimeDate() {
        return leaveDate;
    }

    public void setLeaveTimeDate(Date leaveTimeDate) {
        if (leaveTimeDate != null) this.leaveDate = leaveTimeDate;
    }

    public String getArrivalTime() {
        this.formatter = new SimpleDateFormat(HH_MM_SS);
        String whiteSpace = "";
        whiteSpace = getStringAlternative(whiteSpace);
        return (arrivalDate == null) ? ALTERNATIVEFORMATING : whiteSpace + formatter.format(arrivalDate);
    }

    private String getStringAlternative(String whiteSpace) {
        if (isMinusValues()) {
            whiteSpace = " ";
        }
        return whiteSpace;
    }

    private boolean isMinusValues() {
        return (((overTimeMillis / (1000 * 60 * 60)) % 24)) <= 0;
    }

    public String getLeaveTime() {
        this.formatter = new SimpleDateFormat(HH_MM_SS);
        String whiteSpace = "";
        whiteSpace = getStringAlternative(whiteSpace);
        return (leaveDate == null) ? ALTERNATIVEFORMATING : whiteSpace + formatter.format(leaveDate);
    }

    public String getWorkingTime() {
        //pozoro NUllPOINTER
        Date diff = new Date(this.leaveDate.getTime() - this.arrivalDate.getTime() - TUNE_NUMBER);
        String formatedWorkingTime = String.format("%d:%d:%d", diff.getHours(), diff.getMinutes(), diff.getSeconds());
        return formatedWorkingTime;
    }

    public long getOvertimeMillis(long last) {
        if (leaveDate != null && arrivalDate != null) {
            Date diff = new Date(this.leaveDate.getTime() - this.arrivalDate.getTime());
            long checkForWorkingTimePeriod = diff.getTime() - WORKING_PERIOD;
            this.overTimeMillis = (diff.getTime() + last) - WORKING_PERIOD;
            Date overTime = new Date(diff.getTime() - WORKING_PERIOD);
            return this.overTimeMillis;
        }
        throw new NullPointerException();
    }

    public Date getOvertime() {
        return overtime;
    }

     //      return (((overTimeMillis / (1000 * 60 * 60)) % 24)) + ":" +
    //      (((overTimeMillis / (1000 * 60)) % 60)) + ":" + ((overTimeMillis / 1000) % 60);
    public String getOverTimeString() {
        this.formatter = new SimpleDateFormat(HH_MM_SS);
        Date date = new Date(overTimeMillis);
        String minus = "";
        if (isMinusValues()) {
            minus = "-";
        } else {
            minus = " ";
        }
        return (overTimeMillis == 0) ? ALTERNATIVEFORMATING : minus + formatter.format(date);
    }


    public long getOverTimeMillis() {
        return overTimeMillis;
    }

    public void setOverTimeMillis(long overTimeMillis) {
        this.overTimeMillis = overTimeMillis;
    }

    public void setOvertime(Date overtime) {
        this.overtime = overtime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return "WorkTimeRecord{" +
                "work time" + getWorkingTime() +
                "arrivalDate=" + arrivalDate +
                ", leaveDate=" + leaveDate +
                "overtime millis: " + overTimeMillis +
                ", overTime= " + (((overTimeMillis / (1000 * 60 * 60)) % 24)) + ":" +
                (((overTimeMillis / (1000 * 60)) % 60)) + ":" + ((overTimeMillis / 1000) % 60) + "}";
    }
}
