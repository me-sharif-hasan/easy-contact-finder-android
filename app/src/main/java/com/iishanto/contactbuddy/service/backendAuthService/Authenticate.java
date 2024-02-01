package com.iishanto.contactbuddy.service.backendAuthService;

import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.service.backendAuthService.credential.BackendCredential;

public interface Authenticate {

    void auth(BackendCredential credential, UserAuthEvents userAuthEvents);
}
