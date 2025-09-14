package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.StaffKoperasi;
import com.example.projekpbobd.beans.Transaksi;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TransaksiDAO {
    public static ArrayList<Transaksi> getAll(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM transaksi ORDER BY tanggal_transaksi ASC";
        ArrayList<Transaksi> listTransaksi = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setId(rs.getInt("id_transaksi"));
                transaksi.setTglTransaksi(rs.getDate("tanggal_transaksi").toLocalDate());
                transaksi.setTotalJual(rs.getDouble("total_jual"));
                transaksi.setProfit(rs.getDouble("profit"));
                transaksi.setKuantitas(rs.getInt("kuantitas"));
                transaksi.setId_staff_koperasi(rs.getInt("id_staff_koperasi"));
                transaksi.setId_barang(rs.getInt("id_barang"));
                listTransaksi.add(transaksi);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listTransaksi;
    }

    //untuk excel
    public static Map<String, Object[]> getAllArrayObject(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM transaksi ORDER BY id_transaksi ASC";
        Map<String, Object[]> listTransaksi = new TreeMap<String, Object[]>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                Object[] object = new Object[6];
                object[0] = rs.getInt("id_transaksi");
                object[1] = rs.getDate("tanggal_transaksi");
                object[2] = rs.getDouble("total_jual");
                object[3] = rs.getDouble("profit");
                object[4] = rs.getInt("kuantitas");
                object[5] = rs.getInt("id_staff_koperasi");
                listTransaksi.put(String.valueOf(i), object);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listTransaksi;
    }

    //CRUD
    public static void save(Connection con, Transaksi transaksi) {
        PreparedStatement statement = null;
        String query = "INSERT INTO public.transaksi(tanggal_transaksi, total_jual, profit, kuantitas, id_staff_koperasi) VALUES ( ?, ?, ?, ?, ?)";
        try {
            statement = con.prepareStatement(query);
            statement.setDate(1, Date.valueOf(transaksi.getTglTransaksi()));
            statement.setDouble(2, transaksi.getTotalJual());
            statement.setDouble(3, transaksi.getProfit());
            statement.setInt(4, transaksi.getKuantitas());
            statement.setInt(5, transaksi.getId_staff_koperasi());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void update(Connection con, Transaksi transaksi) {
        PreparedStatement statement = null;
        String query = "UPDATE transaksi SET tanggal_transaksi = ?, total_jual = ?, profit = ?, kuantitas = ?, id_staff_koperasi= ? WHERE id_transaksi = ?";
        try {
            statement = con.prepareStatement(query);
            statement.setDate(1, Date.valueOf(transaksi.getTglTransaksi()));
            statement.setDouble(2, transaksi.getTotalJual());
            statement.setDouble(3, transaksi.getProfit());
            statement.setInt(4, transaksi.getKuantitas());
            statement.setInt(5, transaksi.getId_staff_koperasi());
            statement.setInt(6, transaksi.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void delete(Connection connection, Transaksi transaksi) {
        PreparedStatement ps = null;
        String query = "DELETE FROM transaksi WHERE id_transaksi = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, transaksi.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps);
        }
    }

    //Fitur
    public static ArrayList<Transaksi> getJumlahTransaksiPerStaff(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT id_staff_koperasi, COUNT(*) as jumlah_transaksi " +
                "FROM transaksi " +
                "GROUP BY id_staff_koperasi " +
                "ORDER BY jumlah_transaksi DESC";

        ArrayList<Transaksi> staffTransaksiList = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                // Loop through the result set
                while (rs.next()) {
                    int idStaff = rs.getInt("id_staff_koperasi");
                    int jumlahTransaksi = rs.getInt("jumlah_transaksi");
                    staffTransaksiList.add(new Transaksi(idStaff, jumlahTransaksi));
                }
            }
            return staffTransaksiList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Transaksi> getJumlahTransaksiPerBulan(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT DATE_TRUNC('month', tanggal_transaksi) AS month, COUNT(*) AS jumlah_transaksi\n" +
                "FROM transaksi\n" +
                "GROUP BY month\n" +
                "ORDER BY jumlah_transaksi DESC";

        ArrayList<Transaksi> transaksiList = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate month = rs.getDate("month").toLocalDate();
                int transactionCount = rs.getInt("jumlah_transaksi");
                transaksiList.add(new Transaksi(month, transactionCount));
            }
            return transaksiList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Transaksi> getProfitPerBulan(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT EXTRACT(MONTH FROM tanggal_transaksi) AS bulan,\n" +
                "EXTRACT(YEAR FROM tanggal_transaksi) AS tahun, SUM(profit) AS total_profit\n" +
                "FROM transaksi\n" +
                "GROUP BY bulan, tahun\n" +
                "ORDER BY total_profit DESC";

        ArrayList<Transaksi> transaksiList = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                int month = rs.getInt("bulan");
                int year = rs.getInt("tahun");
                double totalProfit = rs.getDouble("total_profit");

                // Construct LocalDate for the first day of the month
                LocalDate date = LocalDate.of(year, month, 1);

                // Assuming you have a Transaksi constructor that takes these parameters
                transaksiList.add(new Transaksi(totalProfit, date));
            }
            return transaksiList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}



