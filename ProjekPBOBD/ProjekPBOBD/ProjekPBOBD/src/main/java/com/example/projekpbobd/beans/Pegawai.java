package com.example.projekpbobd.beans;

public class Pegawai {
    private int id;
    private String namaDepan;
    private String namaBelakang;
    private String noTelepon;
    private int statusAktif;
    private int idStaffKoperasi;

    public Pegawai() {
    }

    public Pegawai(int id, String namaDepan, String namaBelakang, String noTelepon, int statusAktif, int idStaffKoperasi) {
        this.id = id;
        this.namaDepan = namaDepan;
        this.namaBelakang = namaBelakang;
        this.noTelepon = noTelepon;
        this.statusAktif = statusAktif;
        this.idStaffKoperasi = idStaffKoperasi;
    }
}
