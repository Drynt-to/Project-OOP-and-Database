package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.Pegawai;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PegawaiDAO {
    public static ArrayList<Pegawai> getAll(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pegawai WHERE status_aktif = 1 ORDER BY id_pegawai asc";
        ArrayList<Pegawai> listPegawai = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                Pegawai pegawai = new Pegawai();
                pegawai.setId(rs.getInt("id_pegawai"));
                pegawai.setNamaDepan(rs.getString("nama_depan_pegawai"));
                pegawai.setNamaBelakang(rs.getString("nama_belakang_pegawai"));
                pegawai.setNoTelepon(rs.getString("no_telepon"));
                pegawai.setIdStaffKoperasi(rs.getInt("id_staff_koperasi"));
                listPegawai.add(pegawai);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listPegawai;
    }

    public static Map<String, Object[]> getAllArrayObject(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pegawai WHERE status_aktif = 1 ORDER BY id_pegawai ASC";
        Map<String, Object[]> listPegawai = new TreeMap<String, Object[]>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[] object = new Object[6];
                object[0] = rs.getInt("id_pegawai");
                object[1] = rs.getString("nama_depan_pegawai");
                object[2] = rs.getString("nama_belakang_pegawai");
                object[3] = rs.getString("no_telepon");
                object[4] = rs.getInt("id_staff_koperasi");
                listPegawai.put(String.valueOf(i), object);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listPegawai;
    }

    public static void save(Connection con, Pegawai pegawai){
        PreparedStatement statement = null;
        String query = "INSERT INTO public.pegawai(nama_depan_pegawai, nama_belakang_pegawai, no_telepon, id_staff_koperasi) VALUES ( ?, ?, ?, ?)";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, pegawai.getNamaDepan());
            statement.setString(2, pegawai.getNamaBelakang());
            statement.setString(3, pegawai.getNoTelepon());
            statement.setInt(4, pegawai.getIdStaffKoperasi());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void update(Connection con, Pegawai pegawai) {
        PreparedStatement statement = null;
        String query = "UPDATE pegawai SET nama_depan_pegawai = ?, nama_belakang_pegawai = ?, no_telepon = ?, id_staff_koperasi=? WHERE id_pegawai= ?";
        try {
            statement = con.prepareStatement(query);
            statement.setString(1, pegawai.getNamaDepan());
            statement.setString(2, pegawai.getNamaBelakang());
            statement.setString(3, pegawai.getNoTelepon());
            statement.setInt(4, pegawai.getIdStaffKoperasi());
            statement.setInt(5, pegawai.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void delete(Connection connection, Pegawai pegawai) {
        PreparedStatement ps = null;
        String query = "UPDATE pegawai SET status_aktif = 0 WHERE id_pegawai= ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, pegawai.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps);
        }
    }



}
