package com.iishanto.contactbuddy.model;

import java.io.Serializable;

public class SaveContactModel extends Model {
    private String name;
    private String number;
    private Phones aliasTarget;
    private User person;

    public void setPerson(User person) {
        this.person = person;
    }

    public User getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public Phones getAliasTarget() {
        return aliasTarget;
    }

    public void setAliasTarget(Phones aliasTarget) {
        this.aliasTarget = aliasTarget;
    }
}
