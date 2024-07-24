package com.dinda.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Manage_Menu extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/manage_menu";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private JPanel panelMain;
    private JTable jTableMenu;
    private JTextField tfMenu;
    private JTextField tfHarga;
    private JButton btnTambah;
    private JButton btnEdit;
    private JButton btnHapus;
    private JButton btnBack;
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private String selectedMenu = "";

    public Manage_Menu() {
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(450, 460));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String menu = tfMenu.getText();
                double harga = Double.parseDouble(tfHarga.getText());

                Menu newMenu = new Menu();
                newMenu.setMenu(menu);
                newMenu.setHarga(harga);

                insertMenu(newMenu);
                refreshTable(getMenu());
                clearForm();
            }
        });

        jTableMenu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int row = jTableMenu.getSelectedRow();

                if (row < 0)
                    return;

                String menu = jTableMenu.getValueAt(row, 0).toString();

                if (selectedMenu.equals(menu))
                    return;

                selectedMenu = menu;

                String harga = jTableMenu.getValueAt(row, 1).toString();

                tfMenu.setText(menu);
                tfHarga.setText(harga);
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMenu.isEmpty()) return;

                String menu = tfMenu.getText();
                double harga = Double.parseDouble(tfHarga.getText());

                Menu updatedMenu = new Menu();
                updatedMenu.setMenu(menu);
                updatedMenu.setHarga(harga);

                updateMenu(selectedMenu, updatedMenu);
                refreshTable(getMenu());
                clearForm();
                selectedMenu = "";
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String menu = tfMenu.getText();
                hapusMenu(menu);
                refreshTable(getMenu());
                clearForm();
                selectedMenu = ""; // Reset selected menu
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showOptionDialog(
                        panelMain,
                        "Ke mana kamu mau pergi?",
                        "Pilih Tujuan",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Dashboard", "Login", "Cancel"},
                        "Dashboard"
                );
                if (option == 0) {
                    new Dashboard("Waiter").setVisible(true);
                    dispose();
                } else if (option == 1) {
                    new Login().setVisible(true);
                    dispose();
                }

            }
        });

        refreshTable(getMenu());

    }

    private void clearForm() {
        tfMenu.setText("");
        tfHarga.setText("");
    }

    public void refreshTable(List<Menu> arrayListMenu) {
        Object[][] data = new Object[arrayListMenu.size()][2];

        for (int i = 0; i < arrayListMenu.size(); i++) {
            data[i] = new Object[]{
                    arrayListMenu.get(i).getMenu(),
                    arrayListMenu.get(i).getHarga(),
            };
        }

        defaultTableModel = new DefaultTableModel(
                data,
                new String[]{"Menu", "Harga"}
        );

        jTableMenu.setModel(defaultTableModel);
    }

    private static void executeSql(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ResultSet executeQuery(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void insertMenu(Menu menu) {
        String sql = "INSERT INTO menu (menu, harga) VALUES (" +
                "'" + menu.getMenu() + "', " +
                menu.getHarga() + ")";
        executeSql(sql);
    }

    private static void updateMenu(String oldMenu, Menu newMenu) {
        String sql = "UPDATE menu SET " +
                "menu = '" + newMenu.getMenu() + "', " +
                "harga = " + newMenu.getHarga() +
                " WHERE menu = '" + oldMenu + "'";
        executeSql(sql);
    }

    private static void hapusMenu(String menu) {
        String sql = "DELETE FROM menu WHERE menu = '" + menu + "'";
        executeSql(sql);
    }

    private static List<Menu> getMenu() {
        List<Menu> arrayListMenu = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM menu");

        try {
            while (resultSet.next()) {
                String menu = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");

                Menu menuItem = new Menu();
                menuItem.setMenu(menu);
                menuItem.setHarga(harga);

                arrayListMenu.add(menuItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return arrayListMenu;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Manage_Menu().setVisible(true);
            }
        });
    }
}