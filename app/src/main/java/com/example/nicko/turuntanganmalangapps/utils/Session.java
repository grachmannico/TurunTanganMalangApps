package com.example.nicko.turuntanganmalangapps.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nicko on 6/8/2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;
    private String tipe_pengguna, nama, email, pangkat_divisi, divisi, invoice;
    private String MAIN_URL = "http://192.168.43.133:80/ttm/";

    public Session(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(boolean loggedin) {
        editor.putBoolean("loggedInmode", loggedin);
        editor.commit();
    }

    public void setTipePengguna(String tipe_pengguna) {
        this.tipe_pengguna = tipe_pengguna;
        editor.putString("tipe_pengguna", this.tipe_pengguna);
        editor.commit();
    }

    public void setNama(String nama) {
        this.nama = nama;
        editor.putString("nama", this.nama);
        editor.commit();
    }

    public void setEmail(String email) {
        this.email = email;
        editor.putString("email", this.email);
        editor.commit();
    }

    public void setPangkatDivisi(String pangkat_divisi) {
        this.pangkat_divisi = pangkat_divisi;
        editor.putString("pangkat_divisi", this.pangkat_divisi);
        editor.commit();
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
        editor.putString("divisi", this.divisi);
        editor.commit();
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
        editor.putString("invoice", this.invoice);
        editor.commit();
    }

//    public void setURL(String url) {
//        this.MAIN_URL = url;
//        editor.putString("url", this.MAIN_URL);
//        editor.commit();
//    }

    public boolean loggedin() {
        return prefs.getBoolean("loggedInmode", false);
    }

    public String getTipePengguna() {
        return prefs.getString("tipe_pengguna", this.tipe_pengguna);
    }

    public String getNama() {
        return prefs.getString("nama", this.nama);
    }

    public String getEmail() {
        return prefs.getString("email", this.email);
    }

    public String getPangkatDivisi() {
        return prefs.getString("pangkat_divisi", this.pangkat_divisi);
    }

    public String getDivisi() {
        return prefs.getString("divisi", this.divisi);
    }

    public String getInvoice() {
        return prefs.getString("invoice", this.invoice);
    }

    public String getURL() {
        return prefs.getString("url", this.MAIN_URL);
    }
}
