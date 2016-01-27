package com.example.aluptak.timemanagermenu.Dao;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aluptak on 27/01/2016.
 */
public class WorkTimeRecord implements Serializable {

    private Date arrivalDate;
    private Date leaveDate;
    private final long TUNE_NUMBER = 3600000;
    private Date overtime;
    private int dayOfWeek;
    private long overTimeMillis;
    final long TUNENUMBER = 3600000; // 1H time shift
    final long WORKING_PERIOD = 30600000; // 8:30 H

    public WorkTimeRecord(Date arrivalTimeDate, Date leaveTimeDate, Date overtime) {
        super();
        this.arrivalDate = arrivalTimeDate;
        this.leaveDate = leaveTimeDate;
        this.overtime = overtime;
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
        this.leaveDate = leaveTimeDate;
    }

    public String getArrivalTime() {
        String formatedArrivalTime = String.format("%d:%d:%d", arrivalDate.getHours(), arrivalDate.getMinutes(),
                arrivalDate.getSeconds());
        return formatedArrivalTime;
    }

    public String getLeaveTime() {
        String formatedArrivalTime = String.format("%d:%d:%d", leaveDate.getHours(), leaveDate.getMinutes(),
                leaveDate.getSeconds());
        return formatedArrivalTime;
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

    public void setOvertime(Date overtime) {
        this.overtime = overtime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
