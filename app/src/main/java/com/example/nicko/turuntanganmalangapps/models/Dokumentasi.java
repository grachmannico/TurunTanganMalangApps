package com.example.nicko.turuntanganmalangapps.models;

import android.content.Context;

import com.example.nicko.turuntanganmalangapps.utils.Session;

/**
 * Created by nicko on 7/17/2017.
 */

public class Dokumentasi {
    private int id_dokumentasi, id_kegiatan;
    private String nama_dokumentasi, gambar_dokumentasi, deskripsi, tanggal;
    private Context context;
    private Session session;

    public Dokumentasi(Context context) {
        this.context = context;
        session = new Session(this.context);
    }

    public int getId_dokumentasi() {
        return id_dokumentasi;
    }

    public void setId_dokumentasi(int id_dokumentasi) {
        this.id_dokumentasi = id_dokumentasi;
    }

    public int getId_kegiatan() {
        return id_kegiatan;
    }

    public void setId_kegiatan(int id_kegiatan) {
        this.id_kegiatan = id_kegiatan;
    }

    public String getNama_dokumentasi() {
        return nama_dokumentasi;
    }

    public void setNama_dokumentasi(String nama_dokumentasi) {
        this.nama_dokumentasi = nama_dokumentasi;
    }

    public String getGambar_dokumentasi() {
        return gambar_dokumentasi;
    }

    public void setGambar_dokumentasi(String gambar_dokumentasi) {
        this.gambar_dokumentasi = session.getURL() + "uploads/dokumentasi/" + gambar_dokumentasi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
