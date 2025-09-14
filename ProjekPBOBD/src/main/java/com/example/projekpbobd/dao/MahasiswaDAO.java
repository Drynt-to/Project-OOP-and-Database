package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.Mahasiswa;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MahasiswaDAO {
    public static ArrayList<Mahasiswa> getAll(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM mahasiswa WHERE status_aktif = 1 ORDER BY id_mahasiswa ASC";
        ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setId(rs.getInt("id_mahasiswa"));
                mahasiswa.setNamaDepan(rs.getString("nama_depan_mahasiswa"));
                mahasiswa.setNamaBelakang(rs.getString("nama_belakang_mahasiswa"));
                mahasiswa.setNoTelepon(rs.getString("no_telepon"));
                mahasiswa.setIdStaffKoperasi(rs.getInt("id_staff_koperasi"));
                listMahasiswa.add(mahasiswa);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listMahasiswa;
    }

    public static Map<String, Object[]> getAllArrayObject(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM mahasiswa WHERE status_aktif = 1 ORDER BY id_mahasiswa ASC";
        Map<String, Object[]> listMahasiswa = new TreeMap<String, Object[]>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[] object = new Object[6];
                object[0] = rs.getInt("id_mahasiswa");
                object[1] = rs.getString("nama_depan_mahasiswa");
                object[2] = rs.getString("nama_belakang_mahasiswa");
                object[3] = rs.getString("no_telepon");
                object[4] = rs.getInt("id_staff_koperasi");
                listMahasiswa.put(String.valueOf(i), object);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listMahasiswa;
    }

    public static void save(Connection con, Mahasiswa mahasiswa){
        PreparedStatement statement = null;
        String query = "INSERT INTO public.mahasiswa(nama_depan_mahasiswa, nama_belakang_mahasiswa, no_telepon, id_staff_koperasi) VALUES (?, ?, ?, ?)";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, mahasiswa.getNamaDepan());
            statement.setString(2, mahasiswa.getNamaBelakang());
            statement.setString(3, mahasiswa.getNoTelepon());
            statement.setInt(4, mahasiswa.getIdStaffKoperasi());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void update(Connection con, Mahasiswa mahasiswa) {
        PreparedStatement statement = null;
        String query = "UPDATE mahasiswa SET nama_depan_mahasiswa = ?, nama_belakang_mahasiswa = ?, no_telepon = ?, id_staff_koperasi = ? WHERE id_mahasiswa = ?";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, mahasiswa.getNamaDepan());
            statement.setString(2, mahasiswa.getNamaBelakang());
            statement.setString(3, mahasiswa.getNoTelepon());
            statement.setInt(4, mahasiswa.getIdStaffKoperasi());
            statement.setInt(5, mahasiswa.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void delete(Connection connection, Mahasiswa mahasiswa) {
        PreparedStatement ps = null;
        String query = "UPDATE mahasiswa SET status_aktif = 0 WHERE id_mahasiswa = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, mahasiswa.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps);
        }
    }

}
