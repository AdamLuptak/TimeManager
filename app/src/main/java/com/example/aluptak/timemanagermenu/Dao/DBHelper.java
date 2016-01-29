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
    public static final String CONTACTS_TABLE_NAME = "WorkingTimeRecords";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_ARRIVALDATE = "arrivalDate";
    public static final String CONTACTS_COLUMN_LEAVEDATE = "leaveDate";
    public static final String CONTACTS_COLUMN_OVERTIME = "overtime";
    public static final String GET_LAST_BUT_ONE_ROW = "select * from(select * from WorkingTimeRecords  order by id desc limit 2) order by id asc limit 1;";
    public static final String GET_LAST_TIMERECORD = "select * from WorkingTimeRecords  order by id desc limit 1";
    public static final String DAY_OF_WEEK = "dayOfWeek";
    public static final String WORRKTIME = "workTime";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS WorkingTimeRecords");
        this.getWritableDatabase().execSQL(
                "create table WorkingTimeRecords " +
                        "(id integer primary key autoincrement, arrivalDate TEXT, leaveDate TEXT, overtime INTEGER, workTime INTEGER, dayOfWeek TEXT)"
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table WorkingTimeRecords " +
                        "(id integer primary key autoincrement, arrivalDate TEXT, leaveDate TEXT, overtime INTEGER ,workTime INTEGER, dayOfWeek TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS WorkingTimeRecords");
        onCreate(db);
    }

    public boolean insertContact(String arrivalDate, String leaveTime, Long overTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("arrivalDate", arrivalDate);
        contentValues.put("leaveDate", leaveTime);
        contentValues.put("overtime", overTime);
        long result = db.insert("WorkingTimeRecords", null, contentValues);
        return (result != -1) ? true : false;
    }

    public boolean insertStartWorkTime(String arrivalDate, Long overTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("arrivalDate", arrivalDate);
        //contentValues.put("overtime", overTime);
        Date date = new Date(Long.valueOf(arrivalDate));
        DateFormat sdf1 = new SimpleDateFormat("EEE-MM-dd-yyyy");
        contentValues.put("dayOfWeek", sdf1.format(date));

        long result = db.insert("WorkingTimeRecords", null, contentValues);
        return (result != -1) ? true : false;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from WorkingTimeRecords where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(Integer id, String name, String phone, String email, String street, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer getIdOfWorkDayRecordMappedByArrivalTime(WorkTimeRecord workTimeRecord) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from WorkingTimeRecords where arrivalDate = " + workTimeRecord.getArrivalTimeDate().getTime() + "", null);
        res.moveToFirst();
        WorkTimeRecord wk = new WorkTimeRecord();

        int idProductIwantUpadte = 0;
        while (res.isAfterLast() == false) {
            idProductIwantUpadte = res.getInt(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID));
            res.moveToNext();
        }
        return idProductIwantUpadte;
    }

    /**
     * Update in DB workTimeRecord in DB and return update Object
     *
     * @param workTimeRecord
     * @return Update Object WorkTimeRecord
     */
    public WorkTimeRecord updateWorkTimeRecord(WorkTimeRecord workTimeRecord) {
        SQLiteDatabase db = this.getReadableDatabase();
        //find record in DB
        int id = getIdOfWorkDayRecordMappedByArrivalTime(workTimeRecord);
        // Update record in DB
        ContentValues contentValues = new ContentValues();
        contentValues.put("overtime", workTimeRecord.getOverTimeMillis());
        contentValues.put("leaveDate", workTimeRecord.getLeaveTimeDate().getTime());
        db.update("WorkingTimeRecords", contentValues, "id = ? ", new String[]{Integer.toString(id)});

        //reload actual product from DB
        Cursor res = db.rawQuery("select * from WorkingTimeRecords where arrivalDate = " + workTimeRecord.getArrivalTimeDate().getTime() + "", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String millistString = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_ARRIVALDATE));
            Long testMillis = Long.parseLong(millistString);

            workTimeRecord.setArrivalTimeDate(new Date(testMillis));
            millistString = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_LEAVEDATE));

            if (millistString != null) {
                testMillis = Long.parseLong(millistString);
                workTimeRecord.setLeaveTimeDate(new Date(testMillis));
            }

            testMillis = res.getLong(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_OVERTIME));

            if (testMillis != 0) {
                workTimeRecord.setOverTimeMillis(testMillis);
            }
            res.moveToNext();
        }
        return workTimeRecord;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public long getYesterday() {
        //select * from(select * from WorkingTimeRecords  order by id desc limit 2) order by id asc limit 1;
        long overTimeFormLastButNotOne = 0l;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(GET_LAST_BUT_ONE_ROW, null);
        res.moveToFirst();
        if (res != null) {
            while (res.isAfterLast() == false) {
                long testMillis = res.getLong(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_OVERTIME));
                if (testMillis != 0) {
                    overTimeFormLastButNotOne = testMillis;
                }
                res.moveToNext();
            }
        }
        return overTimeFormLastButNotOne;
    }


    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from WorkingTimeRecords", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ARRIVALDATE)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<WorkTimeRecord> getAllWorkTimeRecords() {
        List<WorkTimeRecord> workTimeRecordList = new ArrayList<WorkTimeRecord>();
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        DateFormat sdf12 = new SimpleDateFormat("EEE-MM-dd-yyyy");
        //'pi-01-29-2016'
        Cursor res = db.rawQuery("select * from WorkingTimeRecords where dayOfWeek = '"+ sdf12.format(calendar.getTime())+"'" , null);
        res.moveToFirst();
        String nam = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_ARRIVALDATE));
        WorkTimeRecord wk = new WorkTimeRecord();

        while (res.isAfterLast() == false) {

            String millistString = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_ARRIVALDATE));
            Long testMillis = Long.parseLong(millistString);
            wk.setArrivalTimeDate(new Date(testMillis));

            millistString = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_LEAVEDATE));

            if (millistString != null) {
                testMillis = Long.parseLong(millistString);
                wk.setLeaveTimeDate(new Date(testMillis));
            }

            testMillis = res.getLong(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_OVERTIME));

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

    public WorkTimeRecord getLastWorkTimeRecord() {
        WorkTimeRecord workTimeRecord = new WorkTimeRecord();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(GET_LAST_TIMERECORD, null);
        res.moveToFirst();
        if (res != null) {
            while (res.isAfterLast() == false) {
                String millistString = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_ARRIVALDATE));

                if (millistString != null) {
                    Long testMillis = Long.parseLong(millistString);
                    workTimeRecord.setArrivalTimeDate(new Date(testMillis));
                }

                millistString = res.getString(res.getColumnIndex(DBHelper.CONTACTS_COLUMN_LEAVEDATE));

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
