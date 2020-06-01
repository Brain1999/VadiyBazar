package com.dasturchi.app.vadiybazar.model;

public class User {
    public String name;
    public String phone;
    public String parol;
    public int coin;

    public User() {
    }

    public User(String name, String phone, String parol,int coin) {
        this.name = name;
        this.phone = phone;
        this.parol = parol;
        this.coin = coin;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getParol() {
        return parol;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setParol(String parol) {
        this.parol = parol;
    }
}
