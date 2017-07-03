package com.example.nicko.turuntanganmalangapps.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.sqlite.NotificationAdapter;
import com.example.nicko.turuntanganmalangapps.sqlite.NotificationModel;
import com.example.nicko.turuntanganmalangapps.sqlite.SQLiteHelper;

import java.util.ArrayList;

/**
 * Created by nicko on 7/3/2017.
 */

public class NotificationFragment extends Fragment {

    private ArrayList<NotificationModel> notifModel;
    private NotificationAdapter dataAdapter;
    private SQLiteHelper sqLiteHelper;

    private ListView list_data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Notification");

        notifModel = new ArrayList<NotificationModel>();

        sqLiteHelper = new SQLiteHelper(getActivity());
        notifModel = sqLiteHelper.getAllRecords();
        list_data = (ListView) getActivity().findViewById(R.id.list_notifikasi);

        dataAdapter = new NotificationAdapter(getActivity(), notifModel);
        list_data.setAdapter(dataAdapter);
    }
}
