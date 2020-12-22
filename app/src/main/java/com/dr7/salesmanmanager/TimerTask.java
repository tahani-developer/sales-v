package com.dr7.salesmanmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.Timer;

public class TimerTask {
    public  Timer timer;
    requestAdmin request;
    Context ma_context;
    private static TimerTask _instance;

    public TimerTask(Context main_context) {
        this.ma_context=main_context;
        request=new requestAdmin(ma_context);
    }

    public TimerTask() {
    }

    public  void  startTimer(){
        timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {

                request.checkRequestState();


            }

        }, 0, 5000);
    }
    public  void  stopTimer(){

        if(timer!=null)
        {
            timer.purge();
            timer.cancel();
            timer=null;
        }

    }


    public synchronized static TimerTask getInstance() {
        if (_instance == null) {
            _instance = new TimerTask();
        }
        return _instance;
    }
};

