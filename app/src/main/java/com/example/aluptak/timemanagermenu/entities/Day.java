package com.example.aluptak.timemanagermenu.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aluptak on 25/01/2016.
 */
public class Day implements Serializable {

    private long arrivalTime;
    private long leaveTime;
    private DayOfWeek day;
    private Date date;

    public Day(long arrivalTime, long leaveTime, Date date) {
        this.arrivalTime = arrivalTime;
        this.leaveTime = leaveTime;
        this.date = date;
    }

    public Day(long arrivalTime, long leaveTime) {
        this.arrivalTime = arrivalTime;
        this.leaveTime = leaveTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public long getWorkTime(){
        return this.leaveTime - this.arrivalTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Day{" +
                "arrivalTime=" + arrivalTime +
                ", leaveTime=" + leaveTime +
                ", day=" + day +
                ", date=" + date +
                '}';
    }
}
