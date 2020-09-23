package com.elicius.timr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    protected static final String MINUTES = "minutes";
    protected static final String VALUES = "values";
    protected static final String SECONDS = "seconds";
    protected static final String HOURS = "hours";
    protected static final String OK = "ok";

    private int minutes;
    private int seconds;
    private int hours;

    private TimerThread timerThread;

    private TextView seconds1;
    private TextView seconds2;
    private TextView minutes1;
    private TextView minutes2;
    private TextView hours1;
    private TextView hours2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timr);

        try {
            Bundle values = getIntent().getBundleExtra(VALUES);
            if (values == null)
                throw new NullPointerException();
            minutes = values.getInt(MINUTES);
            seconds = values.getInt(SECONDS);
            hours = values.getInt(HOURS);
        } catch (NullPointerException e) {
            createErrorDialog(getString(R.string.values_not_found));
        }
        try {
            setTextViews();
            drawClock(seconds, minutes, hours);
        } catch (NullPointerException e) {
            createErrorDialog(getString(R.string.textviews_not_found));
        }

        TimerResultReceiver receiver = new TimerResultReceiver(new Handler());

        TimrIntentService.startTimer(this, new Timer(seconds, minutes, hours), receiver);
    }

    void drawClock(int seconds, int minutes, int hours) {
        //TODO: Ressourcestrings with placeholders
        hours1.setText("" + (hours / 10));
        hours2.setText("" + (hours % 10));
        minutes1.setText("" + (minutes / 10));
        minutes2.setText("" + (minutes % 10));
        seconds1.setText("" + (seconds / 10));
        seconds2.setText("" + (seconds % 10));
    }

    private void setTextViews() throws NullPointerException {
        hours1 = findTextView(R.id.firstnumber);
        hours2 = findTextView(R.id.secondnumber);
        minutes1 = findTextView(R.id.thirdnumber);
        minutes2 = findTextView(R.id.fourthnumber);
        seconds1 = findTextView(R.id.fifthnumber);
        seconds2 = findTextView(R.id.sixthnumber);
    }

    private TextView findTextView(int number) throws NullPointerException {
        TextView textView = ((TextView) ((ConstraintLayout) findViewById(number)).getViewById(R.id.textView));
        if (textView == null)
            throw new NullPointerException();
        else
            return textView;
    }

    private void createErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimerActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    protected class TimerResultReceiver extends ResultReceiver {

        protected static final int RUNNING = 1;

        public TimerResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == RUNNING) {
                if (resultData == null)
                    throw new NullPointerException();//TODO: Abfangen - aber wo?
                int seconds = resultData.getInt(SECONDS);
                int minutes = resultData.getInt(MINUTES);
                int hours = resultData.getInt(HOURS);
                drawClock(seconds, minutes, hours);
            }
        }
    }
}