package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.Barang;
import com.example.projekpbobd.beans.Transaksi;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class BarangDAO {
    public static ArrayList<Barang> getAll(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM barang WHERE status_aktif = 1 ORDER BY id_barang asc";
        ArrayList<Barang> listGuru = new ArrayList<>();
        try {
            calculateProfitMargin(con);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Barang barang = new Barang();
                barang.setId(rs.getInt("id_barang"));
                barang.setNama(rs.getString("nama_barang"));
                barang.setHargaPokok(rs.getDouble("harga_pokok"));
                barang.setHargaJual(rs.getDouble("harga_jual"));
                barang.setKategori(rs.getString("kategori"));
                barang.setProfit_margin(rs.getDouble("profit_margin"));
                listGuru.add(barang);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listGuru;
    }

    //get untuk excel
    public static Map<String, Object[]> getAllArrayObject(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM barang WHERE status_aktif = 1 ORDER BY id_barang ASC";
        Map<String, Object[]> listDataBarang = new TreeMap<String, Object[]>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                Object[] object = new Object[6];
                object[0] = rs.getInt("id_barang");
                object[1] = rs.getString("nama_barang");
                object[2] = rs.getDouble("harga_pokok");
                object[3] = rs.getDouble("harga_jual");
                object[4] = rs.getString("kategori");
                listDataBarang.put(String.valueOf(i), object);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listDataBarang;
    }

    public static void save(Connection con, Barang barang) {
        PreparedStatement statement = null;
        String query = "INSERT INTO public.barang(nama_barang, harga_pokok, harga_jual, kategori) VALUES (?, ?, ?, ?)";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, barang.getNama());
            statement.setDouble(2, barang.getHargaPokok());
            statement.setDouble(3, barang.getHargaJual());
            statement.setString(4, barang.getKategori());
            statement.executeUpdate();
            calculateProfitMargin(con);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void update(Connection con, Barang barang) {
        PreparedStatement statement = null;
        String query = "UPDATE barang SET nama_barang = ?, harga_pokok = ?, harga_jual = ?, kategori = ? WHERE id_barang = ?";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, barang.getNama());
            statement.setDouble(2, barang.getHargaPokok());
            statement.setDouble(3, barang.getHargaJual());
            statement.setString(4, barang.getKategori());
            statement.setInt(5, barang.getId());
            statement.executeUpdate();
            calculateProfitMargin(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void delete(Connection connection, Barang barang) {
        PreparedStatement ps = null;
        String query = "UPDATE barang SET status_aktif = 0 WHERE id_barang = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, barang.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps);
        }
    }

    public static ArrayList<Barang> getBarangPerKategori(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT kategori, COUNT(*) AS jumlah_barang " +
                "FROM barang " +
                "WHERE status_aktif = 1 " +
                "GROUP BY kategori " +
                "ORDER BY jumlah_barang DESC";

        ArrayList<Barang> categoryStats = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String kategori = rs.getString("kategori");
                int jumlahBarang = rs.getInt("jumlah_barang");
                // Buat objek Barang dengan hanya mengisi kategori dan jumlahBarang
                Barang barang = new Barang(kategori, jumlahBarang);
                categoryStats.add(barang);
            }
            return categoryStats;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving product categories stats", e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
    }

    public static ArrayList<Barang> getPenjualanTerbanyak(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT t.id_barang, b.nama_barang, b.kategori, SUM(t.kuantitas) AS total_kuantitas \n" +
                "FROM transaksi t \n" +
                "INNER JOIN barang b ON t.id_barang = b.id_barang \n" +
                "GROUP BY t.id_barang, b.nama_barang, b.kategori \n" +
                "ORDER BY total_kuantitas DESC";

        ArrayList<Barang> penjualanList = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                int idBarang = rs.getInt("id_barang");
                String namaBarang = rs.getString("nama_barang");
                String kategori = rs.getString("kategori");
                int kuantitas = rs.getInt("total_kuantitas");

                // Membuat objek Transaksi
                Barang barang = new Barang(idBarang, namaBarang, kategori, kuantitas);
                penjualanList.add(barang);
            }
            return penjualanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving product categories stats", e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
    }

    public static void calculateProfitMargin(Connection con) {
        PreparedStatement ps = null;
        String query = "UPDATE barang SET profit_margin = ((harga_jual - harga_pokok) / harga_jual) * 100";
        try{
            ps = con.prepareStatement(query);
            ps.execute();
        }catch (SQLException e) {
            throw new RuntimeException("Error at calculate Profit Margin", e);
        }finally {
            ConnectionManager.close(ps);
        }
    }
}
