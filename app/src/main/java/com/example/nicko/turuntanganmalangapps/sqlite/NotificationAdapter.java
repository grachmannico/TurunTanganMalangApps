package com.example.nicko.turuntanganmalangapps.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.R;

import java.util.ArrayList;

/**
 * Created by nicko on 7/2/2017.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {
    public Context context;
    public ArrayList<NotificationModel> list = null;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) { //Constructor adapter
        super(context, R.layout.row_notification, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) { //modify getView()
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_notification, parent, false);

        TextView txt_title = (TextView) rowView.findViewById(R.id.txt_title);
        TextView txt_body = (TextView) rowView.findViewById(R.id.txt_body);
        TextView txt_tanggal_notif = (TextView) rowView.findViewById(R.id.txt_tanggal_notif);

        txt_title.setText(list.get(position).getTitle());
        txt_body.setText(list.get(position).getBody());
        txt_tanggal_notif.setText(list.get(position).getDate_rcv());

        return (rowView);
    }
}
