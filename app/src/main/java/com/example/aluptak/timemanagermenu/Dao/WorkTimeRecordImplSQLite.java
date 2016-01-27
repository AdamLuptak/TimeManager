package com.example.aluptak.timemanagermenu.Dao;

import java.util.Date;
import java.util.List;

/**
 * Created by aluptak on 27/01/2016.
 */
public class WorkTimeRecordImplSQLite implements WorkTimeRecordDao  {

    DBHelper myDb;

    public WorkTimeRecordImplSQLite(DBHelper myDb){
        this.myDb = myDb;
    }

    @Override
    public List<WorkTimeRecord> getAllWorkTimeRecords() {
        return null;
    }

    @Override
    public List<WorkTimeRecord> getAllWorkForThisWeek() {
        return null;
    }

    @Override
    public List<WorkTimeRecord> getAllWorkTimeForWeek(long mondayOfTheWeek) {
        return null;
    }

    @Override
    public WorkTimeRecord getWorkTimeRecord(long date) {
        return null;
    }

    @Override
    public WorkTimeRecord getWorkTimeRecord(Date date) {
        return null;
    }

    @Override
    public void updateWorkTimeRecord(WorkTimeRecord workTimeRecord) {

    }

    @Override
    public void deleteWorkTimeRecord(WorkTimeRecord workTimeRecord) {

    }

    @Override
    public void CreateWorkTimeRecord(WorkTimeRecord workTimeRecord) {

    }
}
