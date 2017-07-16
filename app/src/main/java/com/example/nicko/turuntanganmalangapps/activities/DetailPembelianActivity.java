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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.adapters.KeranjangBelanjaAdapter;
import com.example.nicko.turuntanganmalangapps.models.GarageSale;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DetailPembelianActivity extends AppCompatActivity {

    private TextView txt_invoice_detail, txt_total_tagihan_detail;
    private Button btn_foto_struk_pembelian, btn_konfirmasi_pembelian;

    private ListView listView;
    private ArrayList<GarageSale> list;
    private KeranjangBelanjaAdapter adapter;

    private Session session;
    private String email, invoice, status;
    private Integer total_tagihan;

    private File image;
    private String image_name;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembelian);

        this.setTitle("Detail Pembelian");

        listView = (ListView) findViewById(R.id.list_keranjang_belanja_detail);
        list = new ArrayList<>();
        adapter = new KeranjangBelanjaAdapter(this, list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        session = new Session(this);
        email = session.getEmail();
        invoice = getIntent().getExtras().getString("id_invoice");

        txt_invoice_detail = (TextView) findViewById(R.id.txt_invoice_detail);
        txt_total_tagihan_detail = (TextView) findViewById(R.id.txt_total_tagihan_detail);

        txt_invoice_detail.setText("ID Invoice: " + invoice);
        total_tagihan = 0;

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Detail_Pembelian().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        image_name = "imgNull";
        btn_foto_struk_pembelian = (Button) findViewById(R.id.btn_foto_struk_pembelian);
        btn_foto_struk_pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btn_konfirmasi_pembelian = (Button) findViewById(R.id.btn_konfirmasi_pembelian);
        btn_konfirmasi_pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_name.equals("imgNull")) {
                    Toast.makeText(getApplicationContext(), "Anda Belum Memilih Struk Transfer Untuk Diunggah", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Konfirmasi_Pembelian().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
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
                        path = DetailPembelianActivity.getPath(this, uri);
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

    class Detail_Pembelian extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DetailPembelianActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("mengunduh Data Detail Pembelian");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.detail_pembelian(invoice);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            GarageSale model = new GarageSale(getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            int id_keranjang_belanja = Integer.parseInt(innerObject.getString("id_keranjang_belanja"));
                            String nama_barang = innerObject.getString("nama_barang");
                            double harga = Double.parseDouble(innerObject.getString("harga"));
                            String gambar_barang = innerObject.getString("gambar_barang");
                            int qty = Integer.parseInt(innerObject.getString("qty"));
                            total_tagihan = total_tagihan + ((int) harga * qty);

                            model.setId_keranjang_belanja(id_keranjang_belanja);
                            model.setNama_barang(nama_barang);
                            model.setHarga(harga);
                            model.setGambar_barang(gambar_barang);
                            model.setQty(qty);
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
                txt_total_tagihan_detail.setText("Total Tagihan: Rp. " + total_tagihan.toString());
            } else {
                Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Konfirmasi_Pembelian extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DetailPembelianActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.konfirmasi_pembelian(invoice, image, image_name);
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
                Toast.makeText(getApplicationContext(), "Konfirmasi Pembayaran Sukses", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Terima Kasih, telah melakukan konfirmasi pembayaran pada invoice " + invoice, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailPembelianActivity.this, MainActivity.class);
                intent.putExtra("trigger", "konfirmasi_pembayaran");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Gagal Konfirmasi Pembayaran", Toast.LENGTH_LONG).show();
            } else if (status.equals("error")) {
                Toast.makeText(getApplicationContext(), "Anda Belum Memilih Struk Transfer Untuk Diunggah", Toast.LENGTH_LONG).show();
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
