package com.dinda.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Waiters {
    private static final String URL = "jdbc:mysql://localhost:3306/managemenu";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM menu")) {

            while (resultSet.next()) {
                String menu = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");
                System.out.println("Menu: " + menu + ", Harga: " + harga);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
