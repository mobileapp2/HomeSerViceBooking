package com.designtech9studio.puntersapp.Helpers;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateGenerator {

    public static List<DateHelper> generateDate() {
        List<DateHelper> list = new ArrayList<>();
        for (int i=0;i<12;i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day          = (String) DateFormat.format("dd",   date); // 20
            String monthString  = (String) DateFormat.format("MMM",  date); // Jun

            DateHelper dateHelper = new DateHelper(day, dayOfTheWeek, monthString);
            list.add(dateHelper);
        }

        return list;
    }

    public static List<DateHelper> generateDateWithMonthNumber() {
        List<DateHelper> list = new ArrayList<>();
        for (int i=0;i<7;i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day          = (String) DateFormat.format("dd",   date); // 20
            String monthString  = (String) DateFormat.format("MM",  date); // Jun

            DateHelper dateHelper = new DateHelper(day, dayOfTheWeek, monthString);
            list.add(dateHelper);
        }

        return list;
    }


    public static String getDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, i);
        Date date = calendar.getTime();
        String dayOfTheWeek = (String) DateFormat.format("dd-MM-yy", date);
        return dayOfTheWeek;
    }

    public static Date convertStringToDate(String d) {
        try{
            Date date1=new SimpleDateFormat("dd-MM-yy").parse(d);
            return date1;
        }catch (Exception e ){

        }

        return null;


    }

}
