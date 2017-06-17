package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.adapters.FeedbackAdapter;
import com.example.nicko.turuntanganmalangapps.models.Feedback;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Feedback> list;
    private FeedbackAdapter adapter;

    private TextView txt_nama_kegiatan_feedback, txt_null_feedback;
    private EditText edt_feedback;
    private Spinner spin_rating;
    private Button btn_kirim_feedback;

    private Session session;
    private String id_kegiatan, nama_kegiatan, email, komentar, rating, status;
    private String[] array_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        this.setTitle("Feedback Kegiatan");

        session = new Session(this);
        email = session.getEmail();
        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
        id_kegiatan = getIntent().getExtras().getString("id_kegiatan");
        nama_kegiatan = getIntent().getExtras().getString("nama_kegiatan");

        txt_nama_kegiatan_feedback = (TextView) findViewById(R.id.txt_nama_kegiatan_feedback);
        txt_null_feedback = (TextView) findViewById(R.id.txt_null_feedback);
        edt_feedback = (EditText) findViewById(R.id.edt_feedback);
        spin_rating = (Spinner) findViewById(R.id.spin_rating);
        btn_kirim_feedback = (Button) findViewById(R.id.btn_kirim_feedback);

        txt_nama_kegiatan_feedback.setText(nama_kegiatan);

        this.array_rating = new String[]{
                "5", "4", "3", "2", "1"
        };
        ArrayAdapter<String> status_kegiatan_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array_rating);
        spin_rating.setAdapter(status_kegiatan_adapter);

        listView = (ListView) findViewById(R.id.list_feedback);
        list = new ArrayList<>();
        adapter = new FeedbackAdapter(this, list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Lihat_Feedback().execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(FeedbackActivity.this, BalasanFeedbackActivity.class);
                    intent.putExtra("id_feedback_kegiatan", list.get(position).getId_feedback_kegiatan());
                    intent.putExtra("komentar", list.get(position).getKomentar());
                    intent.putExtra("nama", list.get(position).getNama());
                    startActivity(intent);

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_kirim_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                komentar = edt_feedback.getText().toString();
                rating = spin_rating.getSelectedItem().toString();
                if (InternetConnection.checkConnection(getApplicationContext())) {
//                    Toast.makeText(getApplication(), komentar, Toast.LENGTH_LONG).show();
                    new Kirim_Feedback().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class Lihat_Feedback extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(FeedbackActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Feedback Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.lihat_feedback(id_kegiatan);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Feedback model = new Feedback();
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            String id_feedback_kegiatan = innerObject.getString("id_feedback_kegiatan");
                            String nama = innerObject.getString("nama");
                            String komentar = innerObject.getString("komentar");
                            String rating = innerObject.getString("rating");
                            String jml_balasan = innerObject.getString("jml_balasan");

                            model.setId_feedback_kegiatan(id_feedback_kegiatan);
                            model.setNama(nama);
                            model.setKomentar(komentar);
                            model.setRating(rating);
                            model.setJml_balasan(jml_balasan);
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
                txt_nama_kegiatan_feedback.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                txt_null_feedback.setVisibility(View.VISIBLE);
            }
        }
    }

    class Kirim_Feedback extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(FeedbackActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengirim Feedback");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.kirim_feedback(email, id_kegiatan, komentar, rating);
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
                Intent intent = new Intent(FeedbackActivity.this, FeedbackActivity.class);
                intent.putExtra("id_kegiatan", id_kegiatan);
                intent.putExtra("nama_kegiatan", nama_kegiatan);
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
