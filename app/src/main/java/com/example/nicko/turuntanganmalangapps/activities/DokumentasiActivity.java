package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.adapters.DokumentasiAdapter;
import com.example.nicko.turuntanganmalangapps.models.Dokumentasi;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DokumentasiActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Dokumentasi> list;
    private DokumentasiAdapter adapter;

    private TextView txt_nama_kegiatan_dokumentasi, txt_null_dokumentasi;
    private String id_kegiatan, nama_kegiatan, email;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokumentasi);

        this.setTitle(" Dokumentasi Kegiatan");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_description);

        session = new Session(this);
        email = session.getEmail();
        id_kegiatan = getIntent().getExtras().getString("id_kegiatan");

        txt_nama_kegiatan_dokumentasi = (TextView) findViewById(R.id.txt_nama_kegiatan_dokumentasi);
        txt_null_dokumentasi = (TextView) findViewById(R.id.txt_null_dokumentasi);

        listView = (ListView) findViewById(R.id.list_dokumentasi);
        list = new ArrayList<>();
        adapter = new DokumentasiAdapter(this, list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Dokumentasi_Kegiatan().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    class Dokumentasi_Kegiatan extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DokumentasiActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Dokumentasi Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.dokumentasi(id_kegiatan);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Dokumentasi model = new Dokumentasi(getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
//                            String id_dokumentasi = innerObject.getString("id_dokumentasi");
//                            String id_kegiatan = innerObject.getString("id_kegiatan");
//                            String gambar_kegiatan = innerObject.getString("gambar_dokumentasi");
//                            String deskripsi = innerObject.getString("deskripsi");
//                            String tanggal = innerObject.getString("tanggal");
//                            String nama_dokumentasi = innerObject.getString("nama_dokumentasi");
//                            nama_kegiatan = innerObject.getString("nama_kegiatan");

                            model.setGambar_dokumentasi(innerObject.getString("gambar_dokumentasi"));
                            model.setDeskripsi(innerObject.getString("deskripsi"));
                            model.setTanggal(innerObject.getString("tanggal"));
                            model.setNama_dokumentasi(innerObject.getString("nama_dokumentasi"));
                            nama_kegiatan = innerObject.getString("nama_kegiatan");
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
                txt_nama_kegiatan_dokumentasi.setText("Dokumentasi Kegiatan " + nama_kegiatan);
            } else {
                txt_nama_kegiatan_dokumentasi.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                txt_null_dokumentasi.setVisibility(View.VISIBLE);
            }
        }
    }
}
