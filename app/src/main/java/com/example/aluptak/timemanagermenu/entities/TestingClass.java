package com.example.aluptak.timemanagermenu.entities;

import java.util.Date;

/**
 * Created by aluptak on 25/01/2016.
 */
public class TestingClass {

    public static Week week = new Week();

    public static void makeTestDate(){
        for (int i = 0 ; i < 7; i++){
            week.addDay(new Day(System.currentTimeMillis(),System.currentTimeMillis(), new Date()));
        }
    }


}
