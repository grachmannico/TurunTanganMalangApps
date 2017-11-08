package com.example.nicko.turuntanganmalangapps.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;
import com.example.nicko.turuntanganmalangapps.utils.Session;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

public class DetailKegiatanActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, DirectionCallback {

    private GoogleMap mMap;

    private TextView txt_nama_kegiatan_detail, txt_pesan_ajakan_detail, txt_alamat, txt_tanggal_kegiatan, txt_batas_akhir_pendaftaran,
            txt_jumlah_relawan, txt_jumlah_donasi;
    private Button btn_gabung, btn_donasi, btn_dokumentasi_kegiatan, btn_route;
    private ImageView img_banner_kegiatan;
    private HtmlTextView htmlTextView;

    private String id_kegiatan, email, status, status_bergabung;
    private double lat, lng, yLat, yLng = 0;
    private LatLng origin, destination;

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
        img_banner_kegiatan = (ImageView) findViewById(R.id.img_banner_kegiatan);
        btn_gabung = (Button) findViewById(R.id.btn_gabung);
        btn_donasi = (Button) findViewById(R.id.btn_donasi);
        btn_dokumentasi_kegiatan = (Button) findViewById(R.id.btn_dokumentasi_kegiatan);
        btn_route = (Button) findViewById(R.id.btn_route);

        htmlTextView = (HtmlTextView) findViewById(R.id.html_deskripsi);

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

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Detail_Kegiatan().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

//        yLat = -7.952440099999998;
//        yLng = 112.6129181;

        btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Pressed. " + yLat + "/" + yLng, Toast.LENGTH_LONG).show();
                if (yLat == 0 && yLng == 0) {
                    Toast.makeText(getApplicationContext(), "Lokasi Anda Belum Ditemukan. Tunggu Sebentar", Toast.LENGTH_LONG).show();
                } else {
                    origin = new LatLng(yLat, yLng);
                    destination = new LatLng(lat, lng);
                    GoogleDirection.withServerKey("AIzaSyBxygI2SIeOSnDK_kuw2a19WuCuFMX-Ua8")
//                    GoogleDirection.withServerKey(String.valueOf(R.string.google_maps_key))
                            .from(origin)
                            .to(destination)
                            .transportMode(TransportMode.DRIVING)
                            .execute(DetailKegiatanActivity.this);
                }
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        yLat = location.getLatitude();
        yLng = location.getLongitude();
//        Toast.makeText(getApplicationContext(), "Your Location: " + yLat + yLng, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
//        Toast.makeText(getApplicationContext(), "Success with status : " + direction.getStatus(), Toast.LENGTH_LONG).show();
        if (direction.isOK()) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(origin).title("Lokasi Anda"));
            mMap.addMarker(new MarkerOptions().position(destination).title("Lokasi Kegiatan"));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(origin);
            builder.include(destination);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mMap.animateCamera(cu);

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.parseColor("#c0392b")));
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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

                htmlTextView.setHtml("Deskripsi: <br>" + kegiatan.getDeskripsi_kegiatan(),
                        new HtmlHttpImageGetter(htmlTextView));

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
                lat = kegiatan.getLat();
                lng = kegiatan.getLng();

                Picasso.with(DetailKegiatanActivity.this).load(kegiatan.getBanner()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(img_banner_kegiatan);

                if (status_bergabung.equals("yes")) {
                    btn_gabung.setEnabled(false);
                    btn_gabung.setText("Anda Telah Bergabung Dalam Kegitan Ini");
                }

                img_banner_kegiatan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailKegiatanActivity.this, ImageViewerActivity.class);
                        intent.putExtra("the_image", kegiatan.getBanner());
                        startActivity(intent);
                    }
                });
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
