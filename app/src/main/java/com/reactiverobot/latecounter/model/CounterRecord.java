package com.reactiverobot.latecounter.model;

import java.util.Date;

import io.realm.RealmObject;

public class CounterRecord extends RealmObject {

    private Date date;
    private int count;
    private CounterType counterType;

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

    public CounterType getCounterType() {
        return counterType;
    }

    public void setCounterType(CounterType counterType) {
        this.counterType = counterType;
    }
}
