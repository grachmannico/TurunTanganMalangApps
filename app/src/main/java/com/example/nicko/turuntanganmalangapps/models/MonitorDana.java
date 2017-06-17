package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/12/2017.
 */

public class MonitorDana {
    private String nama_dana_keluar, tanggal, nominal_dana_keluar, keterangan;

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNama_dana_keluar() {
        return nama_dana_keluar;
    }

    public void setNama_dana_keluar(String nama_dana_keluar) {
        this.nama_dana_keluar = nama_dana_keluar;
    }

    public String getNominal_dana_keluar() {
        return nominal_dana_keluar;
    }

    public void setNominal_dana_keluar(String nominal_dana_keluar) {
        this.nominal_dana_keluar = nominal_dana_keluar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
