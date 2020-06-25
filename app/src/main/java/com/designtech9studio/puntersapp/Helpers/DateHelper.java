package com.designtech9studio.puntersapp.Helpers;

import androidx.annotation.NonNull;

public class DateHelper {

    String dayOfMonth,dayOfWeek, monthName;
    int color;


    public DateHelper(String dayOfMonth, String dayOfWeek, String monthName) {
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.monthName = monthName;
        color =0;
    }

    public int getColor() {
        return color;
    }

    public void resetColor() {
        color = 0;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return  dayOfWeek  + " \n\n" + dayOfMonth + " \n\n" + monthName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
