package com.iishanto.contactbuddy.events;

import com.iishanto.contactbuddy.model.Base64ImageSearchModel;
import com.iishanto.contactbuddy.model.User;

import java.util.List;

public interface UserSearchEvent {
    void success( List <User> userList);
    void failure(Exception e);
}
