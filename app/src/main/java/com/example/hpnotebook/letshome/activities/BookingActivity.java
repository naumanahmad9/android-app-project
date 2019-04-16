package com.example.hpnotebook.letshome.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpnotebook.letshome.R;

import java.util.Calendar;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity {


    private TextView mDisplayDate, tvArrivalDate;
    private EditText etTotalDays;
    private String numberofdays, arrivaldate;
    private boolean check;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        etTotalDays = (EditText) findViewById(R.id.etTotalDays);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        tvArrivalDate = (TextView) findViewById(R.id.tvArrivalDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BookingActivity.this,
                        android.R.style.Theme_Material_Dialog,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#610049")));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;

                String date = day + "/" + month + "/" + year;

                tvArrivalDate.setText(date);
            }
        };

    }

    public void continueBooking(View view) {

        numberofdays = etTotalDays.getText().toString();
        arrivaldate = tvArrivalDate.getText().toString();

        if(Objects.equals(arrivaldate, "")){

            Toast.makeText(BookingActivity.this, "Enter the arrival date", Toast.LENGTH_LONG).show();
            check = true;
        }
        if(numberofdays.isEmpty()){

            etTotalDays.setError("Enter Number of Days");
            check = true;
        }
        if(!check){

            Intent mIntent = new Intent(BookingActivity.this, FinalBookingActivity.class);

            Bundle bundle = new Bundle();

            bundle.putString(arrivaldate, "arrivalDate");
            bundle.putString(numberofdays, "numberOfDays");

            mIntent.putExtras(bundle);

            startActivity(mIntent);
        }
    }
}

