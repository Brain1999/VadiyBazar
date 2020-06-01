package com.dasturchi.app.vadiybazar.model;

public class Admins {
    String login;
    String parol;

    public Admins() {
    }

    public Admins(String login, String parol) {
        this.login = login;
        this.parol = parol;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getParol() {
        return parol;
    }

    public void setParol(String parol) {
        this.parol = parol;
    }
}
