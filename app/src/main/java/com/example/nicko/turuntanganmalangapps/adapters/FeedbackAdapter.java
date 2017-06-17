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

public class FeedbackAdapter extends ArrayAdapter<Feedback> {
    List<Feedback> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public FeedbackAdapter(Context context, List<Feedback> objects) {
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
            View view = mInflater.inflate(R.layout.row_feedback, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Feedback item = getItem(position);

        vh.txt_komentar.setText(Html.fromHtml(item.getKomentar()));
        vh.txt_tanggal_feedback.setText("");
        vh.txt_rating.setText("Rating: " + item.getRating());
        vh.txt_jumlah_balasan.setText("Jumlah balasan: " + item.getJml_balasan());
        vh.txt_nama_pengirim.setText(item.getNama());
        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_komentar;
        public final TextView txt_tanggal_feedback;
        public final TextView txt_rating;
        public final TextView txt_nama_pengirim;
        public final TextView txt_jumlah_balasan;

        private ViewHolder(LinearLayout rootView, TextView txt_komentar, TextView txt_tanggal_feedback, TextView txt_rating, TextView txt_nama_pengirim, TextView txt_jumlah_balasan) {
            this.rootView = rootView;
            this.txt_komentar = txt_komentar;
            this.txt_tanggal_feedback = txt_tanggal_feedback;
            this.txt_rating = txt_rating;
            this.txt_nama_pengirim = txt_nama_pengirim;
            this.txt_jumlah_balasan = txt_jumlah_balasan;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_komentar = (TextView) rootView.findViewById(R.id.txt_komentar);
            TextView txt_tanggal_feedback = (TextView) rootView.findViewById(R.id.txt_tanggal_feedback);
            TextView txt_rating = (TextView) rootView.findViewById(R.id.txt_rating);
            TextView txt_nama_pengirim = (TextView) rootView.findViewById(R.id.txt_nama_pengirim);
            TextView txt_jumlah_balasan = (TextView) rootView.findViewById(R.id.txt_jumlah_balasan);
            return new ViewHolder(rootView, txt_komentar, txt_tanggal_feedback, txt_rating, txt_nama_pengirim, txt_jumlah_balasan);
        }
    }
}
