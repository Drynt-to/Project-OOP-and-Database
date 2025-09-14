package com.example.projekpbobd.beans;

import java.sql.Date;

public class Transaksi {
    private Integer id_transaksi;
    private Date tanggal_transaksi;
    private Integer total_jual;
    private Double profit;
    private Integer kuantitas;
    private Integer id_staff_koperasi;

    public Transaksi(Integer id_transaksi, Date tanggal_transaksi, int total_jual, Double profit, Integer kuantitas, Integer id_staff_koperasi) {
        this.id_transaksi = id_transaksi;
        this.tanggal_transaksi = tanggal_transaksi;
        this.total_jual = total_jual;
        this.profit = profit;
        this.kuantitas = kuantitas;
        this.id_staff_koperasi = id_staff_koperasi;
    }

    public Integer getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(Integer id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public Date getTanggal_transaksi() {
        return tanggal_transaksi;
    }

    public void setTanggal_transaksi(Date tanggal_transaksi) {
        this.tanggal_transaksi = tanggal_transaksi;
    }

    public int getTotal_jual() {
        return total_jual;
    }

    public void setTotal_jual(int total_jual) {
        this.total_jual = total_jual;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(Integer kuantitas) {
        this.kuantitas = kuantitas;
    }

    public Integer getId_staff_koperasi() {
        return id_staff_koperasi;
    }

    public void setId_staff_koperasi(Integer id_staff_koperasi) {
        this.id_staff_koperasi = id_staff_koperasi;
    }
}
