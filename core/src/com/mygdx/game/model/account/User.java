package com.mygdx.game.model.account;

import java.util.ArrayList;
import java.util.Random;

public class User {

    private String username,
        password;

    private final boolean isGuest;
    public int numAvatar;

    public User(String username, String password, boolean isGuest) {
        this(username, password, isGuest, (new Random()).nextInt(4));
    }

    public User(String username, String password, boolean isGuest, int numAvater) {
        this.username = username;
        this.password = password;
        this.isGuest = isGuest;
        this.numAvatar = numAvater;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
