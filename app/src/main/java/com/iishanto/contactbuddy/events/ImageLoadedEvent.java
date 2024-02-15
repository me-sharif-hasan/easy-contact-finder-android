package com.iishanto.contactbuddy.events;

import android.graphics.Bitmap;

public interface ImageLoadedEvent {
    void success(Bitmap bitmap);
    void failure(Exception e);
}
