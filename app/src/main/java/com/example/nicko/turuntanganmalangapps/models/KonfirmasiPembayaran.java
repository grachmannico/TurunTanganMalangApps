package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/12/2017.
 */

public class KonfirmasiPembayaran {
    private String id_invoice, tanggal_pembelian, total_tagihan;

    public void setId_invoice(String id_invoice) {
        this.id_invoice = id_invoice;
    }

    public void setTanggal_pembelian(String tanggal_pembelian) {
        this.tanggal_pembelian = tanggal_pembelian;
    }

    public void setTotal_tagihan(String total_tagihan) {
        this.total_tagihan = total_tagihan;
    }

    public String getId_invoice() {
        return id_invoice;
    }

    public String getTanggal_pembelian() {
        return tanggal_pembelian;
    }

    public String getTotal_tagihan() {
        return total_tagihan;
    }
}
