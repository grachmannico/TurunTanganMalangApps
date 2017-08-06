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
import com.example.nicko.turuntanganmalangapps.models.GarageSale;
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicko on 6/11/2017.
 */

public class GarageSaleAdapter extends ArrayAdapter<GarageSale> {
    List<GarageSale> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public GarageSaleAdapter(Context context, List<GarageSale> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public GarageSale getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_garage_sale, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        GarageSale item = getItem(position);

        vh.txt_nama_barang.setText(item.getNama_barang());
        vh.txt_harga.setText(NumberFormatter.money(item.getHarga()));
        Picasso.with(context).load(item.getGambar_barang()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(vh.img_garage_sale);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView img_garage_sale;
        public final TextView txt_nama_barang;
        public final TextView txt_harga;


        private ViewHolder(RelativeLayout rootView, ImageView img_garage_sale, TextView txt_nama_barang, TextView txt_harga) {
            this.rootView = rootView;
            this.img_garage_sale = img_garage_sale;
            this.txt_nama_barang = txt_nama_barang;
            this.txt_harga = txt_harga;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView img_garage_sale = (ImageView) rootView.findViewById(R.id.img_garage_sale);
            TextView txt_nama_barang = (TextView) rootView.findViewById(R.id.txt_nama_barang);
            TextView txt_harga = (TextView) rootView.findViewById(R.id.txt_harga);
            return new ViewHolder(rootView, img_garage_sale, txt_nama_barang, txt_harga);
        }
    }
}
