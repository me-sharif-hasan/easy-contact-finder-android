package com.iishanto.contactbuddy.service.contact;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ContactService {

    // Context is needed to access ContentResolver
    private final Context context;

    public ContactService(Context context) {
        this.context = context;
    }

    public void saveContact(String name, String phone, Bitmap image) {
        ContentResolver resolver = context.getContentResolver();

        // Insert data into RawContacts table
        ContentValues rawContactValues = new ContentValues();
        Uri rawContactUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, rawContactValues);
        long rawContactId = Long.parseLong(rawContactUri.getLastPathSegment());

        // Insert contact details into Data table
        ContentValues contactValues = new ContentValues();
        contactValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contactValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contactValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        resolver.insert(ContactsContract.Data.CONTENT_URI, contactValues);

        // Insert phone number into Data table
        ContentValues phoneValues = new ContentValues();
        phoneValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        phoneValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        phoneValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        resolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues);

        // Insert photo into Data table if image is provided
        if (image != null) {
            ContentValues photoValues = new ContentValues();
            photoValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            photoValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            photoValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, getByteArrayFromBitmap(image));
            resolver.insert(ContactsContract.Data.CONTENT_URI, photoValues);
        }

        Log.d("CONTACT", "Contact saved successfully");
    }

    // Convert Bitmap image to byte array
    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
