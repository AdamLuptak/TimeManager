package com.example.aluptak.timemanagermenu;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.example.aluptak.timemanagermenu.Dao.WorkTimeRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ImageButton btn;
    Boolean inWork = false;

    TextView txtCurrentTime1;
    TextView txtCurrentTime2;
    TextView txtCurrentTime3;

    //main field WorkTimeRecord
    private WorkTimeRecord workTimeRecord;
    private WorkTimeController workTimeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
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

        //start vlastneho vlakna
        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();

        //listener to button
        btn = (ImageButton) findViewById(R.id.button_work);
        btn.setOnClickListener(this);

        //set time
        txtCurrentTime1 = (TextView) findViewById(R.id.time1);
        txtCurrentTime2 = (TextView) findViewById(R.id.time2);
        txtCurrentTime3 = (TextView) findViewById(R.id.time3);


        //initialization of workTimeController
        workTimeController = new WorkTimeController(this);
        workTimeRecord = workTimeController.getActualWorkingRecord();

        //mus byt zatial natvrdo treba osetrit NULLPOINTER
        txtCurrentTime1.setText(workTimeRecord.getOverTimeString());
        txtCurrentTime2.setText(workTimeRecord.getArrivalTime());
        txtCurrentTime3.setText(workTimeRecord.getLeaveTime());

    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String time = getTime();
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
        String time = getTime();
        if (!inWork) {
            btn.setBackgroundResource(R.drawable.stop);
            workTimeController.writeArrivalTime();
            inWork = true;
        } else {
            btn.setBackgroundResource(R.drawable.play);
            workTimeController.writeLeaveTime();
            txtCurrentTime3.setText(workTimeRecord.getLeaveTime());
            inWork = false;
        }
        workTimeRecord = workTimeController.getActualWorkingRecord();
        txtCurrentTime1.setText(workTimeRecord.getOverTimeString());
        txtCurrentTime2.setText(workTimeRecord.getArrivalTime());
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

}
