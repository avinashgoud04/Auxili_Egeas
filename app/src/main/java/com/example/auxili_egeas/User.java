package com.example.auxili_egeas;

public class User {
    private String mail,password,name,phone;

    public User() {

    }

    public User(String mail, String password, String name, String phone) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.phone = phone;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
