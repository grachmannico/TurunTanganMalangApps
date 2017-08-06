package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/11/2017.
 */

public class Donasi {
    private int id_donasi;
    private double nominal_donasi;
    private String nama_kegiatan, tanggal_donasi;

    public String getId_donasi() {
        return String.valueOf(id_donasi);
    }

    public void setId_donasi(int id_donasi) {
        this.id_donasi = id_donasi;
    }

    public double getNominal_donasi() {
        return nominal_donasi;
    }

    public void setNominal_donasi(double nominal_donasi) {
        this.nominal_donasi = nominal_donasi;
    }

    public String getNama_kegiatan() {
        return nama_kegiatan;
    }

    public void setNama_kegiatan(String nama_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
    }

    public String getTanggal_donasi() {
        return tanggal_donasi;
    }

    public void setTanggal_donasi(String tanggal_donasi) {
        this.tanggal_donasi = tanggal_donasi;
    }

    //    public String getNominal_donasi() {
//        return String.valueOf(((int) nominal_donasi));
//    }
//
//    public void setNominal_donasi(double nominal_donasi) {
//        this.nominal_donasi = nominal_donasi;
//    }
//
//    public String getId_donasi() {
//        return id_donasi;
//    }
//
//    public void setId_donasi(String id_donasi) {
//        this.id_donasi = id_donasi;
//    }
//
//    public String getNama_kegiatan() {
//        return nama_kegiatan;
//    }
//
//    public void setNama_kegiatan(String nama_kegiatan) {
//        this.nama_kegiatan = nama_kegiatan;
//    }
//
//    public String getTanggal_donasi() {
//        return tanggal_donasi;
//    }
//
//    public void setTanggal_donasi(String tanggal_donasi) {
//        this.tanggal_donasi = tanggal_donasi;
//    }
}
