package com.dinda.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JPanel panelMain;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnWaiters;
    private JButton btnCustomer;

    public Login() {
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(325,340));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        btnWaiters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser("waiters", "Waiter");
            }
        });

        btnCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateCustomer(tfUsername.getText());
                Daftar_Menu daftarMenu = new Daftar_Menu();
                daftarMenu.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }

    private void authenticateUser(String table, String role) {
        String username = tfUsername.getText();
        String password = String.valueOf(pfPassword.getPassword());
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mystore", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM " + table + " WHERE username=? AND password=?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + " (" + role + ")");
                new Dashboard(role).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid " + role + " username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void authenticateCustomer(String username) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Welcome, " + username + " (Customer)");

    }
}