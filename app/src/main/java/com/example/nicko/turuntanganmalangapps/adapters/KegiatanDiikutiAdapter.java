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
 * Created by nicko on 6/11/2017.
 */

public class KegiatanDiikutiAdapter extends ArrayAdapter<Kegiatan> {
    List<Kegiatan> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public KegiatanDiikutiAdapter(Context context, List<Kegiatan> objects) {
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
            View view = mInflater.inflate(R.layout.row_kegiatan_diikuti, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Kegiatan item = getItem(position);

        vh.txt_nama_kegiatan_diikuti.setText(item.getNama_kegiatan());
        vh.txt_status_kegiatan.setText(item.getStatus_kegiatan());
        Picasso.with(context).load(item.getBanner()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.img_kegiatan_diikuti);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView img_kegiatan_diikuti;
        public final TextView txt_nama_kegiatan_diikuti;
        public final TextView txt_status_kegiatan;

        private ViewHolder(RelativeLayout rootView, ImageView img_kegiatan_diikuti, TextView txt_nama_kegiatan_diikuti, TextView txt_status_kegiatan) {
            this.rootView = rootView;
            this.img_kegiatan_diikuti = img_kegiatan_diikuti;
            this.txt_nama_kegiatan_diikuti = txt_nama_kegiatan_diikuti;
            this.txt_status_kegiatan = txt_status_kegiatan;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView img_kegiatan_diikuti = (ImageView) rootView.findViewById(R.id.img_kegiatan_diikuti);
            TextView txt_nama_kegiatan_diikuti = (TextView) rootView.findViewById(R.id.txt_nama_kegiatan_diikuti);
            TextView txt_status_kegiatan = (TextView) rootView.findViewById(R.id.txt_status_kegiatan);
            return new ViewHolder(rootView, img_kegiatan_diikuti, txt_nama_kegiatan_diikuti, txt_status_kegiatan);
        }
    }
}
