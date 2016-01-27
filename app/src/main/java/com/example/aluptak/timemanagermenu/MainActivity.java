package com.example.aluptak.timemanagermenu;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public void addTime(View view) throws ParseException {

        addArrivalTime = !addArrivalTime;
        myDb = new DBHelper(this);
        if(myDb.insertContact(new Date(),new Date(),1000000l)){
            Toast.makeText(this, "Dalo do databazi", Toast.LENGTH_SHORT).show();
        }

        Iterator itr = myDb.getAllCotacts().iterator();
        while(itr.hasNext()){
            Object element = itr.next();
            String h = element.toString();
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
            Date date = format.parse(h);
            Log.e("moja", date.toString());
        }
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

    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    TextView txtCurrentTime1 = (TextView) findViewById(R.id.time1);
                    String time = getTime();
                    txtCurrentTime1.setText("Current time: " + time);
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
        TextView txtCurrentTime2 = (TextView) findViewById(R.id.time2);
        TextView txtCurrentTime3 = (TextView) findViewById(R.id.time3);
        String time = getTime();
        if (!inWork) {
            btn.setBackgroundResource(R.drawable.stop);
            txtCurrentTime2.setText("Arrival time: " + time);
            txtCurrentTime3.setText("");
            inWork = true;
        } else {
            btn.setBackgroundResource(R.drawable.play);
            txtCurrentTime3.setText("Leaving time: " + time);
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
}
