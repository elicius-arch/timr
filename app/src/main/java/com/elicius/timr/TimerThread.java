package com.elicius.timr;

import android.os.Bundle;
import com.elicius.timr.TimerActivity.TimerResultReceiver;

import static com.elicius.timr.TimerActivity.TimerResultReceiver.RUNNING;

public class TimerThread extends Thread {

    private int second;
    private int minute;
    private int hour;
    private TimerResultReceiver receiver;

    public TimerThread(int second, int minute, int hour, TimerResultReceiver receiver){
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        while ((second != 0) && (minute != 0) && (hour != 0)) {
            if (second != -1) {
                second--;
            } else {
                if (minute != -1) {
                    minute--;
                    second = 59;
                } else {
                    if (hour != 0) {
                        hour--;
                        minute = 59;
                    }
                }
            }

            Bundle bundle = new Bundle();
            bundle.putInt(TimerActivity.SECONDS, second);
            bundle.putInt(TimerActivity.MINUTES, minute);
            bundle.putInt(TimerActivity.HOURS, hour);

            receiver.send(RUNNING, bundle);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
