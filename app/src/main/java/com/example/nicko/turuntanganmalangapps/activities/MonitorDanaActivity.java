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
import com.example.nicko.turuntanganmalangapps.adapters.MonitorDanaAdapter;
import com.example.nicko.turuntanganmalangapps.models.MonitorDana;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MonitorDanaActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<MonitorDana> list;
    private MonitorDanaAdapter adapter;

    private TextView txt_nama_kegiatan_monitor, txt_null_monitor;
    private String id_kegiatan, nama_kegiatan, email;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_dana);

        this.setTitle("Monitor Dana");

        session = new Session(this);
        email = session.getEmail();
        id_kegiatan = getIntent().getExtras().getString("id_kegiatan");
        nama_kegiatan = getIntent().getExtras().getString("nama_kegiatan");

        txt_nama_kegiatan_monitor = (TextView) findViewById(R.id.txt_nama_kegiatan_monitor);
        txt_null_monitor = (TextView) findViewById(R.id.txt_null_monitor);
        txt_nama_kegiatan_monitor.setText(nama_kegiatan);

        listView = (ListView) findViewById(R.id.list_monitor_dana);
        list = new ArrayList<>();
        adapter = new MonitorDanaAdapter(this, list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Monitor_Dana().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    class Monitor_Dana extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MonitorDanaActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Monitor Dana Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.monitor_dana(email, id_kegiatan);
            try {
                if (!jsonArray.isNull(0)) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            MonitorDana model = new MonitorDana();
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            String nama_dana_keluar = innerObject.getString("nama_dana_keluar");
                            String tanggal = innerObject.getString("tanggal");
                            String nominal_dana_keluar = innerObject.getString("nominal_dana_keluar");
                            String keterangan = innerObject.getString("keterangan");

                            model.setNama_dana_keluar(nama_dana_keluar);
                            model.setTanggal(tanggal);
                            model.setNominal_dana_keluar(nominal_dana_keluar);
                            model.setKeterangan(keterangan);
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
//                Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
                txt_nama_kegiatan_monitor.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                txt_null_monitor.setVisibility(View.VISIBLE);
            }
        }
    }
}