package com.example.aluptak.timemanagermenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluptak.timemanagermenu.Dao.DBHelper;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecord;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecordDao;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecordImplSQLite;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ImageButton btn;
    Boolean inWork = false;

    /**
     * if true button will add arrivalTime else add leave time
     */
    private boolean addArrivalTime = true;
    DBHelper myDb;
    WorkTimeRecordDao workTimeRecordDao;
    private TestDATA testData;
    private WorkTimeController workTimeController;

    public boolean addTime() throws ParseException {
//        long testMillisArrived = 1451631600000L;
//        return workTimeRecordDao.createWorkTimeRecord(new WorkTimeRecord(new Date(testMillisArrived)));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Start updateTime thread
        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();

        //listener to button
        btn = (ImageButton) findViewById(R.id.button_work);
        btn.setOnClickListener(this);


        //create DAO database for insert test data just uncoment
//        workTimeRecordDao = new WorkTimeRecordImplSQLite(new DBHelper(this));
//        try {
//            testData = new TestDATA(this);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        try {
//            testData.testDataToSQLITEDB();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        workTimeController = new WorkTimeController(this);
        TextView txtCurrentTime1 = (TextView) findViewById(R.id.time1);
        //Need load from db overtime Value
        txtCurrentTime1.setText("Over time is: " + workTimeController.getOverTimeFromTesterday());

        //reset actual day
        workTimeController.resetActualWorkingRecord();

    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
//                    TextView txtCurrentTime1 = (TextView) findViewById(R.id.time1);
//                    String time = getTime();
//                    txtCurrentTime1.setText("Current time: " + time);
                } catch (Exception e) {
                }
            }
        });
    }

    @NonNull
    private String getTime() {
        Date dt = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(dt);
    }

    class CountDownRunner implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {

                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        updateStatus();
    }

    private void updateStatus() {
        TextView txtCurrentTime1 = (TextView) findViewById(R.id.time1);
        TextView txtCurrentTime2 = (TextView) findViewById(R.id.time2);
        TextView txtCurrentTime3 = (TextView) findViewById(R.id.time3);

        String time = getTime();
        if (!inWork) {
            inWork = true;
            btn.setBackgroundResource(R.drawable.stop);
            workTimeController.writeArrivalTime();
            txtCurrentTime2.setText("Arrival time: " + workTimeController.getActualWorkingRecord().getArrivalTime());
            txtCurrentTime3.setText("");
        } else {
            btn.setBackgroundResource(R.drawable.play);
            workTimeController.writeLeaveTime();
            txtCurrentTime3.setText("Leaving time: " + workTimeController.getActualWorkingRecord().getLeaveTime());
            txtCurrentTime1.setText("Over time is: " + workTimeController.getActualWorkingRecord().getOverTimeString());
            inWork = false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        workTimeController.resetActualWorkingRecord();
        super.onResume();
    }
}
