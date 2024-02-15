package com.iishanto.contactbuddy.model;

import java.util.List;


public class ContactAliasModel extends Model{
    private User contact;
    private List<SaveContactModel> aliases;

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }

    public List<SaveContactModel> getAliases() {
        return aliases;
    }

    public void setAliases(List<SaveContactModel> aliases) {
        this.aliases = aliases;
    }
}
