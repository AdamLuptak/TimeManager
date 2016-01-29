package com.example.aluptak.timemanagermenu.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mysql.jdbc.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aluptak on 26/01/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TimeManager.db";
    public static final String TABLE_NAME = "WorkingTimeRecords";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ARRIVALDATE = "arrivalDate";
    public static final String COLUMN_LEAVEDATE = "leaveDate";
    public static final String COLUMN_OVERTIME = "overtime";
    public static final String GET_LAST_BUT_ONE_ROW = "select * from(select * from WorkingTimeRecords  order by id desc limit 2) order by id asc limit 1;";
    public static final String GET_LAST_TIMERECORD = "select * from WorkingTimeRecords  order by id desc limit 1";
    public static final String DAY_OF_WEEK = "dayOfWeek";
    public static final String WORRKTIME = "workTime";
    public static final String SELECT_FROM_WORKING_TIME_RECORDS_WHERE_ID_SELECT_ID_FROM_WORKING_TIME_RECORDS_WHERE_DAY_OF_WEEK = "select * from WorkingTimeRecords where id >= (select id from WorkingTimeRecords where dayOfWeek = '";
    public static final String SEARCHING_MONDAY_FORMATER = "EEE-MM-dd-yyyy";
    public static final String CREATING_TABLE_WORKING_TIME_RECORDS = "create table WorkingTimeRecords " +
            "(id integer primary key autoincrement, arrivalDate TEXT, leaveDate TEXT, overtime INTEGER, workTime INTEGER, dayOfWeek TEXT)";
    public static final String DROP_TABLE_IF_EXISTS_WORKING_TIME_RECORDS = "DROP TABLE IF EXISTS WorkingTimeRecords";
    public static final String DROP_TABLE_IF_EXISTS_WORKING_TIME_RECORDS1 = "DROP TABLE IF EXISTS WorkingTimeRecords";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        resetDatabase();
    }

    private void resetDatabase() {
        this.getWritableDatabase().execSQL(DROP_TABLE_IF_EXISTS_WORKING_TIME_RECORDS1);
        this.getWritableDatabase().execSQL(CREATING_TABLE_WORKING_TIME_RECORDS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATING_TABLE_WORKING_TIME_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(DROP_TABLE_IF_EXISTS_WORKING_TIME_RECORDS);
        onCreate(db);
    }

    /**
     * Insert Start work time
     *
     * @param arrivalDate
     * @return True if took place in order
     */
    public boolean insertStartWorkTime(String arrivalDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ARRIVALDATE, arrivalDate);
        //contentValues.put("overtime", overTime);
        Date date = new Date(Long.valueOf(arrivalDate));
        DateFormat sdf1 = new SimpleDateFormat(SEARCHING_MONDAY_FORMATER);
        contentValues.put(DAY_OF_WEEK, sdf1.format(date));

        long result = db.insert(TABLE_NAME, null, contentValues);
        return (result != -1) ? true : false;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    /**
     * Return Id of required object from database
     *
     * @param workTimeRecord
     * @return Id of object
     */
    public Integer getIdOfWorkDayRecordMappedByArrivalTime(WorkTimeRecord workTimeRecord) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_ARRIVALDATE + " = " + workTimeRecord.getArrivalTimeDate().getTime() + "", null);
        res.moveToFirst();
        WorkTimeRecord wk = new WorkTimeRecord();

        int idProductIwantUpadte = 0;
        while (res.isAfterLast() == false) {
            idProductIwantUpadte = res.getInt(res.getColumnIndex(DBHelper.COLUMN_ID));
            res.moveToNext();
        }
        return idProductIwantUpadte;
    }

    /**
     * Update in DB workTimeRecord in DB and return update Object
     *
     * @param workTimeRecord object you want to update
     * @return Update Object WorkTimeRecord
     */
    public WorkTimeRecord updateWorkTimeRecord(WorkTimeRecord workTimeRecord) {
        SQLiteDatabase db = this.getReadableDatabase();
        //find record in DB
        int id = getIdOfWorkDayRecordMappedByArrivalTime(workTimeRecord);
        // Update record in DB
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LEAVEDATE, workTimeRecord.getLeaveTimeDate().getTime());
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});

        //reload actual product from DB
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_ARRIVALDATE + " = " + workTimeRecord.getArrivalTimeDate().getTime() + "", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String millistString = res.getString(res.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));
            Long testMillis = Long.parseLong(millistString);

            workTimeRecord.setArrivalTimeDate(new Date(testMillis));
            millistString = res.getString(res.getColumnIndex(DBHelper.COLUMN_LEAVEDATE));

            if (millistString != null) {
                testMillis = Long.parseLong(millistString);
                workTimeRecord.setLeaveTimeDate(new Date(testMillis));
            }

            testMillis = res.getLong(res.getColumnIndex(DBHelper.COLUMN_OVERTIME));

            if (testMillis != 0) {
                workTimeRecord.setOverTimeMillis(testMillis);
            }
            res.moveToNext();
        }
        return workTimeRecord;
    }

    /**
     * Return yesterday WorkTimeRecords
     *
     * @return overTimeFormLastButNotOne
     */
    public long getYesterday() {
        //select * from(select * from WorkingTimeRecords  order by id desc limit 2) order by id asc limit 1;
        long overTimeFormLastButNotOne = 0l;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(GET_LAST_BUT_ONE_ROW, null);
        res.moveToFirst();
        if (res != null) {
            while (res.isAfterLast() == false) {
                long testMillis = res.getLong(res.getColumnIndex(DBHelper.COLUMN_OVERTIME));
                if (testMillis != 0) {
                    overTimeFormLastButNotOne = testMillis;
                }
                res.moveToNext();
            }
        }
        return overTimeFormLastButNotOne;
    }

    /***
     * Return all records for actual week
     *
     * @return workTimeRecordList
     */
    public List<WorkTimeRecord> getAllWorkTimeRecordsForThisWeek() {
        List<WorkTimeRecord> workTimeRecordList = new ArrayList<WorkTimeRecord>();
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        DateFormat sdf12 = new SimpleDateFormat(SEARCHING_MONDAY_FORMATER);
        //'pi-01-29-2016'
        Cursor res = db.rawQuery(SELECT_FROM_WORKING_TIME_RECORDS_WHERE_ID_SELECT_ID_FROM_WORKING_TIME_RECORDS_WHERE_DAY_OF_WEEK + sdf12.format(calendar.getTime()) + "')", null);
        res.moveToFirst();
        String nam = res.getString(res.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));


        while (res.isAfterLast() == false) {
            WorkTimeRecord wk = new WorkTimeRecord();
            String millistString = res.getString(res.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));
            Long testMillis = Long.parseLong(millistString);
            wk.setArrivalTimeDate(new Date(testMillis));

            millistString = res.getString(res.getColumnIndex(DBHelper.COLUMN_LEAVEDATE));

            if (millistString != null) {
                testMillis = Long.parseLong(millistString);
                wk.setLeaveTimeDate(new Date(testMillis));
            }

            testMillis = res.getLong(res.getColumnIndex(DBHelper.COLUMN_OVERTIME));

            if (testMillis != 0) {
                //wk.getOvertimeMillisNEPOUZIVAT(testMillis);
            }

            millistString = res.getString(res.getColumnIndex(DBHelper.DAY_OF_WEEK));

            if (millistString != null) {
                wk.setDayOfWeek(millistString);
            }

            workTimeRecordList.add(wk);
            res.moveToNext();
        }
        return workTimeRecordList;
    }

    /**
     * Return last WorkTimeRecord
     *
     * @return workTimeRecord
     */
    public WorkTimeRecord getLastWorkTimeRecord() {
        WorkTimeRecord workTimeRecord = new WorkTimeRecord();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(GET_LAST_TIMERECORD, null);
        res.moveToFirst();
        if (res != null) {
            while (res.isAfterLast() == false) {
                String millistString = res.getString(res.getColumnIndex(DBHelper.COLUMN_ARRIVALDATE));

                if (millistString != null) {
                    Long testMillis = Long.parseLong(millistString);
                    workTimeRecord.setArrivalTimeDate(new Date(testMillis));
                }

                millistString = res.getString(res.getColumnIndex(DBHelper.COLUMN_LEAVEDATE));

                if (millistString != null) {
                    Long testMillis = Long.parseLong(millistString);
                    workTimeRecord.setLeaveTimeDate(new Date(testMillis));
                }
                res.moveToNext();
            }
        }
        return workTimeRecord;
    }

}
