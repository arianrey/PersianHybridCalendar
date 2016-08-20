package com.ananasdevs.test;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hybriddevs.persianhybridcalendar.calendar.persian.CalendarTool;
import com.hybriddevs.persianhybridcalendar.view.CalendarView;
import com.hybriddevs.persianhybridcalendar.view.CalendarViewDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class MainActivity extends Activity {

    CalendarView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashSet<Date> dates = new HashSet<>();
        Calendar calendar = Calendar.getInstance();
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        dates.add(calendar.getTime());

        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        cv = ((CalendarView)findViewById(R.id.calendar_view));
        cv.setDateSystem(CalendarView.DateSystem.Persian);
        cv.setFontSize(CalendarView.FontSize.Large);
        cv.setDefaultSelectedDates(dates);
        cv.setMultiSelect(true);
        cv.setTypefaceFarsi(Typeface.createFromAsset(getAssets(), "BYekan.ttf"));
        //cv.updateCalendar(events);
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public boolean onDayLongPress(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onDayPress(Date date) {
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Persian(View v) {
        cv.setDateSystem(CalendarView.DateSystem.Persian);
    }

    public void Gregorian(View v) {
        cv.setDateSystem(CalendarView.DateSystem.Gregorian);
    }

    public void openDialog(View view) {
        CalendarViewDialog calendarViewDialog = new CalendarViewDialog(this, CalendarView.DateSystem.Persian, CalendarView.FontSize.Large, false, Typeface.createFromAsset(getAssets(), "BYekan.ttf")) {
            @Override
            public void onClickOK() {
                HashSet<Date> dates = getSelectedDates();
                String s = "";
                for(Date date : dates) {
                    s += date.toString();
                }
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        };
        calendarViewDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
