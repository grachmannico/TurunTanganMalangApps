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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class DonasiActivity extends AppCompatActivity {

    private EditText edt_nominal_donasi;
    private TextView txt_header_rekening, txt_no_rekening;
    private Button btn_donasi_now, btn_daftar_konfirmasi_donasi;

    private String nominal_donasi, id_kegiatan, email, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi);

        edt_nominal_donasi = (EditText) findViewById(R.id.edt_nominal_donasi);
        txt_header_rekening = (TextView) findViewById(R.id.txt_header_rekening);
        txt_no_rekening = (TextView) findViewById(R.id.txt_no_rekening);
        btn_donasi_now = (Button) findViewById(R.id.btn_donasi_now);
        btn_daftar_konfirmasi_donasi = (Button) findViewById(R.id.btn_daftar_konfirmasi_donasi);

        btn_donasi_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nominal_donasi = edt_nominal_donasi.getText().toString();
                id_kegiatan = getIntent().getExtras().getString("id_kegiatan");
                email = getIntent().getExtras().getString("email");
                if (nominal_donasi.equals("") || nominal_donasi.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Masukkan Nominal Donasi", Toast.LENGTH_LONG).show();
                } else if (nominal_donasi.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Masukkan Nominal Donasi", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Donasi().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_daftar_konfirmasi_donasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonasiActivity.this, MainActivity.class);
                intent.putExtra("trigger", "konfirmasi_donasi");
                startActivity(intent);
            }
        });
    }

    class Donasi extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DonasiActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.donasi(id_kegiatan, email, nominal_donasi);
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
                Toast.makeText(getApplicationContext(), "Donasi Anda Telah Tesimpan", Toast.LENGTH_LONG).show();
                txt_header_rekening.setVisibility(View.VISIBLE);
                txt_no_rekening.setVisibility(View.VISIBLE);
                btn_daftar_konfirmasi_donasi.setVisibility(View.VISIBLE);
            } else if (status.equals("0")) {
                Toast.makeText(getApplicationContext(), "Masukkan Nominal Donasi", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
