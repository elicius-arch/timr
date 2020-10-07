package com.elicius.timr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimerActivity extends AppCompatActivity {
    protected static final String MINUTES = "minutes";
    protected static final String VALUES = "values";
    protected static final String SECONDS = "seconds";
    protected static final String HOURS = "hours";
    protected static final String OK = "ok";

    private int minutes;
    private int seconds;
    private int hours;

    private int actualSeconds;
    private int actualMinutes;
    private int actualHours;

    private TimerResultReceiver receiver;

    private TextView seconds1;
    private TextView seconds2;
    private TextView minutes1;
    private TextView minutes2;
    private TextView hours1;
    private TextView hours2;

    private FloatingActionButton play;
    private FloatingActionButton stop;
    private FloatingActionButton end;
    private FloatingActionButton reset;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timr);

        receiver = new TimerResultReceiver(new Handler());

        try {
            Bundle values = getIntent().getBundleExtra(VALUES);
            if (values == null)
                throw new NullPointerException();
            minutes = actualMinutes = values.getInt(MINUTES);
            seconds = actualSeconds = values.getInt(SECONDS);
            hours = actualHours = values.getInt(HOURS);
        } catch (NullPointerException e) {
            createErrorDialog(getString(R.string.values_not_found), this);
        }
        try {
            setTextViews();
            drawClock(seconds, minutes, hours);
        } catch (NullPointerException e) {
            createErrorDialog(getString(R.string.textview_not_found), this);
        }

        try {
            setButtons();
            setListeners();
        } catch (NullPointerException e) {
            createErrorDialog(getString(R.string.button_not_found), this);
        }
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

    private void setActualTime(int seconds, int minutes, int hours) {
        actualSeconds = seconds;
        actualMinutes = minutes;
        actualHours = hours;
    }

    private void setTextViews() throws NullPointerException {
        hours1 = findTextView(R.id.firstnumber);
        hours2 = findTextView(R.id.secondnumber);
        minutes1 = findTextView(R.id.thirdnumber);
        minutes2 = findTextView(R.id.fourthnumber);
        seconds1 = findTextView(R.id.fifthnumber);
        seconds2 = findTextView(R.id.sixthnumber);
    }

    private void setButtons() throws NullPointerException {
        play = findViewById(R.id.button_play);
        stop = findViewById(R.id.button_stop);
        end = findViewById(R.id.button_end);
        reset = findViewById(R.id.button_reset);
        if (play == null || stop == null || end == null || reset == null)
            throw new NullPointerException();
    }

    private void setListeners() {
        play.setOnClickListener(new TimrButtonListener());
        stop.setOnClickListener(new TimrButtonListener());
        end.setOnClickListener(new TimrButtonListener());
        reset.setOnClickListener(new TimrButtonListener());
        //sicherstellen, dass nur play angeklickt werden kann:
        play.setClickable(true);
        stop.setClickable(false);
        end.setClickable(false);
        reset.setClickable(false);
    }

    private TextView findTextView(int number) throws NullPointerException {
        TextView view = (TextView) ((ConstraintLayout) findViewById(number)).getViewById(R.id.textView);
        if (view == null)
            throw new NullPointerException();
        else
            return view;
    }

    private MediaPlayer playRingTone() {
        Uri alarmSound =
                RingtoneManager. getDefaultUri (RingtoneManager.TYPE_ALARM);
        MediaPlayer mp = MediaPlayer. create (getApplicationContext(), alarmSound);
        mp.start();
        return mp;
    }

    private static void executeVibrator (AppCompatActivity activity) {
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }

    public static void createErrorDialog(String message, final AppCompatActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(R.string.error)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (activity.getClass().equals(TimerActivity.class))
                            activity.finish();
                        else
                            dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        executeVibrator(activity);
    }

    private void createFinishingDialog(final MediaPlayer mp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.timer_finished))
                .setTitle(R.string.finished)
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mp.stop();
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        executeVibrator(this);
    }

    protected class TimerResultReceiver extends ResultReceiver {

        protected static final int RUNNING = 1;
        protected static final int FINISHED = 0;

        public TimerResultReceiver(Handler handler) {
            super(handler);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == RUNNING) {
                if (resultData == null)
                    createErrorDialog(getString(R.string.error_unspecific), TimerActivity.this);
                actualSeconds = resultData.getInt(SECONDS);
                actualMinutes = resultData.getInt(MINUTES);
                actualHours = resultData.getInt(HOURS);
                drawClock(actualSeconds, actualMinutes, actualHours);
            }
            if (resultCode == FINISHED) {
                MediaPlayer mp = playRingTone();
                createFinishingDialog(mp);
                TimerActivity.this.setUnclickable(play);
                TimerActivity.this.setUnclickable(stop);
                TimerActivity.this.setUnclickable(end);
                TimerActivity.this.setClickable(reset);
            }
        }
    }

    private class TimrButtonListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            setUnclickable((FloatingActionButton) v);
            if (v.equals(play)) {
                runTimer(actualSeconds, actualMinutes, actualHours, receiver);
                setClickable(stop);
                setClickable(end);
                setUnclickable(reset);
            }
            if (v.equals(stop)) {
                TimrIntentService.stopTimer(getApplicationContext(), receiver);
                setClickable(play);
                setClickable(reset);
                setClickable(end);
            }
            if (v.equals(end)) {
                TimrIntentService.stopTimer(getApplicationContext(), receiver);
                setActualTime(0, 0, 0);
                drawClock(actualSeconds, actualMinutes, actualHours);
                setClickable(reset);
                setUnclickable(play);
                setUnclickable(stop);
            }
            if (v.equals(reset)) {
                setActualTime(seconds, minutes, hours);
                drawClock(seconds, minutes, hours);
                setClickable(play);
                setUnclickable(stop);
                setUnclickable(end);
            }
        }

        private void runTimer(int seconds, int minutes, int hours, TimerResultReceiver receiver) {
            TimrIntentService.startTimer(getApplicationContext(), new Timer(seconds, minutes, hours), receiver);
            stop.setClickable(true);
            end.setClickable(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUnclickable(FloatingActionButton v) {
        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        v.refreshDrawableState();
        v.setClickable(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setClickable(FloatingActionButton v) {
        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        v.refreshDrawableState();
        v.setClickable(true);
    }
}