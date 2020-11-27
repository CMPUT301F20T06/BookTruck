package com.example.booktruck;

public class UrlModel {

    private String imageUrl;
    public UrlModel(){

    }
    public UrlModel(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
