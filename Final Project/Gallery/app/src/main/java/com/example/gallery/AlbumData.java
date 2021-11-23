package com.example.gallery;

public class AlbumData {
    private String name;
    private String picturePath;

    public AlbumData(String name, String picturePath) {
        this.name = name;
        this.picturePath = picturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
