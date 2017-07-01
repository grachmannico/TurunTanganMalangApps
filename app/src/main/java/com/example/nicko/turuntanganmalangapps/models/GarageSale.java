package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/11/2017.
 */

public class GarageSale {
    private String id_barang_garage_sale, nama_barang, deskripsi, harga, stok_terpesan, gambar_barang, qty, id_keranjang_belanja;

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setGambar_barang(String gambar_barang) {
        this.gambar_barang = "http://192.168.43.133:80/ttm/uploads/barang_garage_sale/" + gambar_barang;
//        this.gambar_barang = "http://turuntanganmalang.pe.hu/uploads/barang_garage_sale/" + gambar_barang;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setId_barang_garage_sale(String id_barang_garage_sale) {
        this.id_barang_garage_sale = id_barang_garage_sale;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public void setStok_terpesan(String stok_terpesan) {
        this.stok_terpesan = stok_terpesan;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setId_keranjang_belanja(String id_keranjang_belanja) {
        this.id_keranjang_belanja = id_keranjang_belanja;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambar_barang() {
        return gambar_barang;
    }

    public String getHarga() {
        return harga;
    }

    public String getId_barang_garage_sale() {
        return id_barang_garage_sale;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public String getStok_terpesan() {
        return stok_terpesan;
    }

    public String getQty() {
        return qty;
    }

    public String getId_keranjang_belanja() {
        return id_keranjang_belanja;
    }
}
