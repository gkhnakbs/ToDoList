package com.example.todolist;

public class Data {

    public String email;
    public String password;
    public String planTitle;
    public String planText;
    public  int start_day;
    public int start_year;
    public int start_month;
    public int start_clock;
    public int start_minute;

    public String id;
    public boolean checkBox;


    public Data( String planTitle, String planText, int start_day, int start_year, int start_month, int start_clock, int start_minute,String id, Boolean checkBox) {


        this.planTitle = planTitle;
        this.planText = planText;
        this.start_day = start_day;
        this.start_year = start_year;
        this.start_month = start_month;
        this.start_clock = start_clock;
        this.start_minute = start_minute;
        this.id=id;
        this.checkBox=checkBox;

    }



}
