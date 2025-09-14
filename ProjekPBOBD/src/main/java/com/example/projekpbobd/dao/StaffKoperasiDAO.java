package com.example.projekpbobd.dao;

import com.example.projekpbobd.beans.Barang;
import com.example.projekpbobd.beans.StaffKoperasi;
import com.example.projekpbobd.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class StaffKoperasiDAO {
    public static ArrayList<StaffKoperasi> getAll(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM staff_koperasi WHERE status_aktif = 1 ORDER BY id_staff_koperasi asc";
        ArrayList<StaffKoperasi> staffKoperasi = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                StaffKoperasi staffKoperasi2 = new StaffKoperasi();
                staffKoperasi2.setId(rs.getInt("id_staff_koperasi"));
                staffKoperasi2.setIdDepartemen(rs.getInt("id_departemen"));

                // Handling potential null values in dates
                Date tanggalMulai = rs.getDate("tanggal_mulai");
                Date tanggalSelesai = rs.getDate("tanggal_selesai");


                if (tanggalMulai != null) {
                    staffKoperasi2.setTanggalMulai(tanggalMulai.toLocalDate());
                }

                if (tanggalSelesai != null) {
                    staffKoperasi2.setTanggalSelesai(tanggalSelesai.toLocalDate());
                }

                staffKoperasi.add(staffKoperasi2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return staffKoperasi;
    }


    public static Map<String, Object[]> getAllArrayObject(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM staff_koperasi WHERE status_aktif = 1 ORDER BY id_staff_koperasi ASC";
        Map<String, Object[]> listStaffKoperasi = new TreeMap<String, Object[]>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[] object = new Object[6];
                object[0] = rs.getInt("id_staff_koperasi");
                object[1] = rs.getDate("tanggal_mulai");
                object[2] = rs.getDate("tanggal_selesai");
                object[3] = rs.getInt("id_departemen");
                listStaffKoperasi.put(String.valueOf(i), object);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listStaffKoperasi;
    }

    public static void save(Connection con, StaffKoperasi staffKoperasi){
        PreparedStatement statement = null;
        String query = "INSERT INTO public.staff_koperasi(tanggal_mulai, tanggal_selesai, id_departemen) VALUES (?, ?, ?)";
        try {
            statement = con.prepareStatement(query);
            statement.setDate(1, Date.valueOf(staffKoperasi.getTanggalMulai()));
            statement.setDate(2, Date.valueOf(staffKoperasi.getTanggalSelesai()));
            statement.setInt(3, staffKoperasi.getIdDepartemen());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void update(Connection con, StaffKoperasi staffKoperasi) {
        PreparedStatement statement = null;
        String query = "UPDATE staff_koperasi SET tanggal_mulai = ?, tanggal_selesai = ?, id_departemen = ? WHERE id_staff_koperasi = ?";
        try {
            statement = con.prepareStatement(query);
            statement.setDate(1, Date.valueOf(staffKoperasi.getTanggalMulai()));
            statement.setDate(2, Date.valueOf(staffKoperasi.getTanggalSelesai()));
            statement.setInt(3, staffKoperasi.getIdDepartemen());
            statement.setInt(4, staffKoperasi.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(statement);
        }
    }

    public static void delete(Connection connection, StaffKoperasi staffKoperasi) {
        PreparedStatement ps = null;
        String query = "UPDATE staff_koperasi SET status_aktif = 0 WHERE id_staff_koperasi= ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, staffKoperasi.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps);
        }
    }

    public static ArrayList<StaffKoperasi> getRangeStaff(Connection con) {
        String query = "SELECT id_staff_koperasi, tanggal_mulai, tanggal_selesai, " +
                "CONCAT(EXTRACT(YEAR FROM AGE(tanggal_selesai, tanggal_mulai)), ' tahun ', " +
                "EXTRACT(MONTH FROM AGE(tanggal_selesai, tanggal_mulai)), ' bulan ', " +
                "EXTRACT(DAY FROM AGE(tanggal_selesai, tanggal_mulai)), ' hari') AS durasi " +
                "FROM staff_koperasi " +
                "WHERE status_aktif = 1 " +
                "ORDER BY tanggal_selesai - tanggal_mulai DESC;";

        ArrayList<StaffKoperasi> categoryStats = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_staff_koperasi");

                // Memeriksa apakah tanggal tidak null sebelum mengonversi ke LocalDate
                LocalDate startDate = null;
                LocalDate endDate = null;

                Date startSqlDate = rs.getDate("tanggal_mulai");
                if (startSqlDate != null) {
                    startDate = startSqlDate.toLocalDate();
                }

                Date endSqlDate = rs.getDate("tanggal_selesai");
                if (endSqlDate != null) {
                    endDate = endSqlDate.toLocalDate();
                }
                else {
                    // Handle case where 'tanggal_selesai' is null
                    // Misalnya, Anda bisa mengatur endDate menjadi null atau nilai default lainnya
                    endDate = null; // Atau bisa diatur ke LocalDate.MIN atau LocalDate.MAX, tergantung kebutuhan aplikasi Anda
                }

                String duration = rs.getString("durasi");

                StaffKoperasi staff = new StaffKoperasi(id, startDate, endDate, duration);
                categoryStats.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving staff data", e);
        }

        return categoryStats;
    }


}
