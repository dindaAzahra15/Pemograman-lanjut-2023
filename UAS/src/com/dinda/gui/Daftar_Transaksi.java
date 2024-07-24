package com.dinda.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Daftar_Transaksi extends JFrame {
    private JTable tableTransaksi;
    private JPanel panelMain;
    private JLabel no;
    private JLabel nama;
    private JLabel total;
    private JLabel waktu;
    private JButton btnClear;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/manage_menu";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Daftar_Transaksi() {
        setTitle("Daftar Transaksi");
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"No", "Nama Customer", "Total", "Waktu Transaksi"}, 0);
        tableTransaksi.setModel(tableModel);

        loadTransaksi();

        tableTransaksi.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = tableTransaksi.getSelectedRow();
                if (selectedRow >= 0) {
                    String noText = tableModel.getValueAt(selectedRow, 0).toString();
                    String namaText = tableModel.getValueAt(selectedRow, 1).toString();
                    String totalText = tableModel.getValueAt(selectedRow, 2).toString();
                    String waktuText = tableModel.getValueAt(selectedRow, 3).toString();
                    no.setText(noText);
                    nama.setText(namaText);
                    total.setText(totalText);
                    waktu.setText(waktuText);
                }
            }
        });


        btnClear.addActionListener(e -> clearTransaksi());

        setVisible(true);
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static void insertTransaction(String customer, double totalHarga, String tanggal) {
        String query = "INSERT INTO transaksi (nama_customer, harga_total, waktu_transaksi) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customer);
            statement.setDouble(2, totalHarga);
            statement.setString(3, tanggal);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTransaction(Transaction transaction) {
        insertTransaction(transaction.getCustomer(), transaction.getTotalHarga(), transaction.getTanggal());
    }

    private void loadTransaksi() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM transaksi")) {

            int no = 1;
            while (resultSet.next()) {
                String namaCustomer = resultSet.getString("nama_customer");
                String hargaTotal = resultSet.getString("harga_total");
                String waktuTransaksi = resultSet.getString("waktu_transaksi");
                tableModel.addRow(new Object[]{no++, namaCustomer, hargaTotal, waktuTransaksi});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        loadTransaksi();
    }
    private void clearTransaksi() {
        String query = "DELETE FROM transaksi";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Semua transaksi berhasil dihapus.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Daftar_Transaksi());
    }
}