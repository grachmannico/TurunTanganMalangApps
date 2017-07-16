package com.example.nicko.turuntanganmalangapps.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.KonfirmasiDonasiActivity;
import com.example.nicko.turuntanganmalangapps.adapters.KonfirmasiDonasiAdapter;
import com.example.nicko.turuntanganmalangapps.models.Donasi;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nicko on 6/7/2017.
 */

public class ListKonfirmasiDonasiFragment extends Fragment {

    private ListView listView;
    private ArrayList<Donasi> list;
    private KonfirmasiDonasiAdapter adapter;

    private Session session;
    private String email;

    private TextView txt_null_konfirmasi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_konfirmasi_donasi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Daftar Konfirmasi Donasi");

        session = new Session(getActivity());
        email = session.getEmail();

        txt_null_konfirmasi = (TextView) getActivity().findViewById(R.id.txt_null_konfirmasi);
        listView = (ListView) getActivity().findViewById(R.id.list_konfirmasi_donasi);
        list = new ArrayList<>();
        adapter = new KonfirmasiDonasiAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
            new List_Konfirmasi_Donasi().execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Toast.makeText(getActivity().getApplicationContext(), list.get(position).getId_donasi(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), KonfirmasiDonasiActivity.class);
                    intent.putExtra("id_donasi", list.get(position).getId_donasi());
                    intent.putExtra("nama_kegiatan", list.get(position).getNama_kegiatan());
                    intent.putExtra("nominal_donasi", list.get(position).getNominal_donasi());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    class List_Konfirmasi_Donasi extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Konfirmasi Donasi");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.list_konfirmasi_donasi(email);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Donasi model = new Donasi();
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            String id_donasi = innerObject.getString("id_donasi");
                            String nama_kegiatan = innerObject.getString("nama_kegiatan");
                            double nominal_donasi = Double.parseDouble(innerObject.getString("nominal_donasi"));
                            String tanggal_donasi = innerObject.getString("tanggal_donasi");

                            model.setId_donasi(id_donasi);
                            model.setNama_kegiatan(nama_kegiatan);
                            model.setNominal_donasi(nominal_donasi);
                            model.setTanggal_donasi(tanggal_donasi);
                            list.add(model);
                        }
                    }
                }
            } catch (JSONException je) {
                Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                txt_null_konfirmasi.setVisibility(View.VISIBLE);
            }
        }
    }
}
