package com.mygdx.game.controller;

import com.mygdx.game.model.account.User;

import java.util.ArrayList;

public class AccountController {

    public static User loginUser = null;

    public static User login(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Username or password is empty");
        }

        User user = getUser(username, password);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }

        return user;
    }

    public static User getUser(String username, String password) {
        if (Database.userExist(username, password)) {
            User user = new User(username, password, false);
            return user;
        }

        return null;
    }

    public static void register(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Username or password is empty");
        }

        User user = getUser(username, password);
        if (user != null) {
            throw new Exception("Username already taken");
        }

        User newUser = new User(username, password, false);
        addUser(newUser);
    }

    public static User guest() {
        User guestUser = new User("guest", "1234", true);
        return guestUser;
    }

    public static void setLoginUser(User newUser) {
        loginUser = newUser;
    }

    public static User getLoginUser() {
        return loginUser;
    }

    public static void addUser(User user) {
        Database.addUser(user.getUsername(), user.getPassword());
    }
}
