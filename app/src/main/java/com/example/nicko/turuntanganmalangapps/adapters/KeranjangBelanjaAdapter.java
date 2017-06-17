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
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nicko on 6/12/2017.
 */

public class KeranjangBelanjaAdapter extends ArrayAdapter<GarageSale> {
    List<GarageSale> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public KeranjangBelanjaAdapter(Context context, List<GarageSale> objects) {
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
            View view = mInflater.inflate(R.layout.row_keranjang_belanja, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        GarageSale item = getItem(position);

        vh.txt_nama_barang_cart.setText(item.getNama_barang());
        vh.txt_harga_cart.setText("Harga Satuan: Rp. " + item.getHarga());
        vh.txt_qty_cart.setText("Pesanan anda: " + item.getQty());
        Picasso.with(context).load(item.getGambar_barang()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.img_keranjang_belanja);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView img_keranjang_belanja;
        public final TextView txt_nama_barang_cart;
        public final TextView txt_harga_cart;
        public final TextView txt_qty_cart;

        private ViewHolder(RelativeLayout rootView, ImageView img_keranjang_belanja, TextView txt_nama_barang_cart, TextView txt_harga_cart, TextView txt_qty_cart) {
            this.rootView = rootView;
            this.img_keranjang_belanja = img_keranjang_belanja;
            this.txt_nama_barang_cart = txt_nama_barang_cart;
            this.txt_harga_cart = txt_harga_cart;
            this.txt_qty_cart = txt_qty_cart;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView img_keranjang_belanja = (ImageView) rootView.findViewById(R.id.img_keranjang_belanja);
            TextView txt_nama_barang_cart = (TextView) rootView.findViewById(R.id.txt_nama_barang_cart);
            TextView txt_harga_cart = (TextView) rootView.findViewById(R.id.txt_harga_cart);
            TextView txt_qty_cart = (TextView) rootView.findViewById(R.id.txt_qty_cart);
            return new ViewHolder(rootView, img_keranjang_belanja, txt_nama_barang_cart, txt_harga_cart, txt_qty_cart);
        }
    }
}
