package com.example.projekpbobd.beans;

public class Departemen {
    private int id;
    private String namaDepartemen;
    private int kepalaDepartemen;
    private String role;
    private int jumlah;

    public Departemen() {
    }

    public Departemen(int id, String namaDepartemen, int kepalaDepartemen, String role) {
        this.id = id;
        this.namaDepartemen = namaDepartemen;
        this.kepalaDepartemen = kepalaDepartemen;
        this.role = role;
    }

    public Departemen(int idDepartemen, String namaDepartemen, int jumlah) {
        this.id = idDepartemen;
        this.namaDepartemen = namaDepartemen;
        this.jumlah = jumlah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaDepartemen() {
        return namaDepartemen;
    }

    public void setNamaDepartemen(String namaDepartemen) {
        this.namaDepartemen = namaDepartemen;
    }

    public int getKepalaDepartemen() {
        return kepalaDepartemen;
    }

    public void setKepalaDepartemen(int kepalaDepartemen) {
        this.kepalaDepartemen = kepalaDepartemen;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
