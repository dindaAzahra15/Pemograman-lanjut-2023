package com.dinda.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Dashboard extends JFrame {
    private JPanel panelMain;
    private JButton btnManageMenu;
    private JButton btnDaftarTransaksi;
    private JButton btnBack;

    public Dashboard(String role) {
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(150,200));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        

        btnManageMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Manage_Menu().setVisible(true);
                dispose();
            }
        });

        btnDaftarTransaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panelMain, "View Transactions button clicked");
                new Daftar_Transaksi().setVisible(true);
                dispose();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                dispose();
            }
        });
    }




    public static void main(String[] args) {
        Dashboard dashboard = new Dashboard("Waiter");
        dashboard.setVisible(true);
    }

}
