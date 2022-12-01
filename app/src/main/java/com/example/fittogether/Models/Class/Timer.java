package com.example.fittogether.Models.Class;

public class Timer {
    private int hour;
    private int minute;
    private int second;

    public Timer(String str) {
        //hh:mm:ss:ii
        // ex 00:01:30:00 -> 1 min 30 sec
        String[] arr = str.split(":");

        this.hour = Integer.parseInt(arr[0]);
        this.minute = Integer.parseInt(arr[1]);
        this.second = Integer.parseInt(arr[2]);
    }

    public Timer() {
        this(0, 0, 0);
    }

    public Timer(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
