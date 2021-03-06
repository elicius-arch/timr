package com.elicius.timr;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private SQLHandler sqlHandler;

    private List<Timer> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.new_timer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTimer();
            }
        });

        sqlHandler = new SQLHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO: auf Sinnhaftigkeit überprüfen
        sqlHandler.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDBAndInitializeRecyclerView();
    }

    private void selectDBAndInitializeRecyclerView() {
        dataset = sqlHandler.selectAll();

        recyclerView = findViewById(R.id.timer_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new MyAdapter(dataset, this);
        recyclerView.setAdapter(adapter);
    }

    private void createNewTimer() {
        LayoutInflater inflater = getLayoutInflater();
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.new_timer, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(layout);
        final AlertDialog dialog = builder.create();


        final Spinner second = layout.findViewById(R.id.new_second);
        final Spinner minute = layout.findViewById(R.id.new_minute);
        final Spinner hour = layout.findViewById(R.id.new_hour);
        second.setContentDescription(getString(R.string.seconds));
        minute.setContentDescription(getString(R.string.minutes));
        hour.setContentDescription(getString(R.string.hours));
        second.setGravity(Gravity.CENTER);
        minute.setGravity(Gravity.CENTER);
        hour.setGravity(Gravity.CENTER);

        ArrayList<Integer> seconds = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            seconds.add(i);
        }
        ArrayList<Integer> minutes = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minutes.add(i);
        }
        ArrayList<Integer> hours = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            hours.add(i);
        }
        ArrayAdapter aas = new ArrayAdapter(this, R.layout.spinner_dropdown_resource, seconds);
        ArrayAdapter aam = new ArrayAdapter(this, R.layout.spinner_dropdown_resource, minutes);
        ArrayAdapter aah = new ArrayAdapter(this, R.layout.spinner_dropdown_resource, hours);
        second.setAdapter(aas);
        minute.setAdapter(aam);
        hour.setAdapter(aah);

        Button okButton = layout.findViewById(R.id.button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                int s = (int) second.getSelectedItem();
                int m = (int) minute.getSelectedItem();
                int h = (int) hour.getSelectedItem();

                if (s == 0 && m == 0 && h == 0) {
                    TimerActivity.createErrorDialog(MainActivity.this.getString(R.string.null_timer), MainActivity.this);
                } else {
                    Timer timer = new Timer(s, m, h);

                    if (dataset == null) {
                        dataset = new ArrayList<>();
                    } else {
                        for (Timer t : dataset) {
                            if (t.equals(timer)) {
                                TimerActivity.createErrorDialog(getString(R.string.times_exists, timer.toString()), MainActivity.this);
                                return;
                            }
                        }
                    }

                    dataset.add(timer);
                    Collections.sort(dataset);
                    sqlHandler.insertOne(timer);
                    adapter = new MyAdapter(dataset, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    Toast toast = Toast.makeText(MainActivity.this, getString(R.string.new_timr_created), Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public SQLHandler getSQLHandler() {
        return sqlHandler;
    }
}