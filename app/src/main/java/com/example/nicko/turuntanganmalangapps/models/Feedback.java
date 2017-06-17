package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/13/2017.
 */

public class Feedback {
    private String id_feedback_kegiatan, nama, komentar, rating, jml_balasan;

    public String getId_feedback_kegiatan() {
        return id_feedback_kegiatan;
    }

    public void setId_feedback_kegiatan(String id_feedback_kegiatan) {
        this.id_feedback_kegiatan = id_feedback_kegiatan;
    }

    public String getJml_balasan() {
        return jml_balasan;
    }

    public void setJml_balasan(String jml_balasan) {
        this.jml_balasan = jml_balasan;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
