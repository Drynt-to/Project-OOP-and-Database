package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.Transaksi;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class TransaksiDAO {
    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> transaksis = new ArrayList<>();
        ConnectionManager connectionManager = new ConnectionManager();
        String connectQuery = "SELECT * FROM proyeksem2.transaksi";

        try (Connection conn = connectionManager.conn();
             PreparedStatement pst = conn.prepareStatement(connectQuery);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Integer id = rs.getInt("id_transaksi");
                Date date = rs.getDate("tanggal_transaksi");
                int total_jual = rs.getInt("total_jual");
                Double profit = rs.getDouble("profit");
                int kuantitas = rs.getInt("kuantitas");
                Integer id_staff = rs.getInt("id_staff_koperasi");
                Transaksi transaksi = new Transaksi(id, date, total_jual, profit, kuantitas, id_staff);
                transaksis.add(transaksi);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaksis;
    }

}
