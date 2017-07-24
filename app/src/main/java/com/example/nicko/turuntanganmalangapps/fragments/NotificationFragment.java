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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.DetailKegiatanActivity;
import com.example.nicko.turuntanganmalangapps.sqlite.NotificationAdapter;
import com.example.nicko.turuntanganmalangapps.sqlite.NotificationModel;
import com.example.nicko.turuntanganmalangapps.sqlite.SQLiteHelper;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import java.util.ArrayList;

/**
 * Created by nicko on 7/3/2017.
 */

public class NotificationFragment extends Fragment {

    private ArrayList<NotificationModel> notifModel;
    private NotificationAdapter dataAdapter;
    private SQLiteHelper sqLiteHelper;

    private ListView list_data;
    private Spinner spin_kategori_notif;
    private TextView txt_null_notif;
    private Button btn_cari_notif;

    private String[] array_kategori_notif;

    private Session session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Notification");

        spin_kategori_notif = (Spinner) getActivity().findViewById(R.id.spin_kategori_notif);
        txt_null_notif = (TextView) getActivity().findViewById(R.id.txt_null_notif);
        btn_cari_notif = (Button) getActivity().findViewById(R.id.btn_cari_notif);

        session = new Session(getActivity());
        if (session.getTipePengguna().equals("relawan")) {
            this.array_kategori_notif = new String[]{
                    "Semua Notifikasi", "Kegiatan", "Absensi", "Dokumentasi"
            };
            ArrayAdapter<String> kategori_notif_adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, array_kategori_notif);
            spin_kategori_notif.setAdapter(kategori_notif_adapter);
        } else if (session.getTipePengguna().equals("donatur")) {
            this.array_kategori_notif = new String[]{
                    "Semua Notifikasi", "Kegiatan", "Konfirmasi Donasi", "Konfirmasi Pembayaran", "Dokumentasi", "Monitor Dana"
            };
            ArrayAdapter<String> kategori_notif_adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, array_kategori_notif);
            spin_kategori_notif.setAdapter(kategori_notif_adapter);
        }

        notifModel = new ArrayList<NotificationModel>();

        sqLiteHelper = new SQLiteHelper(getActivity());
        list_data = (ListView) getActivity().findViewById(R.id.list_notifikasi);
        notifModel = sqLiteHelper.getAllRecords();
        if (notifModel.isEmpty()) {
            list_data.setVisibility(View.GONE);
            txt_null_notif.setVisibility(View.VISIBLE);
        } else {
            dataAdapter = new NotificationAdapter(getActivity(), notifModel);
            list_data.setAdapter(dataAdapter);
            registerForContextMenu(list_data);
        }

        btn_cari_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifModel.clear();

                list_data.setVisibility(View.VISIBLE);
                txt_null_notif.setVisibility(View.GONE);

                if (spin_kategori_notif.getSelectedItem().toString().equals("Semua Notifikasi")) {
                    notifModel = sqLiteHelper.getAllRecords();
                } else if (spin_kategori_notif.getSelectedItem().toString().equals("Kegiatan")) {
                    notifModel = sqLiteHelper.getSelectedRecords("MESSAGE_TYPE", "kegiatan");
                } else if (spin_kategori_notif.getSelectedItem().toString().equals("Absensi")) {
                    notifModel = sqLiteHelper.getSelectedRecords("MESSAGE_TYPE", "absensi");
                } else if (spin_kategori_notif.getSelectedItem().toString().equals("Dokumentasi")){
                    notifModel = sqLiteHelper.getSelectedRecords("MESSAGE_TYPE", "dokumentasi");
                } else if (spin_kategori_notif.getSelectedItem().toString().equals("Konfirmasi Donasi")) {
                    notifModel = sqLiteHelper.getSelectedRecords("MESSAGE_TYPE", "konfirmasi donasi");
                } else if (spin_kategori_notif.getSelectedItem().toString().equals("Konfirmasi Pembayaran")) {
                    notifModel = sqLiteHelper.getSelectedRecords("MESSAGE_TYPE", "konfirmasi pembayaran");
                } else if (spin_kategori_notif.getSelectedItem().toString().equals("Monitor Dana")) {
                    notifModel = sqLiteHelper.getSelectedRecords("MESSAGE_TYPE", "monitor dana");
                }

                if (notifModel.isEmpty()) {
                    list_data.setVisibility(View.GONE);
                    txt_null_notif.setVisibility(View.VISIBLE);
                } else {
                    dataAdapter = new NotificationAdapter(getActivity(), notifModel);
                    list_data.setAdapter(dataAdapter);
                    registerForContextMenu(list_data);
                }
            }
        });

        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (notifModel.get(position).getIntent().equals("DetailKegiatanActivity")) {
                    Intent intent = new Intent(getActivity(), DetailKegiatanActivity.class);
                    intent.putExtra("id_kegiatan", notifModel.get(position).getId_target());
                    startActivity(intent);
                }
            }
        });
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
