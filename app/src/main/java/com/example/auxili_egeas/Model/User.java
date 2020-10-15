package com.example.auxili_egeas.Model;

public class User {
    private String mail,password,username,phone,imageURL;
    private String id;


    public User() {

    }

    public User(String mail, String password, String username, String phone, String imageURL, String id) {
        this.mail = mail;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.imageURL = imageURL;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
