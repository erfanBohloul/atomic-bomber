package com.mygdx.game.controller;

import com.mygdx.game.model.account.User;

import java.util.ArrayList;

public class AccountController {

    private static ArrayList<User> users = new ArrayList<>();
    public static User loginUser = null;

    public static void login(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Username or password is empty");
        }

        User user = getUserByName(username);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }


    }

    public static User getUserByName(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }

        return null;
    }

    public static void register(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Username or password is empty");
        }

        User user = getUserByName(username);
        if (user != null) {
            throw new Exception("Username already taken");
        }

        User newUser = new User(username, password);
        addUser(newUser);
    }

    public static void setLoginUser(User newUser) {
        loginUser = newUser;
    }

    public static User getLoginUser() {
        return loginUser;
    }

    public static void addUser(User user) {
        users.add(user);
    }
}
