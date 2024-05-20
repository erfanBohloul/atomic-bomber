package com.mygdx.game.model.account;

import java.util.ArrayList;

public class User {

    private final String username,
        password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
