package com.iishanto.contactbuddy.model;

public class Base64ImageModel extends Model{
    private String base_64_image;
    private String original_name;
    private String file_ext;
    private String path;

    public String getBase_64_image() {
        return base_64_image;
    }

    public void setBase_64_image(String base_64_image) {
        this.base_64_image = base_64_image;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
