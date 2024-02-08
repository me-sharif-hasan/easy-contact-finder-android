package com.iishanto.contactbuddy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.camera.core.ImageProxy;

import java.nio.ByteBuffer;

public class UtilityAndConstantsProvider {
    private UtilityAndConstantsProvider(){};
    private static UtilityAndConstantsProvider instance;

    public static final String googleAuthType="google-id";
    public static final String classicAuthType="classic";
    public static final String baseUrl="https://contact-buddy.onrender.com/";


    /*
    Methods
     */
    public static Bitmap convertImageProxyToBitmap(ImageProxy image) {
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byteBuffer.rewind();
        byte[] bytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }
}
