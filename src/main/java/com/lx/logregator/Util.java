package com.lx.logregator;

public class Util {
    public static String padZero(int i) {
        if(i <= 9) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

    public static String formatTime(long ms, boolean exactTime) {
        int seconds = (int)(ms / 1000);
        int minutes = (int)Math.round(seconds / 60.0);
        if(seconds < 60) {
            return seconds + "s";
        } else {
            if(exactTime) {
                return minutes + " min " + seconds % 60 + " sec";
            } else {
                return minutes + " min";
            }
        }
    }
    public static String getTimeDuration(long n1, long n2) {
        long difference = Math.abs(n1 - n2);
        return formatTime(difference, false);
    }
}
