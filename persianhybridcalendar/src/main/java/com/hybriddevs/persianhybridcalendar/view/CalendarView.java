package com.hybriddevs.persianhybridcalendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybriddevs.persianhybridcalendar.R;
import com.hybriddevs.persianhybridcalendar.calendar.persian.CalendarTool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AdapterView;

/**
 * Created by arian on 2/19/2016.
 */


public class CalendarView extends LinearLayout {
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();
    private CalendarTool currentDatePersian = new CalendarTool();
    private CalendarTool currentMonthPersian = new CalendarTool();
    private Date selectedDate = new Date();

    //event handling
    private EventHandler eventHandler = null;

    //Date system enum declration
    public static enum DateSystem {

        Gregorian("میلادی"),
        Persian("هجری شمسی"),
        Arabic("هجری قمری");

        DateSystem(String system) {

        }

    }

    public enum FontSize {
        Large,
        Medium,
        Small;
    }

    FontSize fontSize = FontSize.Medium;

    //Date System of this CalendarView
    private DateSystem dateSystem;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    private TextView day1;
    private TextView day2;
    private TextView day3;
    private TextView day4;
    private TextView day5;
    private TextView day6;
    private TextView day7;

    // seasons' rainbow
    int[] rainbow = new int[]{
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeasonGregorian = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};
    int[] monthSeasonPersian = new int[]{3, 3, 3, 0, 0, 0, 1, 1, 1, 2, 2, 2};
    int[] monthSeasonArabic = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    Typeface typefaceFarsi;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        dateSystem = DateSystem.Gregorian;

        updateCalendar();
    }

    private void SetDayNames() {
        String[] names = {""};
        if (dateSystem.equals(DateSystem.Gregorian)) {
            names = new String[]{
                    "SUN",
                    "MON",
                    "TUE",
                    "WED",
                    "THU",
                    "FRI",
                    "SAT"};
        } else if (dateSystem.equals(DateSystem.Persian)) {
            names = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
        } else if (dateSystem.equals(DateSystem.Arabic)) {
            names = new String[]{"ش", "ی", "د", "س", "چ", "پ", "ج"};
        }

        day1 = (TextView) findViewById(R.id.day1);
        day1.setText(names[0]);
        day2 = (TextView) findViewById(R.id.day2);
        day2.setText(names[1]);
        day3 = (TextView) findViewById(R.id.day3);
        day3.setText(names[2]);
        day4 = (TextView) findViewById(R.id.day4);
        day4.setText(names[3]);
        day5 = (TextView) findViewById(R.id.day5);
        day5.setText(names[4]);
        day6 = (TextView) findViewById(R.id.day6);
        day6.setText(names[5]);
        day7 = (TextView) findViewById(R.id.day7);
        day7.setText(names[6]);
        if (fontSize.equals(FontSize.Large)) {
            day1.setTextAppearance(getContext(), R.style.FontSizeLarge);
            day2.setTextAppearance(getContext(), R.style.FontSizeLarge);
            day3.setTextAppearance(getContext(), R.style.FontSizeLarge);
            day4.setTextAppearance(getContext(), R.style.FontSizeLarge);
            day5.setTextAppearance(getContext(), R.style.FontSizeLarge);
            day6.setTextAppearance(getContext(), R.style.FontSizeLarge);
            day7.setTextAppearance(getContext(), R.style.FontSizeLarge);
        } else if (fontSize.equals(FontSize.Medium)) {
            day1.setTextAppearance(getContext(), R.style.FontSizeMedium);
            day2.setTextAppearance(getContext(), R.style.FontSizeMedium);
            day3.setTextAppearance(getContext(), R.style.FontSizeMedium);
            day4.setTextAppearance(getContext(), R.style.FontSizeMedium);
            day5.setTextAppearance(getContext(), R.style.FontSizeMedium);
            day6.setTextAppearance(getContext(), R.style.FontSizeMedium);
            day7.setTextAppearance(getContext(), R.style.FontSizeMedium);
        } else {
            day1.setTextAppearance(getContext(), R.style.FontSizeSmall);
            day2.setTextAppearance(getContext(), R.style.FontSizeSmall);
            day3.setTextAppearance(getContext(), R.style.FontSizeSmall);
            day4.setTextAppearance(getContext(), R.style.FontSizeSmall);
            day5.setTextAppearance(getContext(), R.style.FontSizeSmall);
            day6.setTextAppearance(getContext(), R.style.FontSizeSmall);
            day7.setTextAppearance(getContext(), R.style.FontSizeSmall);
        }
        setDaysTypeface();
    }

    private void setDaysTypeface() {
        if (dateSystem.equals(DateSystem.Persian) || dateSystem.equals(DateSystem.Arabic)) {
            day1.setTypeface(typefaceFarsi, Typeface.BOLD);
            day2.setTypeface(typefaceFarsi, Typeface.BOLD);
            day3.setTypeface(typefaceFarsi, Typeface.BOLD);
            day4.setTypeface(typefaceFarsi, Typeface.BOLD);
            day5.setTypeface(typefaceFarsi, Typeface.BOLD);
            day6.setTypeface(typefaceFarsi, Typeface.BOLD);
            day7.setTypeface(typefaceFarsi, Typeface.BOLD);
        } else {
            day1.setTypeface(null, Typeface.BOLD);
            day2.setTypeface(null, Typeface.BOLD);
            day3.setTypeface(null, Typeface.BOLD);
            day4.setTypeface(null, Typeface.BOLD);
            day5.setTypeface(null, Typeface.BOLD);
            day6.setTypeface(null, Typeface.BOLD);
            day7.setTypeface(null, Typeface.BOLD);
        }
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        if (fontSize != null)
            this.fontSize = fontSize;

        updateCalendar();
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                currentMonthPersian.setIranianDate(currentMonthPersian.getIranianMonth() != 12 ? currentMonthPersian.getIranianYear() : currentMonthPersian.getIranianYear() + 1,
                        currentMonthPersian.getIranianMonth() != 12 ? currentMonthPersian.getIranianMonth() + 1 : 1, 1);

                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                currentMonthPersian.setIranianDate(currentMonthPersian.getIranianMonth() != 1 ? currentMonthPersian.getIranianYear() : currentMonthPersian.getIranianYear() - 1,
                        currentMonthPersian.getIranianMonth() != 1 ? currentMonthPersian.getIranianMonth() - 1 : 12, 1);

                updateCalendar();
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                return true;
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // handle press

                selectedDate = (Date) parent.getItemAtPosition(position);
                updateCalendar();
                eventHandler.onDayPress(selectedDate);
            }
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events) {
        SetDayNames();
        if (dateSystem.equals(DateSystem.Gregorian)) {
            updateCalendarGregorian(events);
        } else if (dateSystem.equals(DateSystem.Persian)) {
            updateCalendarPersian(events);
        } else if (dateSystem.equals(DateSystem.Arabic)) {
            updateCalendarArabic(events);
        }
    }

    private void updateCalendarArabic(HashSet<Date> events) {
    }

    private void updateCalendarPersian(HashSet<Date> events) {

        //Set persian day names


        CalendarTool calendarTool = new CalendarTool();

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.add(Calendar.DATE, 0 - calendarTool.getIranianDay());
        CalendarTool firstDayOfMonth = new CalendarTool(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));

        //int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int monthBeginningCell = (calendar.get(Calendar.DAY_OF_WEEK) + 1) % 7;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DATE, 1 - monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        //SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        calendarTool = currentMonthPersian;
        txtDate.setText(calendarTool.getPersianMonthName(calendarTool.getIranianMonth() - 1) + " " + calendarTool.getIranianYear());
        if (fontSize.equals(FontSize.Large)) {
            txtDate.setTextAppearance(getContext(), R.style.FontSizeLarge);
        } else if (fontSize.equals(FontSize.Medium)) {
            txtDate.setTextAppearance(getContext(), R.style.FontSizeMedium);
        } else {
            txtDate.setTextAppearance(getContext(), R.style.FontSizeSmall);
        }
        txtDate.setTypeface(typefaceFarsi, Typeface.BOLD);

        // set header color according to current season
        int month = calendarTool.getIranianMonth() - 1;
        int season = monthSeasonPersian[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }

    private void updateCalendarGregorian(HashSet<Date> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DATE, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));
        if (fontSize.equals(FontSize.Large)) {
            txtDate.setTextAppearance(getContext(), R.style.FontSizeLarge);
        } else if (fontSize.equals(FontSize.Medium)) {
            txtDate.setTextAppearance(getContext(), R.style.FontSizeMedium);
        } else {
            txtDate.setTextAppearance(getContext(), R.style.FontSizeSmall);
        }
        txtDate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeasonGregorian[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * Choose the type of calendar from DateSystem class
     *
     * @param dateSystem
     */
    public void setDateSystem(DateSystem dateSystem) {
        if (dateSystem != null) {
            this.dateSystem = dateSystem;
        }

        updateCalendar();
    }

    public void setTypefaceFarsi(Typeface typefaceFarsi) {
        this.typefaceFarsi = typefaceFarsi;
        txtDate.setTypeface(typefaceFarsi, Typeface.BOLD);
        setDaysTypeface();
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private HashSet<Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (CalendarView.this.dateSystem == DateSystem.Gregorian) {
                return getViewGregorian(position, view, parent);
            } else if (CalendarView.this.dateSystem == DateSystem.Persian) {
                return getViewPersian(position, view, parent);
            } else if (CalendarView.this.dateSystem == DateSystem.Arabic) {
                return getViewArabic(position, view, parent);
            }
            return view;

        }


        public View getViewPersian(int position, View view, ViewGroup parent) {

            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(date);

            CalendarTool calendarTool = new CalendarTool(1900 + year, month + 1, day);
            CalendarTool today = new CalendarTool();

            // today
            //Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);
            view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, grid.getHeight() / 6));

            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            if (fontSize.equals(FontSize.Large)) {
                ((TextView) view).setTextAppearance(getContext(), R.style.FontSizeLarge);
            } else if (fontSize.equals(FontSize.Medium)) {
                ((TextView) view).setTextAppearance(getContext(), R.style.FontSizeMedium);
            } else {
                ((TextView) view).setTextAppearance(getContext(), R.style.FontSizeSmall);
            }
            ((TextView) view).setTypeface(typefaceFarsi, Typeface.NORMAL);
            ((TextView) view).setTextColor(Color.BLACK);

            if (calendarTool.getIranianMonth() != currentMonthPersian.getIranianMonth() || calendarTool.getIranianYear() != currentMonthPersian.getIranianYear()) {
                // if this day is outside current month, grey it out
                ((TextView) view).setTextColor(getResources().getColor(R.color.greyed_out));
            } else if (calendarTool.getIranianDay() == today.getIranianDay() && calendarTool.getIranianMonth() == today.getIranianMonth() && calendarTool.getIranianYear() == today.getIranianYear()) {
                // if it is today, set it to blue/bold
                ((TextView) view).setTypeface(typefaceFarsi, Typeface.BOLD);
                ((TextView) view).setTextColor(getResources().getColor(R.color.today));
            }
            if (date.equals(selectedDate)) {
                ((TextView) view).setBackground(getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp));
            }

            // set text
            ((TextView) view).setText(String.valueOf(calendarTool.getIranianDay()));

            return view;

        }

        public View getViewGregorian(int position, View view, ViewGroup parent) {

            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(date);

            // today
            Date today = new Date();
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(today);

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);
            view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, grid.getHeight() / 6));

            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            if (fontSize.equals(FontSize.Large)) {
                ((TextView) view).setTextAppearance(getContext(), R.style.FontSizeLarge);
            } else if (fontSize.equals(FontSize.Medium)) {
                ((TextView) view).setTextAppearance(getContext(), R.style.FontSizeMedium);
            } else {
                ((TextView) view).setTextAppearance(getContext(), R.style.FontSizeSmall);
            }
            ((TextView) view).setTypeface(null, Typeface.NORMAL);
            ((TextView) view).setTextColor(Color.BLACK);

            if (dateCal.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH) || dateCal.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR)) {
                // if this day is outside current month, grey it out
                ((TextView) view).setTextColor(getResources().getColor(R.color.greyed_out));
            } else if (dateCal.get(Calendar.DATE) == today.getDate() && dateCal.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH) && dateCal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR)) {
                // if it is today, set it to blue/bold
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) view).setTextColor(getResources().getColor(R.color.today));
            }
            if (date.equals(selectedDate)) {
                ((TextView) view).setBackground(getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp));
            }
            // set text
            ((TextView) view).setText(String.valueOf(date.getDate()));

            return view;

        }

        public View getViewArabic(int position, View view, ViewGroup parent) {

            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            ((TextView) view).setTypeface(null, Typeface.NORMAL);
            ((TextView) view).setTextColor(Color.BLACK);

            if (month != today.getMonth() || year != today.getYear()) {
                // if this day is outside current month, grey it out
                ((TextView) view).setTextColor(getResources().getColor(R.color.greyed_out));
            } else if (day == today.getDate()) {
                // if it is today, set it to blue/bold
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) view).setTextColor(getResources().getColor(R.color.today));
            }

            // set text
            ((TextView) view).setText(String.valueOf(date.getDate()));

            return view;

        }

    }

    public void setSize(int width, int height) {
        this.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayLongPress(Date date);

        void onDayPress(Date date);
    }
}

