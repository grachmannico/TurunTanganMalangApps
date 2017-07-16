package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/13/2017.
 */

public class Feedback {
    private int id_feedback_kegiatan, rating, jml_balasan;
    private String nama, komentar;

    public String getId_feedback_kegiatan() {
        return String.valueOf(id_feedback_kegiatan);
    }

    public void setId_feedback_kegiatan(int id_feedback_kegiatan) {
        this.id_feedback_kegiatan = id_feedback_kegiatan;
    }

    public String getRating() {
        return String.valueOf(rating);
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getJml_balasan() {
        return String.valueOf(jml_balasan);
    }

    public void setJml_balasan(int jml_balasan) {
        this.jml_balasan = jml_balasan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }
}
