package com.iishanto.contactbuddy.model;

public class ProfilePictureUploadModel extends Model{
    public ProfilePictureUploadModel(String base_64_image, String file_ext) {
        this.base_64_image = base_64_image;
        this.file_ext = file_ext;
    }

    public String getBase_64_image() {
        return base_64_image;
    }

    public void setBase_64_image(String base_64_image) {
        this.base_64_image = base_64_image;
    }

    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    String base_64_image;
    String file_ext;
}
