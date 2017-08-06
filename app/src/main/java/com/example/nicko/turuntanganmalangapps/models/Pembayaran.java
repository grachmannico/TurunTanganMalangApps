package com.example.nicko.turuntanganmalangapps.models;

/**
 * Created by nicko on 6/12/2017.
 */

public class Pembayaran {
    private double total_tagihan;
    private String id_invoice, tanggal_pembelian;

    public double getTotal_tagihan() {
        return total_tagihan;
    }

    public void setTotal_tagihan(double total_tagihan) {
        this.total_tagihan = total_tagihan;
    }

    public String getId_invoice() {
        return id_invoice;
    }

    public void setId_invoice(String id_invoice) {
        this.id_invoice = id_invoice;
    }

    public String getTanggal_pembelian() {
        return tanggal_pembelian;
    }

    public void setTanggal_pembelian(String tanggal_pembelian) {
        this.tanggal_pembelian = tanggal_pembelian;
    }
}
