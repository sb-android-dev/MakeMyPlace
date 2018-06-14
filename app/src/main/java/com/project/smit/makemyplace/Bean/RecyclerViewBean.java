package com.project.smit.makemyplace.Bean;

/**
 * Created by Smit on 28-08-2017.
 */

public class RecyclerViewBean {

    String name;
    int image;
    String phone;
    String address;
    String latitude;
    String longitude;

    public RecyclerViewBean(String name, int image, String phone, String address, String latitude, String longitude){
        this.name=name;
        this.image=image;
        this.phone=phone;
        this.address=address;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getPhone() { return phone; }

    public String getAddress() { return address; }

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public void setAddress(String address) { this.address = address; }

    public void setLatitude(String latitude) { this.phone = latitude; }

    public void setLongitude(String longitude) { this.address = longitude; }
}
