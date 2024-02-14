package com.iishanto.contactbuddy.model;

import java.io.Serializable;

public class SaveContactModel extends Model {
    private String name;
    private String phone;
    private Phones aliasTarget;
    private User aliasOwner;

    public User getAliasOwner() {
        return aliasOwner;
    }

    public void setAliasOwner(User aliasOwner) {
        this.aliasOwner = aliasOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Phones getAliasTarget() {
        return aliasTarget;
    }

    public void setAliasTarget(Phones aliasTarget) {
        this.aliasTarget = aliasTarget;
    }
}
