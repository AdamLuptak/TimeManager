package com.example.aluptak.timemanagermenu.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
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
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS WorkingTimeRecords");
        this.getWritableDatabase().execSQL(
                "create table WorkingTimeRecords " +
                        "(id integer primary key autoincrement, arrivalDate TEXT, leaveDate TEXT, overtime INTEGER)"
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table WorkingTimeRecords " +
                        "(id integer primary key autoincrement, arrivalDate TEXT, leaveDate TEXT, overtime INTEGER)"
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
        contentValues.put("overtime", overTime);
        long result = db.insert("WorkingTimeRecords", null, contentValues);
        return (result != -1) ? true : false;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where id=" + id + "", null);
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

    public WorkTimeRecord updateWorkTimeRecord(WorkTimeRecord workTimeRecord){
        //find record in DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from WorkingTimeRecords where arrivalDate = 1451631600000", null);
        WorkTimeRecord wk = new WorkTimeRecord();

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
                wk.getOvertimeMillis(testMillis);
            }
        return workTimeRecord;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
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

    public List<WorkTimeRecord> getAllWorkTimeRecords(){
        List<WorkTimeRecord> workTimeRecordList = new ArrayList<WorkTimeRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from WorkingTimeRecords", null);
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
                wk.getOvertimeMillis(testMillis);
            }
            workTimeRecordList.add(wk);
            res.moveToNext();
        }
        return workTimeRecordList;
    }
}
