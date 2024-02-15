package com.iishanto.contactbuddy.events;

import com.iishanto.contactbuddy.model.SaveContactModel;

import java.util.List;

public interface AliasLoadedEvent {
    public void success(List<SaveContactModel> aliases);
    public void failure(Exception e);
}
