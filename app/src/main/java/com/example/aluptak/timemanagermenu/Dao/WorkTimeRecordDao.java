package com.example.aluptak.timemanagermenu.Dao;

import android.content.Context;

import java.util.Date;
import java.util.List;

/**
 * Created by aluptak on 27/01/2016.
 */
public interface WorkTimeRecordDao {
    /**
     * Return all records in database
     *
     * @return List of records
     */
    public List<WorkTimeRecord> getAllWorkTimeRecords();

    /**
     * Return all records for actual week
     *
     * @return List of actual WorkTimeRecords
     */
    public List<WorkTimeRecord> getAllWorkForThisWeek();

    /**
     * Return specific week start point is Monday of that week
     *
     * @param mondayOfTheWeek
     * @return List specific week
     */
    public List<WorkTimeRecord> getAllWorkTimeForWeek(long mondayOfTheWeek);

    /**
     * Get Specific WorkTimeRecord mapped by date in millis
     *
     * @param date required time stamp
     * @return required WorkTimeRecord from DB
     */
    public WorkTimeRecord getWorkTimeRecord(long date);

    /**
     * Get Specific WorkTimeRecord mapped by date
     *
     * @param date required time stamp
     * @return required WorkTimeRecord from DB
     */
    public WorkTimeRecord getWorkTimeRecord(Date date);

    /**
     * Update specific WorkTimeRecord
     *
     * @param workTimeRecord
     */
    public void updateWorkTimeRecord(WorkTimeRecord workTimeRecord);

    /**
     * Delete specific WorkTimeRecord
     *
     * @param workTimeRecord
     */
    public void deleteWorkTimeRecord(WorkTimeRecord workTimeRecord);

    /**
     * Create new record in DB
     * @param workTimeRecord
     * @return true correct false incorrect
     */
    public boolean createWorkTimeRecord(WorkTimeRecord workTimeRecord);

    /**
     * Return yesterday WorkTimeRecord object
     * @return WorkTimeRecord
     */
    public WorkTimeRecord getYesterdayWorkTimeRecord();

}
