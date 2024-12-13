package com.example.demo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Horário {
    String fileLocation;
    String date;
    String quality;

    Horário(String location){
        fileLocation = location;
        this.date=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    Horário(String location, String date){
        fileLocation = location;
        this.date=date;
    }




}
