package com.example.projekpbobd.beans;

public class Barang {
    private int id;
    private String nama;
    private double hargaPokok;
    private double hargaJual;
    private String kategori;
    private int statusAktif;


    public Barang() {
    }

    public Barang(int id, String nama, double hargaPokok, double hargaJual, String kategori, int statusAktif) {
        this.id = id;
        this.nama = nama;
        this.hargaPokok = hargaPokok;
        this.hargaJual = hargaJual;
        this.kategori = kategori;
        this.statusAktif = statusAktif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getHargaPokok() {
        return hargaPokok;
    }

    public void setHargaPokok(double hargaPokok) {
        this.hargaPokok = hargaPokok;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(int statusAktif) {
        this.statusAktif = statusAktif;
    }
}
