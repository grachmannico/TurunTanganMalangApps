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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.DetailKegiatanActivity;
import com.example.nicko.turuntanganmalangapps.adapters.KegiatanAdapter;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nicko on 6/7/2017.
 */

public class KegiatanFragment extends Fragment {

    private ListView listView;
    private ArrayList<Kegiatan> list;
    private KegiatanAdapter adapter;

    private Button btn_cari;
    private Spinner spin_status_kegiatan;

    private String[] array_status_kegiatan;
    private String id_status_kegiatan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kegiatan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Turun Tangan Malang");

        listView = (ListView) getActivity().findViewById(R.id.list_kegiatan);
        btn_cari = (Button) getActivity().findViewById(R.id.btn_cari);
        spin_status_kegiatan = (Spinner) getActivity().findViewById(R.id.spin_status_kegiatan);

        list = new ArrayList<>();
        adapter = new KegiatanAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        this.array_status_kegiatan = new String[]{
                "Semua Kegiatan", "Kegiatan yang Akan Dilaksanakan", "Kegiatan yang Sedang Dilaksanakan", "Kegiatan yang Selesai Dilaksanakan"
        };
        ArrayAdapter<String> status_kegiatan_adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, array_status_kegiatan);
        spin_status_kegiatan.setAdapter(status_kegiatan_adapter);

        if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
            id_status_kegiatan = "";
            new Tampil_Kegiatan().execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailKegiatanActivity.class);
                    intent.putExtra("id_kegiatan", list.get(position).getId_kegiatan());
                    intent.putExtra("lat", list.get(position).getLat());
                    intent.putExtra("lng", list.get(position).getLng());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();

                if (spin_status_kegiatan.getSelectedItem().toString().equals("Semua Kegiatan")) {
                    id_status_kegiatan = "";
                } else if (spin_status_kegiatan.getSelectedItem().toString().equals("Kegiatan yang Akan Dilaksanakan")) {
                    id_status_kegiatan = "1";
                } else if (spin_status_kegiatan.getSelectedItem().toString().equals("Kegiatan yang Sedang Dilaksanakan")) {
                    id_status_kegiatan = "2";
                } else if (spin_status_kegiatan.getSelectedItem().toString().equals("Kegiatan yang Selesai Dilaksanakan")) {
                    id_status_kegiatan = "3";
                }

                if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
                    new Tampil_Kegiatan().execute();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DetailKegiatanActivity.class);
                            intent.putExtra("id_kegiatan", list.get(position).getId_kegiatan());
                            intent.putExtra("lat", list.get(position).getLat());
                            intent.putExtra("lng", list.get(position).getLng());
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class Tampil_Kegiatan extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
//        JSONParser jsonParser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
//            jsonParser = new JSONParser(getActivity().getApplicationContext());
            JSONArray jsonArray = JSONParser.tampil_kegiatan(id_status_kegiatan);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Kegiatan model = new Kegiatan(getActivity().getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            int id_kegiatan = Integer.parseInt(innerObject.getString("id_kegiatan"));
                            String nama_kegiatan = innerObject.getString("nama_kegiatan");
                            String pesan_ajakan = innerObject.getString("pesan_ajakan");
                            String banner = innerObject.getString("banner");
                            double lat = Double.parseDouble(innerObject.getString("lat"));
                            double lng = Double.parseDouble(innerObject.getString("lng"));

                            model.setId_kegiatan(id_kegiatan);
                            model.setNama_kegiatan(nama_kegiatan);
                            model.setPesan_ajakan(pesan_ajakan);
                            model.setBanner(banner);
                            model.setLat(lat);
                            model.setLng(lng);
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
                Toast.makeText(getActivity().getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            }
        }
    }
}
