package com.example.projekpbobd.beans;

import java.time.LocalDate;

public class Transaksi {
    private int id;
    private LocalDate tglTransaksi;
    private double totalJual;
    private double profit;
    private int kuantitas;
    private int id_staff_koperasi;
    private int id_barang;
    private int jumlahTransaksi;
    private int monthTransaksi;
    private double total_profit;
    private int month;
    private int tahun;

    public Transaksi() {
    }


    public Transaksi(int id, LocalDate tglTransaksi, double totalJual, double profit, int kuantitas, int id_staff_koperasi, int jumlahTransaksi) {
        this.id = id;
        this.tglTransaksi = tglTransaksi;
        this.totalJual = totalJual;
        this.profit = profit;
        this.kuantitas = kuantitas;
        this.id_staff_koperasi = id_staff_koperasi;
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public Transaksi(int idStaff, int jumlahTransaksi) {
        this.id_staff_koperasi = idStaff;
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public Transaksi(LocalDate month, int transactionCount) {
        this.tglTransaksi = month;
        this.jumlahTransaksi = transactionCount;
    }

    public Transaksi(double total_profit, LocalDate tahun) {
        this.total_profit = total_profit;
        this.tglTransaksi = tahun;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(LocalDate tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public double getTotalJual() {
        return totalJual;
    }

    public void setTotalJual(double totalJual) {
        this.totalJual = totalJual;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public int getId_staff_koperasi() {
        return id_staff_koperasi;
    }

    public void setId_staff_koperasi(int id_staff_koperasi) {
        this.id_staff_koperasi = id_staff_koperasi;
    }

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public void setJumlahTransaksi(int jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public int getMonthTransaksi() {
        return monthTransaksi;
    }

    public void setMonthTransaksi(int monthTransaksi) {
        this.monthTransaksi = monthTransaksi;
    }

    public double getTotal_profit() {
        return total_profit;
    }

    public void setTotal_profit(double total_profit) {
        this.total_profit = total_profit;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }
}
