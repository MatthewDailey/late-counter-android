package com.reactiverobot.latecounter.model;

import java.util.Date;

import io.realm.RealmObject;

public class CounterRecord extends RealmObject {

    private Date date;
    private int count;
    private CounterType counterType;

    public static CounterRecord create(Date date, int count, CounterType type) {
        CounterRecord record = new CounterRecord();
        record.setDate(date);
        record.setCount(count);
        record.setCounterType(type);
        return record;
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

    public CounterType getCounterType() {
        return counterType;
    }

    public void setCounterType(CounterType counterType) {
        this.counterType = counterType;
    }

    @Override
    public String toString() {
        return "CounterRecord{" +
                "date=" + date +
                ", count=" + count +
                ", counterType=" + counterType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterRecord that = (CounterRecord) o;

        if (getCount() != that.getCount()) return false;
        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null)
            return false;
        return getCounterType() != null ? getCounterType().equals(that.getCounterType()) : that.getCounterType() == null;

    }

    @Override
    public int hashCode() {
        int result = getDate() != null ? getDate().hashCode() : 0;
        result = 31 * result + getCount();
        result = 31 * result + (getCounterType() != null ? getCounterType().hashCode() : 0);
        return result;
    }
}
