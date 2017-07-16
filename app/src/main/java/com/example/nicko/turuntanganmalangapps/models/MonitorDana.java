package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/12/2017.
 */

public class MonitorDana {
    private double nominal_dana_keluar;
    private String nama_dana_keluar, tanggal, keterangan;

    public String getNominal_dana_keluar() {
        return String.valueOf(((int) nominal_dana_keluar));
    }

    public void setNominal_dana_keluar(double nominal_dana_keluar) {
        this.nominal_dana_keluar = nominal_dana_keluar;
    }

    public String getNama_dana_keluar() {
        return nama_dana_keluar;
    }

    public void setNama_dana_keluar(String nama_dana_keluar) {
        this.nama_dana_keluar = nama_dana_keluar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
