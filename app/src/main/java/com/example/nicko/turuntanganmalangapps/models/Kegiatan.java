package com.example.nicko.turuntanganmalangapps.models;

import android.content.Context;

import com.example.nicko.turuntanganmalangapps.utils.Session;

/**
 * Created by nicko on 6/8/2017.
 */

public class Kegiatan {
    private int id_kegiatan;
    private double lat, lng;
    private String nama_kegiatan, pesan_ajakan, deskripsi_kegiatan, minimal_relawan, minimal_donasi, alamat, banner, status_kegiatan, tanggal_kegiatan, batas_akhir_pendaftaran;
    private Context context;
    private Session session;

    public Kegiatan (Context context) {
        this.context = context;
        session = new Session(this.context);
    }

    public String getId_kegiatan() {
        return String.valueOf(id_kegiatan);
    }

    public void setId_kegiatan(int id_kegiatan) {
        this.id_kegiatan = id_kegiatan;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getNama_kegiatan() {
        return nama_kegiatan;
    }

    public void setNama_kegiatan(String nama_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
    }

    public String getPesan_ajakan() {
        return pesan_ajakan;
    }

    public void setPesan_ajakan(String pesan_ajakan) {
        this.pesan_ajakan = pesan_ajakan;
    }

    public String getDeskripsi_kegiatan() {
        return deskripsi_kegiatan;
    }

    public void setDeskripsi_kegiatan(String deskripsi_kegiatan) {
        this.deskripsi_kegiatan = deskripsi_kegiatan;
    }

    public String getMinimal_relawan() {
        return minimal_relawan;
    }

    public void setMinimal_relawan(String minimal_relawan) {
        this.minimal_relawan = minimal_relawan;
    }

    public String getMinimal_donasi() {
        return minimal_donasi;
    }

    public void setMinimal_donasi(String minimal_donasi) {
        this.minimal_donasi = minimal_donasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = session.getURL() + "uploads/gambar_kegiatan/" + banner;
    }

    public String getStatus_kegiatan() {
        return status_kegiatan;
    }

    public void setStatus_kegiatan(String status_kegiatan) {
        this.status_kegiatan = status_kegiatan;
    }

    public String getTanggal_kegiatan() {
        return tanggal_kegiatan;
    }

    public void setTanggal_kegiatan(String tanggal_kegiatan) {
        this.tanggal_kegiatan = tanggal_kegiatan;
    }

    public String getBatas_akhir_pendaftaran() {
        return batas_akhir_pendaftaran;
    }

    public void setBatas_akhir_pendaftaran(String batas_akhir_pendaftaran) {
        this.batas_akhir_pendaftaran = batas_akhir_pendaftaran;
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
