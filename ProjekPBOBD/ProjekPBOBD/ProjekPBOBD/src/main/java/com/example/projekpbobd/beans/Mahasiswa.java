package com.example.projekpbobd.beans;

public class Mahasiswa {
    private int id;
    private String namaDepan;
    private String namaBelakang;
    private String noTelepon;
    private int statusAktif;
    private int idStaffKoperasi;

    public Mahasiswa() {
    }

    public Mahasiswa(int id, String namaDepan, String namaBelakang, String noTelepon, int statusAktif, int idStaffKoperasi) {
        this.id = id;
        this.namaDepan = namaDepan;
        this.namaBelakang = namaBelakang;
        this.noTelepon = noTelepon;
        this.statusAktif = statusAktif;
        this.idStaffKoperasi = idStaffKoperasi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaDepan() {
        return namaDepan;
    }

    public void setNamaDepan(String namaDepan) {
        this.namaDepan = namaDepan;
    }

    public String getNamaBelakang() {
        return namaBelakang;
    }

    public void setNamaBelakang(String namaBelakang) {
        this.namaBelakang = namaBelakang;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public int getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(int statusAktif) {
        this.statusAktif = statusAktif;
    }

    public int getIdStaffKoperasi() {
        return idStaffKoperasi;
    }

    public void setIdStaffKoperasi(int idStaffKoperasi) {
        this.idStaffKoperasi = idStaffKoperasi;
    }
}

