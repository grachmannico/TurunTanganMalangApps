package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class DonasiActivity extends AppCompatActivity {

    private EditText edt_nominal_donasi;
    private TextView txt_header_rekening, txt_no_rekening;
    private Button btn_donasi_now, btn_daftar_konfirmasi_donasi;

    private String nominal_donasi, id_kegiatan, email, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi);

        this.setTitle(" Donasi");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_favorite);

        edt_nominal_donasi = (EditText) findViewById(R.id.edt_nominal_donasi);
        txt_header_rekening = (TextView) findViewById(R.id.txt_header_rekening);
        txt_no_rekening = (TextView) findViewById(R.id.txt_no_rekening);
        btn_donasi_now = (Button) findViewById(R.id.btn_donasi_now);
        btn_daftar_konfirmasi_donasi = (Button) findViewById(R.id.btn_daftar_konfirmasi_donasi);

        nominal_donasi = edt_nominal_donasi.getText().toString();
        edt_nominal_donasi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!charSequence.toString().equals(nominal_donasi)) {
//                    edt_nominal_donasi.removeTextChangedListener(this);
//
//                    String cleanString = charSequence.toString().replaceAll("[,.]", "");
//
//                    double parsed = Double.parseDouble(cleanString);
//                    String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
//
//                    nominal_donasi = formatted;
//                    edt_nominal_donasi.setText(formatted);
//                    edt_nominal_donasi.setSelection(formatted.length());
//
//                    edt_nominal_donasi.addTextChangedListener(this);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(nominal_donasi)) {
                    edt_nominal_donasi.removeTextChangedListener(this);

                    String replaceable = String.format("[%s,.\\s]", DecimalFormat.getCurrencyInstance().getCurrency().getSymbol());
                    String cleanString = editable.toString().replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                    DecimalFormatSymbols formatNumber = new DecimalFormatSymbols();

                    formatNumber.setCurrencySymbol("");
                    formatNumber.setMonetaryDecimalSeparator(',');
                    formatNumber.setGroupingSeparator('.');

                    formatter.setDecimalFormatSymbols(formatNumber);
                    formatter.setMinimumFractionDigits(0);
                    String formatted = formatter.format((parsed));

                    nominal_donasi = formatted;
                    edt_nominal_donasi.setText(formatted);
                    edt_nominal_donasi.setSelection(formatted.length());
                    edt_nominal_donasi.addTextChangedListener(this);
                }
            }
        });

        btn_donasi_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                nominal_donasi = edt_nominal_donasi.getText().toString();
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
                finish();
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
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
