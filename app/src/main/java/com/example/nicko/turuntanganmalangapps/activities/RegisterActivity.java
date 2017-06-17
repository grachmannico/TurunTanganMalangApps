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
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_reg_nama, edt_reg_email, edt_reg_pass;
    private Button btn_register_now;

    private String nama, email, password, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        edt_reg_nama = (EditText) findViewById(R.id.edt_reg_nama);
        edt_reg_email = (EditText) findViewById(R.id.edt_reg_email);
        edt_reg_pass = (EditText) findViewById(R.id.edt_reg_pass);
        btn_register_now = (Button) findViewById(R.id.btn_register_now);

        btn_register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = edt_reg_nama.getText().toString();
                email = edt_reg_email.getText().toString();
                password = edt_reg_pass.getText().toString();

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new RegisterActivity.Register().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class Register extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.register(nama, email, password);
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
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Register Berhasil, Silahkan Login", Toast.LENGTH_LONG).show();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Register Gagal, Silahkan Coba Kembali", Toast.LENGTH_LONG).show();
            } else if (status.equals("exist")) {
                Toast.makeText(getApplicationContext(), "Email Telah Digunakan, Gunakan Email Lainnya", Toast.LENGTH_LONG).show();
            } else if (status.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Status = kosong", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")){
                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
