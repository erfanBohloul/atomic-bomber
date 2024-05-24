package com.mygdx.game.controller;

import java.sql.*;

public class Database {

    private static final String url = "jdbc:mysql://localhost:3306/atomic_bomber",
            username = "root",
            password = "";

    public static Connection connection;

    public static void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changePassword(String username, String newPassword) throws Exception {
        if (!usernameExist(username)) {
            throw new Exception("user doesn't exist.");
        }

        run();
        String query = "update users SET password = \'"+ newPassword +"\' WHERE username = \'" + username + "\'";
        System.out.println("query: " + query);
        Statement statement = connection.createStatement();
        int res = statement.executeUpdate(query);
        System.out.println("[SUCCESS] Password changed!");
        connection.close();
    }

    public static void changeUsername(String username, String newUsername) throws Exception {
        if (username.equals(newUsername)) {
            throw new Exception("username is equal to newUsername");
        }

        if (!usernameExist(username)) {
            throw new Exception("user doesn't exist.");
        }

        if (usernameExist(newUsername)) {
            throw new Exception("username already taken");
        }

        run();
        String query = "update users set username = \'" + newUsername + "\' where username = \'" + username + "\'";
        System.out.println("query: " + query);
        Statement statement = connection.createStatement();
        int res = statement.executeUpdate(query);
        System.out.println("[SUCCESS] User '" + username + "' has been changed.");
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    public static boolean addUser(String username, String password) {
        try {
            run();
            Statement statement = connection.createStatement();
            String query = "insert into users(username, password) values (\'" + username + "\', \'" + password + "\')";
            int result = statement.executeUpdate(query);
            System.out.println("[SUCC] database message: user added successfully.");

            closeConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean usernameExist(String username) {
        try {
            run();
            Statement statement = connection.createStatement();
            String query = "select * from users where username = \'" + username + "\'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println("[SUCC] database message: username does exist.");
                closeConnection();
                return true;
            }

            System.out.println("[SUCC] database message: username does not exist.");
            closeConnection();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean userExist(String username, String password) {
        try {
            run();
            Statement statement = connection.createStatement();
            String query = "select * from users where username = \'" + username + "\'";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String pass = resultSet.getString(3);
                if (pass.equals(password)) {

                    System.out.println("[SUCC] database message: user does exist.");
                    closeConnection();
                    return true;
                }
            }
            System.out.println("[SUCC] database message: user does not exist.");
            closeConnection();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(String username) {
        try {
            run();
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate("delete from users where username=" + username);

            System.out.println("[SUCC] database message: user deleted.");
            closeConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
