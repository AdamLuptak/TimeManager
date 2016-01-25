package com.example.aluptak.timemanagermenu.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aluptak on 25/01/2016.
 */
public class Week {

    private List<Day> days = new ArrayList<Day>();

    public Week() {
    }

    public Week(List<Day> days) {
        this.days = days;
    }

    public List<Day> getDays() {
        return this.days;
    }

    public Day getDay(DayOfWeek dayLookingFor) {
        for (Day day : this.days
                ) {
            if (day.getDay().equals(dayLookingFor)) {
                return day;
            }
        }
        return null;
    }

    public void addDay(Day day) {
        this.days.add(day);
    }

    public long getWorkTime() {
        long totalWorkingTime = 0;
        for (Day day : this.days
                ) {
            totalWorkingTime += day.getWorkTime();
        }
        return totalWorkingTime;
    }

    @Override
    public String toString() {
        return "Week{" +
                "days=" + days +
                '}';
    }
}
