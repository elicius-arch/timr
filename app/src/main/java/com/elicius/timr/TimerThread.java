package com.elicius.timr;

import android.os.Bundle;

import com.elicius.timr.TimerActivity.TimerResultReceiver;

import static com.elicius.timr.TimerActivity.TimerResultReceiver.RUNNING;
import static com.elicius.timr.TimerActivity.TimerResultReceiver.FINISHED;

public class TimerThread extends Thread {

    private int second;
    private int minute;
    private int hour;
    private TimerResultReceiver receiver;

    private boolean running = false;

    public TimerThread(int second, int minute, int hour, TimerResultReceiver receiver) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.receiver = receiver;
    }

    public TimerThread(Timer timer, TimerResultReceiver receiver) {
        new TimerThread(timer.getSeconds(), timer.getMinutes(), timer.getHours(), receiver);
    }

    @Override
    public void run() {
        running = true;
        while ((second != 0) || (minute != 0) || (hour != 0)) {
            if (!running)
                return;
            if (second != 0) {
                second--;
            } else {
                if (minute != 0) {
                    minute--;
                    second = 59;
                } else {
                    if (hour != 0) {
                        hour--;
                        minute = 59;
                        second = 59;
                    }
                }
            }

            Bundle bundle = new Bundle();
            bundle.putInt(TimerActivity.SECONDS, second);
            bundle.putInt(TimerActivity.MINUTES, minute);
            bundle.putInt(TimerActivity.HOURS, hour);

            receiver.send(RUNNING, bundle);
            if (!(second == 0 && minute == 0 && hour == 0)) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (running) {
            Bundle bundle = new Bundle();
            receiver.send(FINISHED, bundle);
        }

    }

    public void terminate() {
        running = false;
    }

    public Timer getTimer() {
        return new Timer(second, minute, hour);
    }
}
