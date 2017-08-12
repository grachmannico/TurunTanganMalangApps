package com.example.nicko.turuntanganmalangapps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;
import com.example.nicko.turuntanganmalangapps.utils.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class DetailKegiatanDiikutiActivity extends AppCompatActivity {

    private ImageView img_banner_kegiatan_diikuti;
    private TextView txt_nama_kegiatan_detail_diikuti, txt_pesan_ajakan_detail_diikuti, txt_status_kegiatan_diikuti, txt_alamat_diikuti, txt_jumlah_relawan_diikuti,
            txt_jumlah_donasi_diikuti;
    private Button btn_dokumentasi, btn_monitor_dana, btn_feedback, btn_print_lpj;
    private HtmlTextView htmlTextView;

    private String email, id_kegiatan, nama_kegiatan, status;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan_diikuti);

        this.setTitle(" Kegiatan Yang Diikuti");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_people_white);
        session = new Session(this);

        email = session.getEmail();

        img_banner_kegiatan_diikuti = (ImageView) findViewById(R.id.img_banner_kegiatan_diikuti);
        txt_nama_kegiatan_detail_diikuti = (TextView) findViewById(R.id.txt_nama_kegiatan_detail_diikuti);
        txt_pesan_ajakan_detail_diikuti = (TextView) findViewById(R.id.txt_pesan_ajakan_detail_diikuti);
        txt_status_kegiatan_diikuti = (TextView) findViewById(R.id.txt_status_kegiatan_diikuti);
        txt_alamat_diikuti = (TextView) findViewById(R.id.txt_alamat_diikuti);
        txt_jumlah_relawan_diikuti = (TextView) findViewById(R.id.txt_jumlah_relawan_diikuti);
        txt_jumlah_donasi_diikuti = (TextView) findViewById(R.id.txt_jumlah_donasi_diikuti);
        btn_dokumentasi = (Button) findViewById(R.id.btn_dokumentasi);
        btn_monitor_dana = (Button) findViewById(R.id.btn_monitor_dana);
        btn_feedback = (Button) findViewById(R.id.btn_feedback);
        btn_print_lpj = (Button) findViewById(R.id.btn_print_lpj);

        htmlTextView = (HtmlTextView) findViewById(R.id.html_deskripsi_diikuti);

        id_kegiatan = getIntent().getExtras().getString("id_kegiatan");

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

        btn_dokumentasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailKegiatanDiikutiActivity.this, DokumentasiActivity.class);
                intent.putExtra("id_kegiatan", id_kegiatan);
                startActivity(intent);
            }
        });

        btn_print_lpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = session.getURL() + "Report/print_lpj/" + id_kegiatan;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    class Detail_Kegiatan_Diikuti extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        Kegiatan kegiatan = new Kegiatan(getApplicationContext());

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
                        kegiatan.setAlamat(jsonObject.getString("alamat"));
                        kegiatan.setBanner(jsonObject.getString("banner"));
                        kegiatan.setStatus_kegiatan(jsonObject.getString("status_kegiatan"));
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
                txt_nama_kegiatan_detail_diikuti.setText(kegiatan.getNama_kegiatan());
                txt_pesan_ajakan_detail_diikuti.setText(kegiatan.getPesan_ajakan());

                htmlTextView.setHtml("Deskripsi: <br>" + kegiatan.getDeskripsi_kegiatan(),
                        new HtmlHttpImageGetter(htmlTextView));

                txt_jumlah_relawan_diikuti.setText("Jumlah Relawan: \n" + NumberFormatter.number_separator(kegiatan.getJml_relawan()) + " / " + NumberFormatter.number_separator(kegiatan.getMinimal_relawan()));
                txt_jumlah_donasi_diikuti.setText("Jumlah Donasi: \n" + NumberFormatter.money(kegiatan.getDonasi()) + " / " + NumberFormatter.money(kegiatan.getMinimal_donasi()));
                txt_alamat_diikuti.setText("Alamat: \n" + kegiatan.getAlamat());
                txt_status_kegiatan_diikuti.setText("Status Kegiatan: \n" + kegiatan.getStatus_kegiatan());
                Picasso.with(DetailKegiatanDiikutiActivity.this).load(kegiatan.getBanner()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(img_banner_kegiatan_diikuti);
                if (session.getTipePengguna().equals("donatur")) {
                    if (kegiatan.getStatus_kegiatan().equals("Kegiatan Sedang Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                        btn_monitor_dana.setVisibility(View.VISIBLE);
                    } else if (kegiatan.getStatus_kegiatan().equals("Kegiatan Selesai Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                        btn_monitor_dana.setVisibility(View.VISIBLE);
                        btn_feedback.setVisibility(View.VISIBLE);
                        btn_print_lpj.setVisibility(View.VISIBLE);
                    }
                } else if (session.getTipePengguna().equals("relawan")) {
                    if (kegiatan.getStatus_kegiatan().equals("Kegiatan Sedang Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                    } else if (kegiatan.getStatus_kegiatan().equals("Kegiatan Selesai Berjalan")) {
                        btn_dokumentasi.setVisibility(View.VISIBLE);
                        btn_feedback.setVisibility(View.VISIBLE);
                    }
                }
                nama_kegiatan = kegiatan.getNama_kegiatan();
            } else if (status.equals("jsonNull")) {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}
