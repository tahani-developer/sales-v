package com.dr7.salesmanmanager.Modles;

import androidx.appcompat.app.AppCompatActivity;

public class NoteSalesPlane extends AppCompatActivity {

    private String serial;
    private  String planeSerial;
    private String noteStart;
    private String noteEnd;
    private String editStart;

    private String editEnd;
    private String date;
    private String customerId;

    public NoteSalesPlane(){
    }

    public NoteSalesPlane(String serial, String planeSerial, String noteStart, String noteEnd, String editStart, String editEnd, String date, String customerId) {
        this.serial = serial;
        this.planeSerial = planeSerial;
        this.noteStart = noteStart;
        this.noteEnd = noteEnd;
        this.editStart = editStart;
        this.editEnd = editEnd;
        this.date = date;
        this.customerId = customerId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPlaneSerial() {
        return planeSerial;
    }

    public void setPlaneSerial(String planeSerial) {
        this.planeSerial = planeSerial;
    }

    public String getNoteStart() {
        return noteStart;
    }

    public void setNoteStart(String noteStart) {
        this.noteStart = noteStart;
    }

    public String getNoteEnd() {
        return noteEnd;
    }

    public void setNoteEnd(String noteEnd) {
        this.noteEnd = noteEnd;
    }

    public String getEditStart() {
        return editStart;
    }

    public void setEditStart(String editStart) {
        this.editStart = editStart;
    }

    public String getEditEnd() {
        return editEnd;
    }

    public void setEditEnd(String editEnd) {
        this.editEnd = editEnd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}



