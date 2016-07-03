package com.reactiverobot.latecounter.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CounterType extends RealmObject {

    @PrimaryKey
    private String description;
    private int widgetId;
    private int colorId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetid) {
        this.widgetId = widgetid;
    }

    public static CounterType withDescription(String description) {
        CounterType counterType = new CounterType();
        counterType.setDescription(description);
        return counterType;
    }

    @Override
    public String toString() {
        return "CounterType{" +
                "description='" + description + '\'' +
                ", widgetId=" + widgetId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterType that = (CounterType) o;

        return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;

    }

    @Override
    public int hashCode() {
        return getDescription() != null ? getDescription().hashCode() : 0;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
