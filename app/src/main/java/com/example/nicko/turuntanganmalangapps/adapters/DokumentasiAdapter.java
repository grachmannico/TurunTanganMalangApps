package com.example.nicko.turuntanganmalangapps.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Dokumentasi;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicko on 7/17/2017.
 */

public class DokumentasiAdapter extends ArrayAdapter<Dokumentasi> {
    List<Dokumentasi> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public DokumentasiAdapter(Context context, List<Dokumentasi> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public Dokumentasi getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DokumentasiAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_dokumentasi, parent, false);
            vh = DokumentasiAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (DokumentasiAdapter.ViewHolder) convertView.getTag();
        }

        Dokumentasi item = getItem(position);

        vh.txt_tanggal_dokumentasi.setText("Tanggal dokumentasi: " + item.getTanggal());
        vh.txt_deskripsi_dokumentasi.setText(Html.fromHtml(item.getDeskripsi()));
        vh.txt_nama_dokumentasi.setText(item.getNama_dokumentasi());
        Picasso.with(context).load(item.getGambar_dokumentasi()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(vh.img_dokumentasi);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView img_dokumentasi;
        public final TextView txt_tanggal_dokumentasi;
        public final TextView txt_deskripsi_dokumentasi;
        public final TextView txt_nama_dokumentasi;


        private ViewHolder(RelativeLayout rootView, ImageView img_dokumentasi, TextView txt_tanggal_dokumentasi, TextView txt_deskripsi_dokumentasi, TextView txt_nama_dokumentasi) {
            this.rootView = rootView;
            this.img_dokumentasi = img_dokumentasi;
            this.txt_tanggal_dokumentasi = txt_tanggal_dokumentasi;
            this.txt_deskripsi_dokumentasi = txt_deskripsi_dokumentasi;
            this.txt_nama_dokumentasi = txt_nama_dokumentasi;
        }

        public static DokumentasiAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView img_dokumentasi = (ImageView) rootView.findViewById(R.id.img_dokumentasi);
            TextView txt_tanggal_dokumentasi = (TextView) rootView.findViewById(R.id.txt_tanggal_dokumentasi);
            TextView txt_deskripsi_dokumentasi = (TextView) rootView.findViewById(R.id.txt_deskripsi_dokumentasi);
            TextView txt_nama_dokumentasi = (TextView) rootView.findViewById(R.id.txt_nama_dokumentasi);
            return new DokumentasiAdapter.ViewHolder(rootView, img_dokumentasi, txt_tanggal_dokumentasi, txt_deskripsi_dokumentasi, txt_nama_dokumentasi);
        }
    }
}
