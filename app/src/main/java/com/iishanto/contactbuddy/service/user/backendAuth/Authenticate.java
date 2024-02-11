package com.iishanto.contactbuddy.service.user.backendAuth;

import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.service.user.backendAuth.credential.BackendCredential;

public interface Authenticate {

    void auth(BackendCredential credential, UserAuthEvents userAuthEvents);
}
