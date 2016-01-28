package com.example.aluptak.timemanagermenu;

import android.content.Context;
import android.widget.Toast;

import com.example.aluptak.timemanagermenu.Dao.DBHelper;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecord;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecordDao;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecordImplSQLite;

import java.util.Date;

/**
 * Created by vmino on 27/01/2016.
 */
public class WorkTimeController {
    private String overTime;
    private String leaveTime;
    private String leaveTimeWithOT;
    private WorkTimeRecord actualWorkingRecord;
    private WorkTimeRecordDao workTimeRecordDao;

    public WorkTimeController(Context context) {
        this.workTimeRecordDao = new WorkTimeRecordImplSQLite(new DBHelper(context));
        this.actualWorkingRecord = new WorkTimeRecord();
    }

    public WorkTimeRecord getActualWorkingRecord() {
        return actualWorkingRecord;
    }

    public void setActualWorkingRecord(WorkTimeRecord actualWorkingRecord) {
        this.actualWorkingRecord = actualWorkingRecord;
    }

    public void resetActualWorkingRecord() {
        if (this.getActualWorkingRecord() != null) {
            actualWorkingRecord = null;
        }
    }

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
        this.actualWorkingRecord = new WorkTimeRecord(new Date());
        this.workTimeRecordDao.createWorkTimeRecord(this.actualWorkingRecord);
    }

    /**
     * Writes current time into DB by DBHelper as LeaveTime
     * counts new OverTime
     */
    public void writeLeaveTime() {
        long overTimeFromYesterday = this.workTimeRecordDao.getYesterdayOverTime();
        this.actualWorkingRecord.setLeaveTimeDate(new Date());
        long overTimeForThisDay = actualWorkingRecord.getOvertimeMillis(overTimeFromYesterday);
        actualWorkingRecord.setOverTimeMillis(overTimeForThisDay);
        this.workTimeRecordDao.updateWorkTimeRecord(actualWorkingRecord);
    }

    public String getOverTimeFromTesterday() {
        long overTimeFromYesterday = this.workTimeRecordDao.getYesterdayOverTime();
        return (((overTimeFromYesterday / (1000 * 60 * 60)) % 24)) + ":" +
                (((overTimeFromYesterday / (1000 * 60)) % 60)) + ":" + ((overTimeFromYesterday / 1000) % 60);
    }

}
