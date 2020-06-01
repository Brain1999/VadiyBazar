package com.dasturchi.app.vadiybazar.model;

public class UserPay {
    int coin;
    String tur;
    String raqam;
    String phone;

    public UserPay() {
    }

    public UserPay(int coin, String tur, String raqam, String phone) {
        this.coin = coin;
        this.tur = tur;
        this.raqam = raqam;
        this.phone = phone;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    public String getRaqam() {
        return raqam;
    }

    public void setRaqam(String raqam) {
        this.raqam = raqam;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
