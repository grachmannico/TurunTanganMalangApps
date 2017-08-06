package com.example.nicko.turuntanganmalangapps.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.MonitorDana;
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;

import java.util.List;

/**
 * Created by nicko on 6/12/2017.
 */

public class MonitorDanaAdapter extends ArrayAdapter<MonitorDana> {
    List<MonitorDana> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MonitorDanaAdapter(Context context, List<MonitorDana> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public MonitorDana getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_monitor_dana, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MonitorDana item = getItem(position);

        vh.txt_nama_dana_keluar.setText("Perihal:\n" + item.getNama_dana_keluar());
        vh.txt_tanggal_dana_keluar.setText(item.getTanggal());
        vh.txt_nominal_dana_keluar.setText("Dana Keluar:\n" + NumberFormatter.money(item.getNominal_dana_keluar()));
        vh.txt_deskripsi_dana_keluar.setText("Deskripsi:\n" + Html.fromHtml(item.getKeterangan()));

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_nama_dana_keluar;
        public final TextView txt_tanggal_dana_keluar;
        public final TextView txt_nominal_dana_keluar;
        public final TextView txt_deskripsi_dana_keluar;

        private ViewHolder(LinearLayout rootView, TextView txt_nama_dana_keluar, TextView txt_tanggal_dana_keluar, TextView txt_nominal_dana_keluar, TextView txt_deskripsi_dana_keluar) {
            this.rootView = rootView;
            this.txt_nama_dana_keluar = txt_nama_dana_keluar;
            this.txt_tanggal_dana_keluar = txt_tanggal_dana_keluar;
            this.txt_nominal_dana_keluar = txt_nominal_dana_keluar;
            this.txt_deskripsi_dana_keluar = txt_deskripsi_dana_keluar;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_nama_dana_keluar = (TextView) rootView.findViewById(R.id.txt_nama_dana_keluar);
            TextView txt_tanggal_dana_keluar = (TextView) rootView.findViewById(R.id.txt_tanggal_dana_keluar);
            TextView txt_nominal_dana_keluar = (TextView) rootView.findViewById(R.id.txt_nominal_dana_keluar);
            TextView txt_deskripsi_dana_keluar = (TextView) rootView.findViewById(R.id.txt_deskripsi_dana_keluar);
            return new ViewHolder(rootView, txt_nama_dana_keluar, txt_tanggal_dana_keluar, txt_nominal_dana_keluar, txt_deskripsi_dana_keluar);
        }
    }
}
