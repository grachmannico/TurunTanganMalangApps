package com.example.nicko.turuntanganmalangapps.models;

import android.content.Context;

import com.example.nicko.turuntanganmalangapps.utils.Session;

/**
 * Created by nicko on 6/11/2017.
 */

public class GarageSale {
    private int id_barang_garage_sale, stok_terpesan, qty, id_keranjang_belanja;
    private double harga;
    private String nama_barang, deskripsi, gambar_barang;
    private Context context;
    private Session session;

    public GarageSale (Context context) {
        this.context = context;
        session = new Session(this.context);
    }

    public String getId_barang_garage_sale() {
        return String.valueOf(id_barang_garage_sale);
    }

    public void setId_barang_garage_sale(int id_barang_garage_sale) {
        this.id_barang_garage_sale = id_barang_garage_sale;
    }

    public int getStok_terpesan() {
        return stok_terpesan;
    }

    public void setStok_terpesan(int stok_terpesan) {
        this.stok_terpesan = stok_terpesan;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getId_keranjang_belanja() {
        return id_keranjang_belanja;
    }

    public void setId_keranjang_belanja(int id_keranjang_belanja) {
        this.id_keranjang_belanja = id_keranjang_belanja;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar_barang() {
        return gambar_barang;
    }

    public void setGambar_barang(String gambar_barang) {
        this.gambar_barang = session.getURL() + "uploads/barang_garage_sale/" + gambar_barang;
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

    //    public String getId_barang_garage_sale() {
//        return String.valueOf(id_barang_garage_sale);
//    }
//
//    public void setId_barang_garage_sale(int id_barang_garage_sale) {
//        this.id_barang_garage_sale = id_barang_garage_sale;
//    }
//
//    public String getStok_terpesan() {
//        return String.valueOf(stok_terpesan);
//    }
//
//    public void setStok_terpesan(int stok_terpesan) {
//        this.stok_terpesan = stok_terpesan;
//    }
//
//    public String getQty() {
//        return String.valueOf(qty);
//    }
//
//    public void setQty(int qty) {
//        this.qty = qty;
//    }
//
//    public String getId_keranjang_belanja() {
//        return String.valueOf(id_keranjang_belanja);
//    }
//
//    public void setId_keranjang_belanja(int id_keranjang_belanja) {
//        this.id_keranjang_belanja = id_keranjang_belanja;
//    }
//
//    public String getHarga() {
//        return String.valueOf(((int) harga));
//    }
//
//    public void setHarga(double harga) {
//        this.harga = harga;
//    }
//
//    public String getNama_barang() {
//        return nama_barang;
//    }
//
//    public void setNama_barang(String nama_barang) {
//        this.nama_barang = nama_barang;
//    }
//
//    public String getDeskripsi() {
//        return deskripsi;
//    }
//
//    public void setDeskripsi(String deskripsi) {
//        this.deskripsi = deskripsi;
//    }
//
//    public String getGambar_barang() {
//        return gambar_barang;
//    }
//
//    public void setGambar_barang(String gambar_barang) {
//        this.gambar_barang = session.getURL() + "uploads/barang_garage_sale/" + gambar_barang;
//    }
//
//    public Context getContext() {
//        return context;
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }
//
//    public Session getSession() {
//        return session;
//    }
//
//    public void setSession(Session session) {
//        this.session = session;
//    }
}
