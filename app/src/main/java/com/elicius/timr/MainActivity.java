package com.elicius.timr;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SQLHandler sqlHandler;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                createNewTimer();
            }
        });

        sqlHandler = new SQLHandler(this);
        Timer[] dataset = sqlHandler.selectAll();

        //Timer[] dataset = new Timer[1];
        //dataset[0] = new Timer(5, 0, 0);

        recyclerView = findViewById(R.id.timer_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new MyAdapter(dataset, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sqlHandler.close();
    }

    //TODO: Menü füllen
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

    private void createNewTimer() {
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.new_timer, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(layout);
        AlertDialog dialog = builder.create();


        Spinner second = layout.findViewById(R.id.new_second);
        Spinner minute = layout.findViewById(R.id.new_minute);
        Spinner hour = layout.findViewById(R.id.new_hour);
        second.setContentDescription(getString(R.string.seconds));
        minute.setContentDescription(getString(R.string.minutes));
        hour.setContentDescription(getString(R.string.hours));

        ArrayList<View> seconds = new ArrayList<View>();
        for (int i = 0; i <= 59; i++) {
            View view = createSelect_View(i, inflater);
            seconds.add(view);
        }
        ArrayList<View> minutes = new ArrayList<View>();
        for (int i = 0; i <= 59; i++) {
            View view = createSelect_View(i, inflater);
            minutes.add(view);
        }
        ArrayList<View> hours = new ArrayList<View>();
        for (int i = 0; i <= 100; i++) {
            View view = createSelect_View(i, inflater);
            hours.add(view);
        }
        ArrayAdapter aas = new ArrayAdapter(this, R.layout.spinner_dropdown_resource, seconds);
        ArrayAdapter aam = new ArrayAdapter(this, R.layout.spinner_dropdown_resource, minutes);
        ArrayAdapter aah = new ArrayAdapter(this, R.layout.spinner_dropdown_resource, hours);
        second.setAdapter(aas);
        minute.addChildrenForAccessibility(minutes);
        hour.addChildrenForAccessibility(hours);
        dialog.show();
    }

    private View createSelect_View(int i, LayoutInflater inflater) {
        View spinnerView = inflater.inflate(R.layout.select_view, null);
        TextView textView = spinnerView.findViewById(R.id.text_view);
        textView.setText("" + i);
        return spinnerView;
    }
}