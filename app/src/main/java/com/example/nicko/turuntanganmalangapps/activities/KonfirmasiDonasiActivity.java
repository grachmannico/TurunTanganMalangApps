package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;

public class KonfirmasiDonasiActivity extends AppCompatActivity {

    private TextView txt_nama_kegiatan_donasi_detail, txt_nominal_donasi_detail;
    private Button btn_foto_struk_transfer, btn_konfirmasi_donasi;

    private File image;
    private String image_name;
    private static final int FILE_SELECT_CODE = 0;

    private String id_donasi, nama_kegiatan, nominal_donasi, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_donasi);

        this.setTitle("Konfirmasi Donasi");

        txt_nama_kegiatan_donasi_detail = (TextView) findViewById(R.id.txt_nama_kegiatan_donasi_detail);
        txt_nominal_donasi_detail = (TextView) findViewById(R.id.txt_nominal_donasi_detail);
        btn_foto_struk_transfer = (Button) findViewById(R.id.btn_foto_struk_transfer);
        btn_konfirmasi_donasi = (Button) findViewById(R.id.btn_konfirmasi_donasi);

        id_donasi = getIntent().getExtras().getString("id_donasi");
        nama_kegiatan = getIntent().getExtras().getString("nama_kegiatan");
        nominal_donasi = getIntent().getExtras().getString("nominal_donasi");

        txt_nama_kegiatan_donasi_detail.setText("Kegiatan " + nama_kegiatan);
        txt_nominal_donasi_detail.setText("Rp." + nominal_donasi);

        btn_foto_struk_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btn_konfirmasi_donasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new Konfirmasi_Donasi().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    String path = null;
                    try {
                        path = KonfirmasiDonasiActivity.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    // Get the file instance
                    image = new File(path);
                    image_name = image.getName();
                    // Initiate the upload
                    Toast.makeText(this, image.getPath(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    class Konfirmasi_Donasi extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(KonfirmasiDonasiActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.konfirmasi_donasi(id_donasi, image, image_name);
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
                Toast.makeText(getApplicationContext(), "Konfirmasi Donasi Sukses", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Terima Kasih, telah melakukan donasi pada kegiatan " + nama_kegiatan, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(KonfirmasiDonasiActivity.this, MainActivity.class);
                intent.putExtra("trigger", "konfirmasi_donasi");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Gagal Konfirmasi Donasi", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else if (status.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Status = kosong", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
