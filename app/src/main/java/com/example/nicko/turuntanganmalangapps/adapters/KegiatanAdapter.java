package com.example.nicko.turuntanganmalangapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicko on 6/8/2017.
 */

public class KegiatanAdapter extends ArrayAdapter<Kegiatan> {
    List<Kegiatan> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public KegiatanAdapter(Context context, List<Kegiatan> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public Kegiatan getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_kegiatan, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Kegiatan item = getItem(position);

        vh.txt_nama_kegiatan.setText(item.getNama_kegiatan());
        vh.txt_pesan_ajakan.setText(item.getPesan_ajakan());
        Picasso.with(context).load(item.getBanner()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(vh.img_kegiatan);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView img_kegiatan;
        public final TextView txt_nama_kegiatan;
        public final TextView txt_pesan_ajakan;


        private ViewHolder(RelativeLayout rootView, ImageView img_kegiatan, TextView txt_nama_kegiatan, TextView txt_pesan_ajakan) {
            this.rootView = rootView;
            this.img_kegiatan = img_kegiatan;
            this.txt_nama_kegiatan = txt_nama_kegiatan;
            this.txt_pesan_ajakan = txt_pesan_ajakan;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView img_kegiatan = (ImageView) rootView.findViewById(R.id.img_kegiatan);
            TextView txt_nama_kegiatan = (TextView) rootView.findViewById(R.id.txt_nama_kegiatan);
            TextView txt_pesan_ajakan = (TextView) rootView.findViewById(R.id.txt_pesan_ajakan);
            return new ViewHolder(rootView, img_kegiatan, txt_nama_kegiatan, txt_pesan_ajakan);
        }
    }
}
