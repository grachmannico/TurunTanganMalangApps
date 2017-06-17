package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailBarangActivity extends AppCompatActivity {

    private ImageView img_barang;
    private TextView txt_nama_barang_detail, txt_harga_detail, txt_stok, txt_deskripsi_barang;
    private EditText edt_qty;
    private Button btn_qty_min, btn_qty_plus, btn_beli;

    private Session session;

    private Integer qty;
    private String email, invoice, id_barang, status;
    private String id_barang_garage_sale, nama_barang, deskripsi, harga, stok_terpesan, gambar_barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        img_barang = (ImageView) findViewById(R.id.img_barang);
        txt_nama_barang_detail = (TextView) findViewById(R.id.txt_nama_barang_detail);
        txt_harga_detail = (TextView) findViewById(R.id.txt_harga_detail);
        txt_stok = (TextView) findViewById(R.id.txt_stok);
        txt_deskripsi_barang = (TextView) findViewById(R.id.txt_deskripsi_barang);
        edt_qty = (EditText) findViewById(R.id.edt_qty);
        btn_qty_min = (Button) findViewById(R.id.btn_qty_min);
        btn_qty_plus = (Button) findViewById(R.id.btn_qty_plus);
        btn_beli = (Button) findViewById(R.id.btn_beli);

        session = new Session(this);
        email = session.getEmail();
        if (session.getInvoice() == null) {
            invoice = "";
        } else {
            invoice = session.getInvoice();
        }
        id_barang = getIntent().getExtras().getString("id_barang");
//        Toast.makeText(getApplicationContext(), "Cek email: " + email, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), "Cek id barang: " + id_barang, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), "Cek Invoice: " + invoice, Toast.LENGTH_LONG).show();

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Detail_Barang().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        edt_qty.setText("0");
        btn_qty_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(edt_qty.getText().toString());
                if (qty == 0) {
                    edt_qty.setText("0");
                } else {
                    qty = qty - 1;
                    edt_qty.setText(qty.toString());
                }
            }
        });

        btn_qty_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = Integer.parseInt(edt_qty.getText().toString());
                if (qty >= Integer.parseInt(stok_terpesan)) {
                    // Do Nothing
                } else {
                    qty = qty + 1;
                    edt_qty.setText(qty.toString());
                }
            }
        });

        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Beli_Barang().execute();
            }
        });
    }

    class Detail_Barang extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DetailBarangActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Detail Barang Garage Sale");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.detail_barang(email, invoice, id_barang);
            try {
                if (!jsonArray.isNull(0)) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            JSONObject innerObject = jsonArray.getJSONObject(0);
                            status = "sukses";
                            id_barang_garage_sale = innerObject.getString("id_barang_garage_sale");
                            nama_barang = innerObject.getString("nama_barang");
                            deskripsi = innerObject.getString("deskripsi");
                            harga = innerObject.getString("harga");
                            stok_terpesan = innerObject.getString("stok_terpesan");
                            gambar_barang = "http://192.168.43.133:80/ttm/uploads/barang_garage_sale/" + innerObject.getString("gambar_barang");
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
            if (status.equals("sukses")) {
                txt_nama_barang_detail.setText(nama_barang);
                txt_harga_detail.setText("Rp. " + harga);
                txt_deskripsi_barang.setText("Deskripsi: \n" + Html.fromHtml(deskripsi));
                txt_stok.setText("Stok: " + stok_terpesan);
                Picasso.with(DetailBarangActivity.this).load(gambar_barang).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(img_barang);
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Beli_Barang extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetailBarangActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.beli_barang(email, invoice, id_barang, qty.toString());
            session = new Session(getApplicationContext());
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        status = jsonObject.getString("status");
                        invoice = jsonObject.getString("invoice");
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
                Toast.makeText(getApplicationContext(), invoice, Toast.LENGTH_LONG).show();
                if (session.getInvoice() == null) {
                    session.setInvoice(invoice);
                }
                Intent intent = new Intent(DetailBarangActivity.this, KeranjangBelanjaActivity.class);
                startActivity(intent);
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Status Gagal", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}