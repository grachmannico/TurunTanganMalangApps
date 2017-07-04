package com.example.nicko.turuntanganmalangapps.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
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
        registerForContextMenu(list_data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_notifikasi) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_sqlite, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.delete) {
            Toast.makeText(getActivity().getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
            NotificationModel n = new NotificationModel();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity());
            n.setId(notifModel.get(info.position).getId());
            sqLiteHelper.deleteRecord(n);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("trigger", "notification");
            startActivity(intent);
            getActivity().finish();
            return true;
        } else {
            return false;
        }
    }
}
