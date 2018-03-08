package com.draxvel.simpleblog.util;

import android.text.format.DateFormat;
import java.util.Date;

public class DateTimeConverter {
    public static String DateToString(Date date){
        long milisec = date.getTime();
        return DateFormat.format("dd/MM/yyyy", new Date(milisec)).toString();
    }
}
