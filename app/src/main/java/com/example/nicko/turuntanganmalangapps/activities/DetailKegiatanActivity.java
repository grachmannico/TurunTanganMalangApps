package com.example.nicko.turuntanganmalangapps.activities;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;
import com.example.nicko.turuntanganmalangapps.utils.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailKegiatanActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView txt_nama_kegiatan_detail, txt_pesan_ajakan_detail, txt_alamat, txt_tanggal_kegiatan, txt_batas_akhir_pendaftaran,
            txt_jumlah_relawan, txt_jumlah_donasi, txt_deskripsi;
    private Button btn_gabung, btn_donasi, btn_dokumentasi_kegiatan;
    private ImageView img_banner_kegiatan;

    private String id_kegiatan, email, status, status_bergabung;

    private Session session;
    private NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(0);

        setContentView(R.layout.activity_detail_kegiatan);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txt_nama_kegiatan_detail = (TextView) findViewById(R.id.txt_nama_kegiatan_detail);
        txt_pesan_ajakan_detail = (TextView) findViewById(R.id.txt_pesan_ajakan_detail);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_tanggal_kegiatan = (TextView) findViewById(R.id.txt_tanggal_kegiatan);
        txt_batas_akhir_pendaftaran = (TextView) findViewById(R.id.txt_batas_akhir_pendaftaran);
        txt_jumlah_relawan = (TextView) findViewById(R.id.txt_jumlah_relawan);
        txt_jumlah_donasi = (TextView) findViewById(R.id.txt_jumlah_donasi);
        txt_deskripsi = (TextView) findViewById(R.id.txt_deskripsi);
        img_banner_kegiatan = (ImageView) findViewById(R.id.img_banner_kegiatan);
        btn_gabung = (Button) findViewById(R.id.btn_gabung);
        btn_donasi = (Button) findViewById(R.id.btn_donasi);
        btn_dokumentasi_kegiatan = (Button) findViewById(R.id.btn_dokumentasi_kegiatan);

        session = new Session(this);
        email = session.getEmail();
        if (session.getTipePengguna().equals("relawan")) {
            btn_gabung.setVisibility(View.VISIBLE);
            btn_gabung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Gabung_Kegiatan().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (session.getTipePengguna().equals("donatur")) {
            btn_donasi.setVisibility(View.VISIBLE);
            btn_donasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailKegiatanActivity.this, DonasiActivity.class);
                    intent.putExtra("id_kegiatan", id_kegiatan);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });
        }

        id_kegiatan = getIntent().getExtras().getString("id_kegiatan");

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Detail_Kegiatan().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    class Detail_Kegiatan extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        Kegiatan kegiatan = new Kegiatan(getApplicationContext());

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetailKegiatanActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Detail Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.detail_kegiatan(id_kegiatan, email);
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        status = "sukses";
                        kegiatan.setNama_kegiatan(jsonObject.getString("nama_kegiatan"));
                        kegiatan.setPesan_ajakan(jsonObject.getString("pesan_ajakan"));
                        kegiatan.setDeskripsi_kegiatan(jsonObject.getString("deskripsi_kegiatan"));
//                        kegiatan.setMinimal_relawan(jsonObject.getString("jumlah_relawan") + " / " + jsonObject.getString("minimal_relawan"));
//                        kegiatan.setMinimal_donasi(jsonObject.getString("jumlah_donasi") + " / " + jsonObject.getString("minimal_donasi"));
                        kegiatan.setJml_relawan(jsonObject.getInt("jumlah_relawan"));
                        kegiatan.setMinimal_relawan(jsonObject.getInt("minimal_relawan"));
                        kegiatan.setDonasi(jsonObject.getDouble("jumlah_donasi"));
                        kegiatan.setMinimal_donasi(jsonObject.getDouble("minimal_donasi"));
                        kegiatan.setTanggal_kegiatan(jsonObject.getString("tanggal_kegiatan"));
                        kegiatan.setBatas_akhir_pendaftaran(jsonObject.getString("batas_akhir_pendaftaran"));
                        kegiatan.setAlamat(jsonObject.getString("alamat"));
                        kegiatan.setBanner(jsonObject.getString("banner"));
                        kegiatan.setLat(jsonObject.getDouble("lat"));
                        kegiatan.setLng(jsonObject.getDouble("lng"));
                        kegiatan.setStatus_kegiatan(jsonObject.getString("status_kegiatan"));
                        status_bergabung = jsonObject.getString("status_bergabung");
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
                txt_nama_kegiatan_detail.setText(kegiatan.getNama_kegiatan());
                txt_pesan_ajakan_detail.setText(kegiatan.getPesan_ajakan());
                txt_deskripsi.setText("Deskripsi: \n" + Html.fromHtml(kegiatan.getDeskripsi_kegiatan()));
//                txt_jumlah_relawan.setText("Jumlah Relawan: " + kegiatan.getMinimal_relawan());
//                txt_jumlah_donasi.setText("Donasi Terkumpul: " + kegiatan.getMinimal_donasi());
                txt_jumlah_relawan.setText("Jumlah Relawan: \n" + NumberFormatter.number_separator(kegiatan.getJml_relawan()) + " / " + NumberFormatter.number_separator(kegiatan.getMinimal_relawan()));
                txt_jumlah_donasi.setText("Donasi Terkumpul: \n" + NumberFormatter.money(kegiatan.getDonasi()) + " / " + NumberFormatter.money(kegiatan.getMinimal_donasi()));
                txt_tanggal_kegiatan.setText("Pelaksanaan Kegiatan: \n" + kegiatan.getTanggal_kegiatan());
                txt_batas_akhir_pendaftaran.setText("Batas Akhir Pendaftaran: \n" + kegiatan.getBatas_akhir_pendaftaran());
                txt_alamat.setText("Alamat: \n" + kegiatan.getAlamat());
                if (kegiatan.getStatus_kegiatan().equals("Kegiatan Sedang Berjalan")) {
                    btn_donasi.setEnabled(false);
                    btn_donasi.setText("Donasi Telah Ditutup");

                    btn_gabung.setEnabled(false);
                    btn_gabung.setText("Pendaftaran Relawan Telah Ditutup");
                } else if (kegiatan.getStatus_kegiatan().equals("Kegiatan Selesai Berjalan")) {
                    btn_donasi.setVisibility(View.GONE);
                    btn_gabung.setVisibility(View.GONE);

                    btn_dokumentasi_kegiatan.setVisibility(View.VISIBLE);
                    btn_dokumentasi_kegiatan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DetailKegiatanActivity.this, DokumentasiActivity.class);
                            intent.putExtra("id_kegiatan", id_kegiatan);
                            startActivity(intent);
                        }
                    });
                }

                LatLng locMarker = new LatLng(kegiatan.getLat(), kegiatan.getLng());
                mMap.addMarker(new MarkerOptions().position(locMarker).title("Lokasi Kegiatan"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(kegiatan.getLat(), kegiatan.getLng()), 15.0f));

                Picasso.with(DetailKegiatanActivity.this).load(kegiatan.getBanner()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(img_banner_kegiatan);

                if (status_bergabung.equals("yes")) {
                    btn_gabung.setEnabled(false);
                    btn_gabung.setText("Anda Telah Bergabung Dalam Kegitan Ini");
                }
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Gabung_Kegiatan extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetailKegiatanActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.gabung_kegiatan(id_kegiatan, email);
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
                Toast.makeText(getApplicationContext(), "Anda telah berhasil bergabung dalam kegiatan ini", Toast.LENGTH_LONG).show();
                btn_gabung.setEnabled(false);
                btn_gabung.setText("Anda telah bergabung dalam kegiatan ini");
            } else if (status.equals("exist")) {
                Toast.makeText(getApplicationContext(), "Anda telah bergabung dalam kegiatan ini", Toast.LENGTH_LONG).show();
                btn_gabung.setEnabled(false);
                btn_gabung.setText("Anda telah bergabung dalam kegiatan ini");
            } else if (status.equals("exp")) {
                Toast.makeText(getApplicationContext(), "Mohon Maaf. Pendaftaran Relawan Telah Ditutup.", Toast.LENGTH_LONG).show();
                btn_gabung.setEnabled(false);
                btn_gabung.setText("Pendaftaran Ditutup");
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
