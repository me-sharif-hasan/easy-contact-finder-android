package com.iishanto.contactbuddy.service.contact;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.SaveContactCollections;
import com.iishanto.contactbuddy.model.SaveContactModel;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactService {
    private String TAG="CONTACT_SERVICE";

    // Context is needed to access ContentResolver
    private final Context context;
    HttpClient httpClient;

    public ContactService(Context context) {
        this.context = context;
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
    }

    public void saveContact(String name, String phone, Bitmap image) throws Exception {
        List <SaveContactModel> contact=new ArrayList<>();
        SaveContactModel saveContactModel=new SaveContactModel();
        saveContactModel.setName(name);
        saveContactModel.setPhone(phone);
        contact.add(saveContactModel);
        saveAllContacts(contact,image);
    }



    // Convert Bitmap image to byte array
    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public void saveAllContacts(List<SaveContactModel> numbers, Bitmap imageBitmap) throws Exception {
        System.out.println(numbers);
        Set<String> names=new HashSet<>();
        for (SaveContactModel number:numbers){
            if(number==null){
                names.add(String.valueOf(System.currentTimeMillis()));
                continue;
            }
            names.add(number.getName());
        }
        System.out.println(names.size());
        ContentResolver resolver=null;
        // Insert data into RawContacts table
        ContentValues rawContactValues=null;
        Uri rawContactUri=null;
        long rawContactId=0;

        if(names.size()==1){
            resolver = context.getContentResolver();
            // Insert data into RawContacts table
            rawContactValues = new ContentValues();
            rawContactUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, rawContactValues);
            if(rawContactUri==null) throw new Exception("Failure saving contact.");
            rawContactId = Long.parseLong(rawContactUri.getLastPathSegment());
        }

        for (SaveContactModel contact:numbers){
            if(names.size()>1){
                System.out.println("HAVE MULTIPLE NAMES");
                resolver = context.getContentResolver();
                // Insert data into RawContacts table
                rawContactValues = new ContentValues();
                rawContactUri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, rawContactValues);
                if(rawContactUri==null) throw new Exception("Failure saving contact.");
                rawContactId = Long.parseLong(rawContactUri.getLastPathSegment());
            }
            // Insert contact details into Data table
            ContentValues contactValues = new ContentValues();
            contactValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            contactValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            contactValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName());
            resolver.insert(ContactsContract.Data.CONTENT_URI, contactValues);

            // Insert phone number into Data table
            ContentValues phoneValues = new ContentValues();
            phoneValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            phoneValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            phoneValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone());
            resolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues);// Insert photo into Data table if image is provided
            if (imageBitmap != null) {
                System.out.println("Setting image bitmap");
                ContentValues photoValues = new ContentValues();
                photoValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                photoValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                photoValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, getByteArrayFromBitmap(imageBitmap));
                resolver.insert(ContactsContract.Data.CONTENT_URI, photoValues);
            }
        }
        saveToServer(numbers);
        Log.d("CONTACT", "Contact saved successfully");
    }


    private void saveToServer(List<SaveContactModel> numbers) throws Exception{
        SaveContactCollections saveContactCollections=new SaveContactCollections();
        saveContactCollections.setData(numbers);
        System.out.println("SAVING CONTACT: "+saveContactCollections.toJsonNode().toString());
        httpClient.post("api/phone/save-alias", saveContactCollections, new HttpEvent() {
            @Override
            public void success(String data) {
                Log.i(TAG, "success: contact saved in backend: "+data);
            }

            @Override
            public void failure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
