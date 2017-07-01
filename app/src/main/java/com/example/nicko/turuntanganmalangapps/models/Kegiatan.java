package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/8/2017.
 */

public class Kegiatan {
    private String id_kegiatan, nama_kegiatan, pesan_ajakan, lat, lng, banner, status_kegiatan;

    public void setBanner(String banner) {
        this.banner = "http://192.168.43.133:80/ttm/uploads/gambar_kegiatan/" + banner;
//        this.banner = "http://turuntanganmalang.pe.hu/uploads/gambar_kegiatan/" + banner;
    }

    public void setId_kegiatan(String id_kegiatan) {
        this.id_kegiatan = id_kegiatan;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setNama_kegiatan(String nama_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
    }

    public void setPesan_ajakan(String pesan_ajakan) {
        this.pesan_ajakan = pesan_ajakan;
    }

    public void setStatus_kegiatan(String status_kegiatan) {
        this.status_kegiatan = status_kegiatan;
    }

    public String getBanner() {
        return banner;
    }

    public String getId_kegiatan() {
        return id_kegiatan;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getNama_kegiatan() {
        return nama_kegiatan;
    }

    public String getPesan_ajakan() {
        return pesan_ajakan;
    }

    public String getStatus_kegiatan() {
        return status_kegiatan;
    }
}
