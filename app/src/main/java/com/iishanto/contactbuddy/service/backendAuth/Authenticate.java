package com.iishanto.contactbuddy.service.backendAuth;

import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.service.backendAuth.credential.BackendCredential;

public interface Authenticate {

    void auth(BackendCredential credential, UserAuthEvents userAuthEvents);
}
