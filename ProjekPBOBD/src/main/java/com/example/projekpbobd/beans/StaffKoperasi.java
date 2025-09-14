package com.example.projekpbobd.beans;

import java.time.LocalDate;
import java.util.Date;

public class StaffKoperasi {
    private int id;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private int status_aktif;
    private String durasi;
    private int idDepartemen;


    public StaffKoperasi() {
    }

    public StaffKoperasi(int id, LocalDate tanggalMulai, LocalDate tanggalSelesai, int status_aktif) {
        this.id = id;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.status_aktif = status_aktif;
    }

    public StaffKoperasi(int idStaff, int jumlahTransaksi) {
    }

    public StaffKoperasi(int kategori, LocalDate tglMulai, LocalDate tglSelesai, String durasi) {
        this.id = kategori;
        this.tanggalMulai = tglMulai;
        this.tanggalSelesai = tglSelesai;
        this.durasi = durasi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public int getStatus_aktif() {
        return status_aktif;
    }

    public void setStatus_aktif(int status_aktif) {
        this.status_aktif = status_aktif;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public int getIdDepartemen() {
        return idDepartemen;
    }

    public void setIdDepartemen(int idDepartemen) {
        this.idDepartemen = idDepartemen;
    }
}
