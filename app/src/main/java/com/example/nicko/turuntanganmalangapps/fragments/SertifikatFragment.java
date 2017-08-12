package com.example.nicko.turuntanganmalangapps.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.adapters.SertifikatAdapter;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nicko on 8/11/2017.
 */

public class SertifikatFragment extends Fragment {
    private ListView listView;
    private ArrayList<Kegiatan> list;
    private SertifikatAdapter adapter;

    private Session session;
    private String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sertifikat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(" Achievement");

        session = new Session(getActivity());
        email = session.getEmail();

        listView = (ListView) getActivity().findViewById(R.id.list_sertifikat);
        list = new ArrayList<>();
        adapter = new SertifikatAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
            new List_Sertifikat().execute();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String url = session.getURL() + "Report/sertifikat_kegiatan/" + email + "/" +list.get(position).getId_kegiatan();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    class List_Sertifikat extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
//        JSONParser jsonParser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Sertifikat");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.achievement(email);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Kegiatan model = new Kegiatan(getActivity().getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            model.setNama_kegiatan(innerObject.getString("nama_kegiatan"));
                            model.setTanggal_kegiatan(innerObject.getString("tanggal_kegiatan_berakhir"));
                            model.setId_kegiatan(innerObject.getInt("id_kegiatan"));
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
                listView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } else if (list.size() == 0) {
                listView.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            }
        }
    }
}
