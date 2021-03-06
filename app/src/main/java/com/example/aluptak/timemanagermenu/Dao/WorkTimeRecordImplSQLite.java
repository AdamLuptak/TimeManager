package com.example.aluptak.timemanagermenu.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

/**
 * Created by aluptak on 27/01/2016.
 */
public class WorkTimeRecordImplSQLite implements WorkTimeRecordDao {

    DBHelper myDb;

    public WorkTimeRecordImplSQLite(DBHelper myDb) {
        this.myDb = myDb;
    }

    @Override
    public List<WorkTimeRecord> getAllWorkTimeRecords() {
        return null;
    }

    @Override
    public List<WorkTimeRecord> getAllWorkForThisWeek() {
        return myDb.getAllWorkTimeRecordsForThisWeek();
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
        myDb.updateWorkTimeRecord(workTimeRecord);
        Log.e("Check actual day data: ", myDb.updateWorkTimeRecord(workTimeRecord).toString());
    }

    @Override
    public void deleteWorkTimeRecord(WorkTimeRecord workTimeRecord) {

    }

    /**
     * Function put to database new WorkTimeRecord if not succes Allert box will pop up
     *
     * @param workTimeRecord
     */
    @Override
    public boolean createWorkTimeRecord(WorkTimeRecord workTimeRecord) {
        if (myDb.insertStartWorkTime(String.valueOf(workTimeRecord.getArrivalTimeDate().getTime()))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public WorkTimeRecord getYesterdayWorkTimeRecord() {
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor res = db.rawQuery("select * from WorkingTimeRecords where id=" + 1 + "", null);
        res.moveToFirst();
        String nam = res.getString(res.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));
        Toast.makeText(null, "pridal som do databazy " + nam, Toast.LENGTH_SHORT).show();
        return null;
    }

    /**
     * Return overtime in millis from yesterday
     *
     * @return
     */
    @Override
    public long getYesterdayOverTime() {
        return myDb.getYesterday();
    }

    @Override
    public WorkTimeRecord getLastRecord() {
        return myDb.getLastWorkTimeRecord();
    }
}
