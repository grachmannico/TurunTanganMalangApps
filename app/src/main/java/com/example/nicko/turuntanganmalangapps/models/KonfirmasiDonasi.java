package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/11/2017.
 */

public class KonfirmasiDonasi {
    private String id_donasi, nama_kegiatan, nominal_donasi, tanggal_donasi;

    public void setId_donasi(String id_donasi) {
        this.id_donasi = id_donasi;
    }

    public void setNama_kegiatan(String nama_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
    }

    public void setNominal_donasi(String nominal_donasi) {
        this.nominal_donasi = nominal_donasi;
    }

    public void setTanggal_donasi(String tanggal_donasi) {
        this.tanggal_donasi = tanggal_donasi;
    }

    public String getId_donasi() {
        return id_donasi;
    }

    public String getNama_kegiatan() {
        return nama_kegiatan;
    }

    public String getNominal_donasi() {
        return nominal_donasi;
    }

    public String getTanggal_donasi() {
        return tanggal_donasi;
    }
}
