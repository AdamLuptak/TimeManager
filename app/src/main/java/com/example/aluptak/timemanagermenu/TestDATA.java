package com.example.aluptak.timemanagermenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aluptak on 27/01/2016.
 */
public class TestDATA {
    final long TUNENUMBER = 3600000; // 1H time shift
    final long WORKING_PERIOD = 30600000; // 8:30 H
    private List<WorkTimeRecord> wk = new ArrayList<WorkTimeRecord>();
    long overTimeMillis;

    public TestDATA() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = "1-1-2016 8:00:0";
        Date dateObjectArrival = sdf.parse(dateString);
        dateString = "1-1-2016 15:30:0";
        Date leaveObjectArrival = sdf.parse(dateString);

        WorkTimeRecord wk = new WorkTimeRecord(dateObjectArrival, leaveObjectArrival, null);
        System.out.println("arrival time: " + wk.getArrivalTime());
        System.out.println("leave time: " + wk.getLeaveTime());
        System.out.println("totalWoking time this day: " + wk.getWorkingTime());


        overTimeMillis = wk.getOvertimeMillis(0l);
        System.out.println("overtime time: " + ((overTimeMillis / (1000 * 60 * 60)) % 24) + ":"
                + ((overTimeMillis / (1000 * 60)) % 60) + ":" + (overTimeMillis / 1000) % 60);



        System.out.println("");
        System.out.println("dalsi den");
        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateString = "2-1-2016 8:00:0";
        dateObjectArrival = sdf.parse(dateString);
        dateString = "2-1-2016 17:30:0";
        leaveObjectArrival = sdf.parse(dateString);
        WorkTimeRecord wk1 = new WorkTimeRecord(dateObjectArrival, leaveObjectArrival, null);
        System.out.println("arrival time: " + wk1.getArrivalTime());
        System.out.println("leave time: " + wk1.getLeaveTime());
        System.out.println("totalWoking time this day: " + wk1.getWorkingTime());

        overTimeMillis = wk1.getOvertimeMillis(overTimeMillis);
        System.out.println("overtime time: " + ((overTimeMillis / (1000 * 60 * 60)) % 24) + ":"
                + ((overTimeMillis / (1000 * 60)) % 60) + ":" + (overTimeMillis / 1000) % 60);


        System.out.println("");
        System.out.println("dalsi den");
        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateString = "3-1-2016 7:00:0";
        dateObjectArrival = sdf.parse(dateString);
        dateString = "3-1-2016 19:00:0";
        leaveObjectArrival = sdf.parse(dateString);
        WorkTimeRecord wk2 = new WorkTimeRecord(dateObjectArrival, leaveObjectArrival, null);
        System.out.println("arrival time: " + wk2.getArrivalTime());
        System.out.println("leave time: " + wk2.getLeaveTime());
        System.out.println("totalWoking time this day: " + wk2.getWorkingTime());

        overTimeMillis = wk2.getOvertimeMillis(overTimeMillis);
        System.out.println("overtime time: " + ((overTimeMillis / (1000 * 60 * 60)) % 24) + ":"
                + ((overTimeMillis / (1000 * 60)) % 60) + ":" + (overTimeMillis / 1000) % 60);

    }

    public Date getOvertime(Date arrivalDate, Date leaveDate) {

        Date diff = new Date(leaveDate.getTime() - arrivalDate.getTime());
        long checkForWorkingTimePeriod = diff.getTime() - WORKING_PERIOD;
        this.overTimeMillis = diff.getTime() - WORKING_PERIOD;
        Date overTime = new Date(diff.getTime() - WORKING_PERIOD);
        return overTime;
    }

}
