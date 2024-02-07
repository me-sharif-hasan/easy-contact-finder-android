package com.iishanto.contactbuddy.events;

import android.graphics.Bitmap;

public interface OnFaceDetected {
    public void onFaceDetected(Bitmap face);
    public void onFaceNotDetected();
}
