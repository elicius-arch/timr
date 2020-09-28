package com.elicius.timr;

public class Timer {
    private int seconds;
    private int minutes;
    private int hours;

    public Timer(int seconds, int minutes, int hours) {
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public boolean equals(Timer timer) {
        return timer.seconds == seconds && timer.minutes == minutes && timer.hours == hours;
    }

    public String toString() {
        return hours + " : " + minutes + " : " + seconds;
    }
}
