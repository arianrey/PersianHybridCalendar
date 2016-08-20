package com.hybriddevs.persianhybridcalendar.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.hybriddevs.persianhybridcalendar.R;

import java.util.Date;
import java.util.HashSet;


/**
 * Created by arian on 8/7/2016.
 */
public abstract class CalendarViewDialog extends AlertDialog implements DialogInterface.OnClickListener{

    protected CalendarView mCalendarView;
    protected HashSet<Date> selectedDates;
    protected Date selectedDate;

    public CalendarViewDialog(Context context, CalendarView.DateSystem dateSystem, CalendarView.FontSize fontSize, boolean multiSelect, Typeface farsiTypeface) {
        super(context);

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.calendar_view_dialog, null);


        selectedDates = new HashSet<>();
        selectedDate = new Date();
        selectedDate.setSeconds(0);
        selectedDate.setHours(0);
        selectedDate.setMinutes(0);

        mCalendarView = (CalendarView) view.findViewById(R.id.calendarViewDialog);
        mCalendarView.setDateSystem(dateSystem);
        mCalendarView.setFontSize(fontSize);
        mCalendarView.setMultiSelect(multiSelect);
        mCalendarView.setTypefaceFarsi(farsiTypeface);
        mCalendarView.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public boolean onDayLongPress(Date date) {
                return false;
            }

            @Override
            public void onDayPress(Date date) {
                selectedDate = date;
            }
        });

        setButton(BUTTON_POSITIVE, "OK", this);
        setButton(BUTTON_NEGATIVE, "cancel", this);

        setView(view);

    }

    public CalendarViewDialog(Context context, CalendarView.DateSystem dateSystem, CalendarView.FontSize fontSize, boolean multiSelect, Typeface farsiTypeface, HashSet<Date> selectedDates) {
        this(context, dateSystem, fontSize, multiSelect, farsiTypeface);
        mCalendarView.setDefaultSelectedDates(selectedDates);
    }

    public CalendarViewDialog(Context context, CalendarView.DateSystem dateSystem, CalendarView.FontSize fontSize, boolean multiSelect, Typeface farsiTypeface,int theme) {
        this(context, dateSystem, fontSize, multiSelect, farsiTypeface);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                onClickOK();
                break;
            case BUTTON_NEGATIVE:
                break;
        }
    }

    public abstract void onClickOK();

    public Date getSelectedDate() {
        return selectedDate;
    }

    public HashSet<Date> getSelectedDates() {
        return mCalendarView.getSelectedDates();
    }
}
