package com.reactiverobot.latecounter.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CounterType extends RealmObject {

    @PrimaryKey
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
