package com.example.aluptak.timemanagermenu;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by vmino on 27/01/2016.
 */
public class WorkTimeController {
    private String overTime;
    private String leaveTime;
    private String leaveTimeWithOT;


    public String getOvertime() {
// TODO: 27/01/2016
        return "+1:25";
    }

    public String getLeaveTime() {
// TODO: 27/01/2016
        return "17:30";
    }

    public String getLeaveTimeWithOT() {
// TODO: 27/01/2016
        return "16:05";
    }

    /**
     * Writes current time into DB by DBHelper as ArrivalTime
     * counts new leaveTime, leaveTimeWithOT
     */
    public void writeArrivalTime() {
// TODO: 27/01/2016
    }

    /**
     * Writes current time into DB by DBHelper as LeaveTime
     * counts new OverTime
     */
    public void writeLeaveTime() {
// TODO: 27/01/2016
    }

}
