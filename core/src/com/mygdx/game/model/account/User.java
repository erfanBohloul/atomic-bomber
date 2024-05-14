package com.mygdx.game.model.account;

import java.util.ArrayList;

public class User {

    public static ArrayList<User> users = new ArrayList<User>();

    private static User loginUser = null;

    private final String username,
        password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        users.add(this);
    }

    public static User getUserByName(String username) {
        for (User user : users) {
            if (user.username.equals(username))
                return user;

        }

        return null;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public static User getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(User loginUser) {
        User.loginUser = loginUser;
    }
}
