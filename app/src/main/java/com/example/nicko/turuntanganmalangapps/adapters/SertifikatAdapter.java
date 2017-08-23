package com.example.nicko.turuntanganmalangapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.SertifikatRelawan;

import java.util.List;

/**
 * Created by nicko on 8/11/2017.
 */

public class SertifikatAdapter extends ArrayAdapter<SertifikatRelawan> {
    List<SertifikatRelawan> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public SertifikatAdapter(Context context, List<SertifikatRelawan> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public SertifikatRelawan getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_sertifikat, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        SertifikatRelawan item = getItem(position);

        vh.txt_nama_sertif_kegiatan.setText(item.getNama_sertifikat());
        vh.txt_tanggal_sertif_kegiatan.setText("Diterbitkan " + item.getTanggal_terbit());

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final TextView txt_nama_sertif_kegiatan;
        public final TextView txt_tanggal_sertif_kegiatan;


        private ViewHolder(RelativeLayout rootView, TextView txt_nama_sertif_kegiatan, TextView txt_tanggal_sertif_kegiatan) {
            this.rootView = rootView;
            this.txt_nama_sertif_kegiatan = txt_nama_sertif_kegiatan;
            this.txt_tanggal_sertif_kegiatan = txt_tanggal_sertif_kegiatan;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView txt_nama_sertif_kegiatan = (TextView) rootView.findViewById(R.id.txt_nama_sertif_kegiatan);
            TextView txt_tanggal_sertif_kegiatan = (TextView) rootView.findViewById(R.id.txt_tanggal_sertif_kegiatan);
            return new ViewHolder(rootView, txt_nama_sertif_kegiatan, txt_tanggal_sertif_kegiatan);
        }
    }
}
