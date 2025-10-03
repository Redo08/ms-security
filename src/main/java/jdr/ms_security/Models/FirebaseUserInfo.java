package jdr.ms_security.Models;

public class FirebaseUserInfo {
    private String uid;
    private String email;
    private String name;
    private String picture;

    public FirebaseUserInfo(String uid, String email, String name, String picture) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "FirebaseUserInfo{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}