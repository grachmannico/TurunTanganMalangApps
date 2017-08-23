package com.example.nicko.turuntanganmalangapps.models;

import android.content.Context;

import com.example.nicko.turuntanganmalangapps.utils.Session;

/**
 * Created by nicko on 8/19/2017.
 */

public class SertifikatRelawan {
    private int id_target;
    private String tanggal_terbit, nama_sertifikat, jenis;

    public String getId_target() {
        return String.valueOf(id_target);
    }

    public void setId_target(int id_target) {
        this.id_target = id_target;
    }

    public String getTanggal_terbit() {
        return tanggal_terbit;
    }

    public void setTanggal_terbit(String tanggal_terbit) {
        this.tanggal_terbit = tanggal_terbit;
    }

    public String getNama_sertifikat() {
        return nama_sertifikat;
    }

    public void setNama_sertifikat(String nama_sertifikat) {
        this.nama_sertifikat = nama_sertifikat;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
