package com.reactiverobot.latecounter;

import java.util.Date;

import io.realm.RealmObject;

public class CounterDailyRecord extends RealmObject {

    private String counterType;
    private Date date;
    private int count;

    public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
