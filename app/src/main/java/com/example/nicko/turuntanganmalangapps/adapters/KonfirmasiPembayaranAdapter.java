package com.example.nicko.turuntanganmalangapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Pembayaran;
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;

import java.util.List;

/**
 * Created by nicko on 6/12/2017.
 */

public class KonfirmasiPembayaranAdapter extends ArrayAdapter<Pembayaran> {
    List<Pembayaran> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public KonfirmasiPembayaranAdapter(Context context, List<Pembayaran> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public Pembayaran getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_konfirmasi_pembayaran, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Pembayaran item = getItem(position);

        vh.txt_invoice_pembayaran.setText("ID Invoice:\n" + item.getId_invoice());
        vh.txt_tanggal_pembelian.setText(item.getTanggal_pembelian());
        vh.txt_total_tagihan_pembelian.setText("Total Pembayaran:\n" + NumberFormatter.money(item.getTotal_tagihan()));

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_invoice_pembayaran;
        public final TextView txt_tanggal_pembelian;
        public final TextView txt_total_tagihan_pembelian;


        private ViewHolder(LinearLayout rootView, TextView txt_invoice_pembayaran, TextView txt_tanggal_pembelian, TextView txt_total_tagihan_pembelian) {
            this.rootView = rootView;
            this.txt_invoice_pembayaran = txt_invoice_pembayaran;
            this.txt_tanggal_pembelian = txt_tanggal_pembelian;
            this.txt_total_tagihan_pembelian = txt_total_tagihan_pembelian;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_invoice_pembayaran = (TextView) rootView.findViewById(R.id.txt_invoice_pembayaran);
            TextView txt_tanggal_pembelian = (TextView) rootView.findViewById(R.id.txt_tanggal_pembelian);
            TextView txt_total_tagihan_pembelian = (TextView) rootView.findViewById(R.id.txt_total_tagihan_pembelian);
            return new ViewHolder(rootView, txt_invoice_pembayaran, txt_tanggal_pembelian, txt_total_tagihan_pembelian);
        }
    }
}
