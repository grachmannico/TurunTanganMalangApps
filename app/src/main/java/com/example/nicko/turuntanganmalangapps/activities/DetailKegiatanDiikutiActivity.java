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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.models.Kegiatan;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailKegiatanDiikutiActivity extends AppCompatActivity {

    private ImageView img_banner_kegiatan_diikuti;
    private TextView txt_nama_kegiatan_detail_diikuti, txt_pesan_ajakan_detail_diikuti, txt_status_kegiatan_diikuti, txt_alamat_diikuti, txt_jumlah_relawan_diikuti,
            txt_jumlah_donasi_diikuti, txt_deskripsi_diikuti;
    private Button btn_dokumentasi, btn_monitor_dana, btn_feedback;

    private String id_kegiatan, status_kegiatan, nama_kegiatan, pesan_ajakan, deskripsi_kegiatan, jumlah_relawan, jumlah_donasi,
            alamat, banner, status;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan_diikuti);

        this.setTitle("Kegiatan Yang Diikuti");
        session = new Session(this);

        img_banner_kegiatan_diikuti = (ImageView) findViewById(R.id.img_banner_kegiatan_diikuti);
        txt_nama_kegiatan_detail_diikuti = (TextView) findViewById(R.id.txt_nama_kegiatan_detail_diikuti);
        txt_pesan_ajakan_detail_diikuti = (TextView) findViewById(R.id.txt_pesan_ajakan_detail_diikuti);
        txt_status_kegiatan_diikuti = (TextView) findViewById(R.id.txt_status_kegiatan_diikuti);
        txt_alamat_diikuti = (TextView) findViewById(R.id.txt_alamat_diikuti);
        txt_jumlah_relawan_diikuti = (TextView) findViewById(R.id.txt_jumlah_relawan_diikuti);
        txt_jumlah_donasi_diikuti = (TextView) findViewById(R.id.txt_jumlah_donasi_diikuti);
        txt_deskripsi_diikuti = (TextView) findViewById(R.id.txt_deskripsi_diikuti);
        btn_dokumentasi = (Button) findViewById(R.id.btn_dokumentasi);
        btn_monitor_dana = (Button) findViewById(R.id.btn_monitor_dana);
        btn_feedback = (Button) findViewById(R.id.btn_feedback);

        id_kegiatan = getIntent().getExtras().getString("id_kegiatan");
//        status_kegiatan = getIntent().getExtras().getString("status_kegiatan");

//        if (session.getTipePengguna().equals("donatur")) {
//            if (status_kegiatan.equals("Kegiatan Sedang Berjalan")) {
//                btn_dokumentasi.setVisibility(View.VISIBLE);
//                btn_monitor_dana.setVisibility(View.VISIBLE);
//            } else if (status_kegiatan.equals("Kegiatan Selesai Berjalan")) {
//                btn_dokumentasi.setVisibility(View.VISIBLE);
//                btn_monitor_dana.setVisibility(View.VISIBLE);
//                btn_feedback.setVisibility(View.VISIBLE);
//            }
//        } else if (session.getTipePengguna().equals("relawan")) {
//            if (status_kegiatan.equals("Kegiatan Sedang Berjalan")) {
//                btn_dokumentasi.setVisibility(View.VISIBLE);
//            } else if (status_kegiatan.equals("Kegiatan Selesai Berjalan")) {
//                btn_dokumentasi.setVisibility(View.VISIBLE);
//                btn_feedback.setVisibility(View.VISIBLE);
//            }
//        }

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new Detail_Kegiatan_Diikuti().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_monitor_dana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailKegiatanDiikutiActivity.this, MonitorDanaActivity.class);
                intent.putExtra("id_kegiatan", id_kegiatan);
                startActivity(intent);
            }
        });

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailKegiatanDiikutiActivity.this, FeedbackActivity.class);
                intent.putExtra("id_kegiatan", id_kegiatan);
                intent.putExtra("nama_kegiatan", nama_kegiatan);
                startActivity(intent);
            }
        });
    }

    class Detail_Kegiatan_Diikuti extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        Session session;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DetailKegiatanDiikutiActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Detail Kegiatan");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = JSONParser.detail_kegiatan(id_kegiatan);
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        status = "sukses";
                        nama_kegiatan = jsonObject.getString("nama_kegiatan");
                        pesan_ajakan = jsonObject.getString("pesan_ajakan");
                        deskripsi_kegiatan = jsonObject.getString("deskripsi_kegiatan");
                        jumlah_relawan = jsonObject.getString("jumlah_relawan") + " / " + jsonObject.getString("minimal_relawan");
                        jumlah_donasi = jsonObject.getString("jumlah_donasi") + " / " + jsonObject.getString("minimal_donasi");
                        alamat = jsonObject.getString("alamat");
                        banner = "http://192.168.43.133:80/ttm/uploads/gambar_kegiatan/" + jsonObject.getString("banner");
//                        banner = "http://turuntanganmalang.pe.hu/uploads/gambar_kegiatan/" + jsonObject.getString("banner");
                        status_kegiatan = jsonObject.getString("status_kegiatan");
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

                txt_nama_kegiatan_detail_diikuti.setText(nama_kegiatan);
                txt_pesan_ajakan_detail_diikuti.setText(pesan_ajakan);
                txt_deskripsi_diikuti.setText("Deskripsi: \n" + Html.fromHtml(deskripsi_kegiatan));
                txt_jumlah_relawan_diikuti.setText("Jumlah Relawan: " + jumlah_relawan);
                txt_jumlah_donasi_diikuti.setText("Donasi Terkumpul: " + jumlah_donasi);
                txt_alamat_diikuti.setText("Alamat: " + alamat);
                txt_status_kegiatan_diikuti.setText("Status Kegiatan: " + status_kegiatan);
                Picasso.with(DetailKegiatanDiikutiActivity.this).load(banner).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(img_banner_kegiatan_diikuti);
                if (session.getTipePengguna().equals("donatur")) {
                    if (status_kegiatan.equals("Kegiatan Sedang Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                        btn_monitor_dana.setVisibility(View.VISIBLE);
                    } else if (status_kegiatan.equals("Kegiatan Selesai Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                        btn_monitor_dana.setVisibility(View.VISIBLE);
                        btn_feedback.setVisibility(View.VISIBLE);
                    }
                } else if (session.getTipePengguna().equals("relawan")) {
                    if (status_kegiatan.equals("Kegiatan Sedang Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                    } else if (status_kegiatan.equals("Kegiatan Selesai Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                        btn_feedback.setVisibility(View.VISIBLE);
                    }
                }
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
