package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;

public class KeranjangBelanjaActivity extends AppCompatActivity {

    private TextView txt_invoice, txt_total_tagihan, txt_judul_cart, txt_null_cart;
    private Button btn_beli_sekarang, btn_tambah_barang_belanja, btn_batal_beli;

    private ListView listView;
    private ArrayList<GarageSale> list;
    private KeranjangBelanjaAdapter adapter;

    private Session session;
    private String email, invoice, id_keranjang_belanja, status;
    private Integer total_tagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang_belanja);

        this.setTitle("Keranjang Belanja");

        listView = (ListView) findViewById(R.id.list_keranjang_belanja);
        list = new ArrayList<>();
        adapter = new KeranjangBelanjaAdapter(this, list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        session = new Session(this);
        email = session.getEmail();
        if (session.getInvoice() == null || session.getInvoice().equals("null")) {
            invoice = "";
        } else {
            invoice = session.getInvoice();
        }

        txt_invoice = (TextView) findViewById(R.id.txt_invoice);
        txt_judul_cart = (TextView) findViewById(R.id.txt_judul_cart);
        txt_null_cart = (TextView) findViewById(R.id.txt_null_cart);
        txt_total_tagihan = (TextView) findViewById(R.id.txt_total_tagihan);

        txt_invoice.setText("ID Invoice: " + invoice);
        total_tagihan = 0;

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Keranjang_Belanja().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_beli_sekarang = (Button) findViewById(R.id.btn_beli_sekarang);
        btn_batal_beli = (Button) findViewById(R.id.btn_batal_beli);
        btn_tambah_barang_belanja = (Button) findViewById(R.id.btn_tambah_barang_belanja);

        btn_beli_sekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Daftar Barang Belanja Kosong, Silahkan Beli Minimal 1 Item", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Pembelian().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(getApplicationContext(), "Thread dijalankan", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_batal_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new Batal_Beli().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_tambah_barang_belanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeranjangBelanjaActivity.this, MainActivity.class);
                intent.putExtra("trigger", "garage_sale");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_keranjang_belanja) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.hapus_barang) {
            id_keranjang_belanja = list.get(info.position).getId_keranjang_belanja();
            new Hapus_Barang().execute();
            return true;
        } else {
            return false;
        }
    }

    class Keranjang_Belanja extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KeranjangBelanjaActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Keranjang Belanja Garage Sale");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.keranjang_belanja(email, invoice);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            GarageSale model = new GarageSale(getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            int id_keranjang_belanja = Integer.parseInt(innerObject.getString("id_keranjang_belanja"));
                            String nama_barang = innerObject.getString("nama_barang");
                            double harga = Integer.parseInt(innerObject.getString("harga"));
                            String gambar_barang = innerObject.getString("gambar_barang");
                            int qty = Integer.parseInt(innerObject.getString("qty"));
                            total_tagihan = total_tagihan + (((int) harga) * qty);

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
                txt_total_tagihan.setText("Total Tagihan: Rp." + total_tagihan.toString());
            } else {
                if (session.getInvoice().equals("null")) {
                    txt_null_cart.setVisibility(View.VISIBLE);
                    txt_judul_cart.setVisibility(View.GONE);
                    txt_total_tagihan.setVisibility(View.GONE);
                    txt_invoice.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    btn_beli_sekarang.setVisibility(View.GONE);
                    btn_batal_beli.setVisibility(View.GONE);
                    btn_tambah_barang_belanja.setVisibility(View.GONE);
                } else {
                    adapter.notifyDataSetChanged();
                    txt_total_tagihan.setText("Total Tagihan: Rp." + total_tagihan.toString());
                }
            }
        }
    }

    class Hapus_Barang extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(KeranjangBelanjaActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Menghapus Barang Belanjaan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.hapus_barang(email, invoice, id_keranjang_belanja);
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
                Intent intent = new Intent(KeranjangBelanjaActivity.this, KeranjangBelanjaActivity.class);
                startActivity(intent);
                finish();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Hapus Barang Gagal", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Pembelian extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(KeranjangBelanjaActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.pembelian(email, invoice);
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
                session = new Session(getApplicationContext());
                session.setInvoice("null");
                Toast.makeText(getApplicationContext(), "Pembelian Barang Sukses. Segera Lakukan Pembayaran dan Konfirmasi Pembayaran", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(KeranjangBelanjaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Pembelian Barang Gagal", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Batal_Beli extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(KeranjangBelanjaActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.batal_beli(invoice);
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
                session = new Session(getApplicationContext());
                session.setInvoice("null");
                Toast.makeText(getApplicationContext(), "Pembatalan Pembelian Sukses", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(KeranjangBelanjaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Pembelian Barang Gagal", Toast.LENGTH_LONG).show();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
