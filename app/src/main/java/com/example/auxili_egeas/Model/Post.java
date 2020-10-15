package com.example.auxili_egeas.Model;

public class Post {

    String bmodel,bfair,btime,imageURL;

    public Post(String bmodel, String bfair, String btime, String imageURL) {
        this.bmodel = bmodel;
        this.bfair = bfair;
        this.btime = btime;
        this.imageURL = imageURL;
    }

    public Post() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBmodel() {
        return bmodel;
    }

    public void setBmodel(String bmodel) {
        this.bmodel = bmodel;
    }

    public String getBfair() {
        return bfair;
    }

    public void setBfair(String bfair) {
        this.bfair = bfair;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }
}
