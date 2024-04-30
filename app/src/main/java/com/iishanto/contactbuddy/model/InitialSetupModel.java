package com.iishanto.contactbuddy.model;

public class InitialSetupModel extends Model{
    private Base64ImageModel base64Image;
    private String phoneNumber;

    public Base64ImageModel getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = new Base64ImageModel();
        this.base64Image.setBase_64_image(base64Image);
        this.base64Image.setFile_ext("png");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
