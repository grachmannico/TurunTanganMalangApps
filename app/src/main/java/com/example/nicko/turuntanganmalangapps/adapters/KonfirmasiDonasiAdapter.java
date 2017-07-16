package com.example.nicko.turuntanganmalangapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Donasi;

import java.util.List;

/**
 * Created by nicko on 6/11/2017.
 */

public class KonfirmasiDonasiAdapter extends ArrayAdapter<Donasi> {
    List<Donasi> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public KonfirmasiDonasiAdapter(Context context, List<Donasi> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public Donasi getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_konfirmasi_donasi, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Donasi item = getItem(position);

        vh.txt_nama_kegiatan_donasi.setText(item.getNama_kegiatan());
        vh.txt_nominal_donasi.setText(item.getNominal_donasi());
        vh.txt_tanggal_donasi.setText(item.getTanggal_donasi());

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_nama_kegiatan_donasi;
        public final TextView txt_nominal_donasi;
        public final TextView txt_tanggal_donasi;


        private ViewHolder(LinearLayout rootView, TextView txt_nama_kegiatan_donasi, TextView txt_nominal_donasi, TextView txt_tanggal_donasi) {
            this.rootView = rootView;
            this.txt_nama_kegiatan_donasi = txt_nama_kegiatan_donasi;
            this.txt_nominal_donasi = txt_nominal_donasi;
            this.txt_tanggal_donasi = txt_tanggal_donasi;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_nama_kegiatan_donasi = (TextView) rootView.findViewById(R.id.txt_nama_kegiatan_donasi);
            TextView txt_nominal_donasi = (TextView) rootView.findViewById(R.id.txt_nominal_donasi);
            TextView txt_tanggal_donasi = (TextView) rootView.findViewById(R.id.txt_tanggal_donasi);
            return new ViewHolder(rootView, txt_nama_kegiatan_donasi, txt_nominal_donasi, txt_tanggal_donasi);
        }
    }
}
