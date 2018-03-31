package com.face_location.facelocation;

/**
 * Created by admin on 30.11.17.
 */

public class Files {

    String name, fileURL;

    public Files(String name, String fileURL) {
        this.name = name;
        this.fileURL = fileURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
