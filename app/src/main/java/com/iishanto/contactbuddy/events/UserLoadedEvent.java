package com.iishanto.contactbuddy.events;

import com.iishanto.contactbuddy.model.User;

public interface UserLoadedEvent {
    void success(User user);
    void failure(Exception e);
}
