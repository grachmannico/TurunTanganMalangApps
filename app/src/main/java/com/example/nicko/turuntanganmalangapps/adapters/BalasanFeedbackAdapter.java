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
import com.example.nicko.turuntanganmalangapps.models.Feedback;

import java.util.List;

/**
 * Created by nicko on 6/13/2017.
 */

public class BalasanFeedbackAdapter extends ArrayAdapter<Feedback> {
    List<Feedback> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public BalasanFeedbackAdapter(Context context, List<Feedback> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public Feedback getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_balasan_feedback, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Feedback item = getItem(position);

        vh.txt_komentar_balasan.setText(Html.fromHtml(item.getKomentar()));
        vh.txt_tanggal_feedback_balasan.setText("");
        vh.txt_nama_pengirim_balasan.setText(item.getNama());
        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_komentar_balasan;
        public final TextView txt_tanggal_feedback_balasan;
        public final TextView txt_nama_pengirim_balasan;

        private ViewHolder(LinearLayout rootView, TextView txt_komentar_balasan, TextView txt_tanggal_feedback_balasan, TextView txt_nama_pengirim_balasan) {
            this.rootView = rootView;
            this.txt_komentar_balasan = txt_komentar_balasan;
            this.txt_tanggal_feedback_balasan = txt_tanggal_feedback_balasan;
            this.txt_nama_pengirim_balasan = txt_nama_pengirim_balasan;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_komentar_balasan = (TextView) rootView.findViewById(R.id.txt_komentar_balasan);
            TextView txt_tanggal_feedback_balasan = (TextView) rootView.findViewById(R.id.txt_tanggal_feedback_balasan);
            TextView txt_nama_pengirim_balasan = (TextView) rootView.findViewById(R.id.txt_nama_pengirim_balasan);
            return new ViewHolder(rootView, txt_komentar_balasan, txt_tanggal_feedback_balasan, txt_nama_pengirim_balasan);
        }
    }
}
