package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.Barang;
import com.example.projekpbobd.beans.Departemen;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DepartmentDAO {
    public static ArrayList<Departemen> getAll(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM departemen WHERE status_aktif = 1 ORDER BY id_departemen asc";
        ArrayList<Departemen> listDepartemen = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                Departemen departemen = new Departemen();
                departemen.setId(rs.getInt("id_departemen"));
                departemen.setNamaDepartemen(rs.getString("nama_departemen"));
                departemen.setKepalaDepartemen(rs.getInt("kepala_departemen"));
                departemen.setRole(rs.getString("role"));
                listDepartemen.add(departemen);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listDepartemen;
    }

    public static Map<String, Object[]> getAllArrayObject(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM departemen WHERE status_aktif = 1 ORDER BY id_departemen ASC";
        Map<String, Object[]> listDepartemen = new TreeMap<String, Object[]>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[] object = new Object[6];
                object[0] = rs.getInt("id_departemen");
                object[1] = rs.getString("nama_departemen");
                object[2] = rs.getInt("kepala_departemen");
                object[3] = rs.getString("role");
                listDepartemen.put(String.valueOf(i), object);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listDepartemen;
    }

    public static void save(Connection con, Departemen departemen){
        PreparedStatement statement = null;
        String query = "INSERT INTO public.departemen(nama_departemen, kepala_departemen, role) VALUES (?, ?, ?)";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, departemen.getNamaDepartemen());
            statement.setInt(2, departemen.getKepalaDepartemen());
            statement.setString(3, departemen.getRole());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void update(Connection con, Departemen departemen) {
        PreparedStatement statement = null;
        String query = "UPDATE departemen SET nama_departemen = ?, kepala_departemen = ?, role = ? WHERE id_departemen = ?";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, departemen.getNamaDepartemen());
            statement.setInt(2, departemen.getKepalaDepartemen());
            statement.setString(3, departemen.getRole());
            statement.setInt(4, departemen.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void delete(Connection connection, Departemen departemen) {
        PreparedStatement ps = null;
        String query = "UPDATE departemen SET status_aktif = 0 WHERE id_departemen = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, departemen.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps);
        }
    }

    public static ArrayList<Departemen> getJumlahPerDepartemen(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT d.id_departemen AS id_departemen, d.nama_departemen, COUNT(s.id_departemen) AS jumlah_anggota\n" +
                "FROM departemen d\n" +
                "LEFT JOIN staff_koperasi s ON d.id_departemen = s.id_departemen\n" +
                "GROUP BY d.id_departemen, d.nama_departemen;";

        ArrayList<Departemen> categoryStats = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                int idDepartemen = rs.getInt("id_departemen");
                String namaDepartemen = rs.getString("nama_departemen");
                int jumlah = rs.getInt("jumlah_anggota");
                // Buat objek Barang dengan hanya mengisi kategori dan jumlahBarang
                Departemen departemen = new Departemen(idDepartemen, namaDepartemen, jumlah);
                categoryStats.add(departemen);
            }
            return categoryStats;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving product categories stats", e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
    }

}
