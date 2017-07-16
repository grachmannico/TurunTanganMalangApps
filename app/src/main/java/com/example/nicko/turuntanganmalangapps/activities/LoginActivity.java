package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email, edt_pass;
    private Button btn_login, btn_register;

    private String email, password, token, tipe_pengguna, status;
    private String email_pengguna, nama_pengguna, pangkat_divisi, divisi;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        session = new Session(this);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_pass = (EditText) findViewById(R.id.edt_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
                email = edt_email.getText().toString();
                password = edt_pass.getText().toString();
//                Toast.makeText(getApplicationContext(), session.getURL(), Toast.LENGTH_LONG).show();
                new JSONParser(getApplicationContext());
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new Login().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if (session.loggedin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class Login extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.login(email, password, token);
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        status = jsonObject.getString("status");
                        if (!status.equals("gagal")) {
                            tipe_pengguna = jsonObject.getString("tipe_pengguna");
                            if (tipe_pengguna.equals("donatur")) {
                                email_pengguna = jsonObject.getString("email");
                                nama_pengguna = jsonObject.getString("nama");
                            } else if (tipe_pengguna.equals("relawan")) {
                                email_pengguna = jsonObject.getString("email");
                                nama_pengguna = jsonObject.getString("nama");
                                pangkat_divisi = jsonObject.getString("pangkat_divisi");
                                divisi = jsonObject.getString("divisi");
                            }
                        }
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
            if (status.equals("sukses") && tipe_pengguna.equals("relawan")) {
                session.setLoggedIn(true);
                session.setTipePengguna(tipe_pengguna);
                session.setEmail(email_pengguna);
                session.setNama(nama_pengguna);
                session.setPangkatDivisi(pangkat_divisi);
                session.setDivisi(divisi);
                Toast.makeText(getApplicationContext(), "Login Berhasil, Selamat Datang " + session.getNama(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (status.equals("sukses") && tipe_pengguna.equals("donatur")) {
                session.setLoggedIn(true);
                session.setTipePengguna(tipe_pengguna);
                session.setNama(nama_pengguna);
                session.setEmail(email_pengguna);
                if (session.getInvoice() == null || session.getInvoice().equals("null")) {
                    session.setInvoice("null");
                }
                Toast.makeText(getApplicationContext(), "Login Berhasil, Selamat Datang " + session.getNama(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Login Gagal, Silahkan Coba Kembali", Toast.LENGTH_LONG).show();
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
