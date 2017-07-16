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
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.DetailKegiatanDiikutiActivity;
import com.example.nicko.turuntanganmalangapps.adapters.KegiatanAdapter;
import com.example.nicko.turuntanganmalangapps.adapters.KegiatanDiikutiAdapter;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
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

public class KegiatanDiikutiFragment extends Fragment {

    private ListView listView;
    private ArrayList<Kegiatan> list;
    private KegiatanDiikutiAdapter adapter;

    private Session session;
    private String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kegiatan_diikuti, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Kegiatan Yang Diikuti");

        session = new Session(getActivity());
        email = session.getEmail();

        listView = (ListView) getActivity().findViewById(R.id.list_kegiatan_diikuti);
        list = new ArrayList<>();
        adapter = new KegiatanDiikutiAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
            new List_Kegiatan_Diikuti().execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailKegiatanDiikutiActivity.class);
                    intent.putExtra("id_kegiatan", list.get(position).getId_kegiatan());
                    intent.putExtra("status_kegiatan", list.get(position).getStatus_kegiatan());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    class List_Kegiatan_Diikuti extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

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
            JSONArray jsonArray = JSONParser.list_kegiatan_diikuti(email);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Kegiatan model = new Kegiatan(getActivity().getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            int id_kegiatan = Integer.parseInt(innerObject.getString("id_kegiatan"));
                            String nama_kegiatan = innerObject.getString("nama_kegiatan");
                            String banner = innerObject.getString("banner");
                            String status_kegiatan = innerObject.getString("status_kegiatan");

                            model.setId_kegiatan(id_kegiatan);
                            model.setNama_kegiatan(nama_kegiatan);
                            model.setBanner(banner);
                            model.setStatus_kegiatan(status_kegiatan);
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
