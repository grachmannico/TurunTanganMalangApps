package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.adapters.BalasanFeedbackAdapter;
import com.example.nicko.turuntanganmalangapps.models.Feedback;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BalasanFeedbackActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Feedback> list;
    private BalasanFeedbackAdapter adapter;

    private TextView txt_komentar_dibalas, txt_nama_pengirim_dibalas, txt_null_feedback_balasan;
    private EditText edt_feedback_balasan;
    private Button btn_kirim_balasan;

    private String email, tipe_pengguna, id_feedback_kegiatan, komentar, nama, balasan, status;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balasan_feedback);

        session = new Session(this);
        email = session.getEmail();
        tipe_pengguna = session.getTipePengguna();
        id_feedback_kegiatan = getIntent().getExtras().getString("id_feedback_kegiatan");
        komentar = getIntent().getExtras().getString("komentar");
        nama = getIntent().getExtras().getString("nama");

        txt_komentar_dibalas = (TextView) findViewById(R.id.txt_komentar_dibalas);
        txt_nama_pengirim_dibalas = (TextView) findViewById(R.id.txt_nama_pengirim_dibalas);
        txt_null_feedback_balasan = (TextView) findViewById(R.id.txt_null_feedback_balasan);
        edt_feedback_balasan = (EditText) findViewById(R.id.edt_feedback_balasan);
        btn_kirim_balasan = (Button) findViewById(R.id.btn_kirim_balasan);

        txt_nama_pengirim_dibalas.setText("Nama: " + nama);
        txt_komentar_dibalas.setText("Komentar: " + komentar);

        listView = (ListView) findViewById(R.id.list_feedback_balasan);
        list = new ArrayList<>();
        adapter = new BalasanFeedbackAdapter(this, list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Lihat_Balasan_Feedback().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_kirim_balasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balasan = edt_feedback_balasan.getText().toString();
                if (balasan.equals("") || balasan.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Isi Kolom Komentar", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Kirim_Balasan_Feedback().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    class Lihat_Balasan_Feedback extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BalasanFeedbackActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Feedback Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.lihat_balasan_feedback(id_feedback_kegiatan, tipe_pengguna);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Feedback model = new Feedback();
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            String nama = innerObject.getString("nama");
                            String komentar = innerObject.getString("komentar");

                            model.setId_feedback_kegiatan(Integer.parseInt(id_feedback_kegiatan));
                            model.setNama(nama);
                            model.setKomentar(komentar);
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
//                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
                listView.setVisibility(View.GONE);
                txt_null_feedback_balasan.setVisibility(View.VISIBLE);
            }
        }
    }

    class Kirim_Balasan_Feedback extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(BalasanFeedbackActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengirim Balasan Feedback");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.kirim_balasan_feedback(id_feedback_kegiatan, email, balasan, tipe_pengguna);
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        status = jsonObject.getString("status");
                    } else {
                        status = "jsonNull";
                    }
                } else {
                    status = "jsonNull";
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
            if (status.equals("sukses")) {
                Toast.makeText(getApplicationContext(), "Feedback Terkirim", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BalasanFeedbackActivity.this, BalasanFeedbackActivity.class);
                intent.putExtra("id_feedback_kegiatan", id_feedback_kegiatan);
                intent.putExtra("komentar", komentar);
                intent.putExtra("nama", nama);
                startActivity(intent);
                finish();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Feedback Gagal Terkirim", Toast.LENGTH_LONG).show();
            } else if (status.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Status = kosong", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
