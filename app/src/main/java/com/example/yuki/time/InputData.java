package com.example.yuki.time;

import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Calendar;
import java.util.Date;
import android.text.format.DateFormat;

/**
 * Created by Yuki on 2016/04/05.
 */
public class InputData {
    static int monthDays[][] = {{31,28,31,30,31,30,31,31,30,31,30,31},
                                {31,29,31,30,31,30,31,31,30,31,30,31}};

    private int inputYear;
    private int inputMonth;
    private int inputDay;
    private int inputHour;
    private int inputMinute;
    private int num;

    private String title;
    private String memo;

    public int getInputYear() {
        return inputYear;
    }

    public void setInputYear(int inputYear) {
        this.inputYear = inputYear;
    }

    public int getInputMonth() {
        return inputMonth;
    }

    public void setInputMonth(int inputMonth) {
        this.inputMonth = inputMonth;
    }

    public int getInputDay() {
        return inputDay;
    }

    public void setInputDay(int inputDay) {
        this.inputDay = inputDay;
    }

    public int getInputHour() {
        return inputHour;
    }

    public void setInputHour(int inputHour) {
        this.inputHour = inputHour;
    }

    public int getInputMinute() {
        return inputMinute;
    }

    public void setInputMinute(int inputMinute) {
        this.inputMinute = inputMinute;
    }

    public void set(int year, int month, int day, int hour, int minute) {
        inputYear = year;
        inputMonth = month;
        inputDay = day;
        inputHour = hour;
        inputMinute = minute;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public String getDateTimeString(){
        if (inputHour < 12) {
            return String.format("%04d年%d月%02d日 午前 %d時%02d分", inputYear, inputMonth, inputDay, inputHour, inputMinute);

        } else {
            return String.format("%04d年%d月%02d日 午後 %d時%02d分", inputYear, inputMonth, inputDay, inputHour-12, inputMinute);

        }
    }

    public String getCountDownString(){

        //int countMonth[][] = new int[2][12];


        Calendar calendar = Calendar.getInstance();

        int flag = 0;

        int minYear = calendar.get(Calendar.YEAR);
        int minMonth = calendar.get(Calendar.MONTH) + 1;
        int minDay = calendar.get(Calendar.DATE);
        int minHour = calendar.get(Calendar.HOUR_OF_DAY);
        int minMinute = calendar.get(Calendar.MINUTE);
        int minSecond = calendar.get(Calendar.SECOND);

        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH) + 1;
        int maxDay = calendar.get(Calendar.DATE);
        int maxHour = calendar.get(Calendar.HOUR_OF_DAY);
        int maxMinute = calendar.get(Calendar.MINUTE);
        int maxSecond = calendar.get(Calendar.SECOND);

        int outputYear = 0;
        int outputMonth = 0;
        //int outputDayAll = 0;
        int outputDay = 0;
        int outputHour = 0;
        int outputMinute = 0;
        int outputSecond = 0;

        //int roundYear = maxYear - minYear;
        //int roundMonth = minMonth+1;

        flag = timeDecision(inputYear,inputMonth,inputDay,inputHour,inputMinute);

        if (flag == 0){
            minYear = inputYear;
            minDay = inputDay;
            minMonth = inputMonth;
            minHour = inputHour;
            minMinute = inputMinute;
            minSecond = 0;
        }else if (flag == 1){
            maxYear = inputYear;
            maxDay = inputDay;
            maxMonth = inputMonth;
            maxHour = inputHour;
            maxMinute = inputMinute;
            maxSecond = 0;
        }


        outputYear = maxYear - minYear;
        outputMonth = maxMonth - minMonth;
        outputDay = maxDay - minDay;
        outputHour = maxHour - minHour;
        outputMinute = maxMinute - minMinute;
        outputSecond = maxSecond - minSecond;


        if (outputSecond <= -1){
            outputMinute--;
            outputSecond+=60;
        }

        if (outputMinute <=-1){
            outputHour--;
            outputMinute+=60;
        }

        if (outputHour <= -1){
            outputDay--;
            outputHour+=24;
        }

        if (outputDay <= -1){

            outputMonth--;
            if(minYear % 400 == 0 || (minYear % 4 == 0 && minYear % 100 != 0)){
                outputDay+=monthDays[1][minMonth-1];
            }else {
                outputDay+=monthDays[0][minMonth-1];
            }
        }

        if (outputMonth <= -1){
            outputYear--;
            outputMonth+=12;
        }

/*
        for (minYear=minYear; minYear <= maxYear; minYear++){
            if (minYear % 400 == 0 || (minYear % 4 == 0 && minYear % 100 != 0)){
                if (minYear == maxYear){
                    while (roundMonth < maxMonth){
                        countMonth[1][roundMonth-1]++;
                        roundMonth++;
                    }
                }else if (roundMonth != 0){
                    while (roundMonth <= 12){
                        countMonth[1][roundMonth-1]++;
                        roundMonth++;
                    }
                }else {
                    while (roundMonth <= 12){
                        countMonth[1][roundMonth-1]++;
                        roundMonth++;
                    }
                }
                roundMonth = 0;
            }else {
                if (minYear == maxYear){
                    while (roundMonth < maxMonth){
                        countMonth[0][roundMonth-1]++;
                        roundMonth++;
                    }
                }else if (roundMonth != 0){
                    while (roundMonth <= 12){
                        countMonth[0][roundMonth-1]++;
                        roundMonth++;
                    }
                }else {
                    while (roundMonth <= 12){
                        countMonth[0][roundMonth-1]+=1;
                        roundMonth++;
                    }
                }
                roundMonth = 0;
            }
        }

        for(int round = 0; round < 12; round++){
            outputDayAll += countMonth[0][round] * monthDays[0][round];
            outputDayAll += countMonth[1][round] * monthDays[1][round];
        }
        outputDayAll += outputDay;
        */

        if (flag == 0){
            if (outputYear != 0){
                return String.format("%d年 %dヶ月 %d日 %d時間 %d分 %d秒\n経過",outputYear,outputMonth,outputDay,outputHour,outputMinute,outputSecond);
            }else if (outputMonth != 0){
                return String.format("%dヶ月 %d日 %d時間 %d分 %d秒\n経過",outputMonth,outputDay,outputHour,outputMinute,outputSecond);
            }else if (outputDay != 0){
                return String.format("%d日 %d時間 %d分 %d秒\n経過",outputDay,outputHour,outputMinute,outputSecond);
            }else if (outputHour != 0){
                return String.format("%d時間 %d分 %d秒\n経過",outputHour,outputMinute,outputSecond);
            } else if (outputMinute != 0){
                return String.format("%d分 %d秒\n経過",outputMinute,outputSecond);
            }else {
                return String.format("%d秒\n経過",outputSecond);
            }
        }else if (flag == 1){
            if (outputYear != 0){
                return String.format("残り\n%d年 %dヶ月 %d日 %d時間 %d分 %d秒",outputYear,outputMonth,outputDay,outputHour,outputMinute,outputSecond);
            }else if (outputMonth != 0){
                return String.format("残り\n%dヶ月 %d日 %d時間 %d分 %d秒",outputMonth,outputDay,outputHour,outputMinute,outputSecond);
            }else if (outputDay != 0){
                return String.format("残り\n%d日 %d時間 %d分 %d秒",outputDay,outputHour,outputMinute,outputSecond);
            }else if (outputHour != 0){
                return String.format("残り\n%d時間 %d分 %d秒",outputHour,outputMinute,outputSecond);
            } else if (outputMinute != 0){
                return String.format("残り\n%d分 %d秒",outputMinute,outputSecond);
            }else {
                return String.format("残り\n%d秒",outputSecond);
            }
        }else {
            return String.format("設定時刻です");
        }

/*
        if (flag == 2){
            if (outputDayAll != 0){
                return String.format("残り\n%d日 %d時間 %d分",outputDayAll,outputHour,outputMinute);
            }else if (outputHour != 0){
                return String.format("残り\n%d時間 %d分",outputHour,outputMinute);
            } else {
                return String.format("残り\n%d分",outputMinute);
            }
        }else if (flag == 1){
            if (outputDayAll != 0){
                return String.format("%d日 %d時間 %d分\n経過",outputDayAll,outputHour,outputMinute);
            }else if (outputHour != 0){
                return String.format("%d時間 %d分\n経過",outputHour,outputMinute);
            } else {
                return String.format("%d分\n経過",outputMinute);
            }
        }else {
            return String.format("設定時刻です");
        }

        */
        //return String.format("%04d年%d月%02d日 %d時%02d分\n%04d年%d月%02d日 %d時%02d分", inputYear, inputMonth, inputDay, inputHour, inputMinute, nowYear, nowMonth, nowDay, nowHour, nowMinute);


    }



    public int timeDecision (int year, int month, int day, int hour, int minute){

        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DATE);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);

        if (nowYear < year){
            return  1;
        }else if (nowYear > year){
            return 0;
        }else if (nowMonth < month){
            return 1;
        }else if (nowMonth > month){
            return 0;
        }else if (nowDay < day){
            return 1;
        }else if (nowDay > day){
            return 0;
        }else if (nowHour < hour){
            return 1;
        }else if (nowHour > hour){
            return 0;
        }else if (nowMinute < minute){
            return 1;
        }else if (nowMinute > minute){
            return 0;
        }else {
            return 2;
        }

    }


}
