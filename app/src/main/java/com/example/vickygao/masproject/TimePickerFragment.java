package com.example.vickygao.masproject;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.app.DialogFragment;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Log.w("DatePicker", hourOfDay + ":" + minute);
        ((TextView) getActivity().findViewById(R.id.newDelTime)).setText(hourOfDay + ":" + minute);
    }
}