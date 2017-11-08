package com.example.nicko.turuntanganmalangapps.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;

public class ProfilRelawanActivity extends AppCompatActivity {

    private ImageView img_foto_profil_relawan;
    private Button btn_update_foto_profil, btn_edit_profil_relawan, btn_tgl_lahir, btn_batal_update, btn_update_profil;
    private EditText edt_nama_relawan, edt_pass_relawan, edt_no_hp, edt_alamat;
    private TextView txt_divisi_relawan, txt_tgl_lahir;
    private ScrollView show_edit;
    private Spinner spin_jenis_kelamin;

    private int year = 1990;
    private int month = 1;
    private int day = 1;
    private String[] array_jenis_kelamin;
    ArrayAdapter<String> jenis_kelamin_adapter;
    private String email, tipe_pengguna, status;
    private String foto_profil, nama, divisi, password, no_hp, alamat, tgl_lahir, jenis_kelamin;

    private Calendar calendar;
    private Session session;
    private File image;
    private String image_name;

//    IMAGE CROPPER
    Uri mCropImageUri;
    Uri croppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_relawan);

        this.setTitle(" Profil");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_account_circle);

        session = new Session(this);
        email = session.getEmail();
        tipe_pengguna = session.getTipePengguna();

        img_foto_profil_relawan = (ImageView) findViewById(R.id.img_foto_profil_relawan);
        edt_nama_relawan = (EditText) findViewById(R.id.edt_nama_relawan);
        edt_pass_relawan = (EditText) findViewById(R.id.edt_pass_relawan);
        edt_no_hp = (EditText) findViewById(R.id.edt_no_hp);
        edt_alamat = (EditText) findViewById(R.id.edt_alamat);
        txt_divisi_relawan = (TextView) findViewById(R.id.txt_divisi_relawan);
        txt_tgl_lahir = (TextView) findViewById(R.id.txt_tgl_lahir);
        show_edit = (ScrollView) findViewById(R.id.show_edit);
        spin_jenis_kelamin = (Spinner) findViewById(R.id.spin_jenis_kelamin);
        btn_update_foto_profil = (Button) findViewById(R.id.btn_update_foto_profil);
        btn_edit_profil_relawan = (Button) findViewById(R.id.btn_edit_profil_relawan);
        btn_tgl_lahir = (Button) findViewById(R.id.btn_tgl_lahir);
        btn_batal_update = (Button) findViewById(R.id.btn_batal_update);
        btn_update_profil = (Button) findViewById(R.id.btn_update_profil);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        this.array_jenis_kelamin = new String[]{
                "Laki-laki", "Perempuan"
        };

        jenis_kelamin_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array_jenis_kelamin);
        spin_jenis_kelamin.setAdapter(jenis_kelamin_adapter);

        if (InternetConnection.checkConnection(this.getApplicationContext())) {
            new Profil().execute();

        } else {
            Toast.makeText(this.getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_edit_profil_relawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_edit.setVisibility(View.VISIBLE);
                btn_update_foto_profil.setVisibility(View.VISIBLE);
                btn_edit_profil_relawan.setVisibility(View.GONE);
//                edt_nama_relawan.setEnabled(true);
                edt_nama_relawan.setClickable(true);
                edt_nama_relawan.setCursorVisible(true);
                edt_nama_relawan.setFocusable(true);
                edt_nama_relawan.setFocusableInTouchMode(true);
            }
        });

        btn_batal_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_edit.setVisibility(View.GONE);
                btn_update_foto_profil.setVisibility(View.GONE);
                btn_edit_profil_relawan.setVisibility(View.VISIBLE);
//                edt_nama_relawan.setEnabled(false);
                edt_nama_relawan.setClickable(false);
                edt_nama_relawan.setCursorVisible(false);
                edt_nama_relawan.setFocusable(false);
                edt_nama_relawan.setFocusableInTouchMode(false);
            }
        });

        btn_tgl_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });

        image_name = "imgNull";
        btn_update_foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity();
            }
        });

        btn_update_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = edt_pass_relawan.getText().toString();
                no_hp = edt_no_hp.getText().toString();
                alamat = edt_alamat.getText().toString();

                if (spin_jenis_kelamin.getSelectedItem().toString().equals("Laki-laki")) {
                    jenis_kelamin = "1";
                } else if (spin_jenis_kelamin.getSelectedItem().equals("Perempuan")) {
                    jenis_kelamin = "2";
                }

                tgl_lahir = txt_tgl_lahir.getText().toString();
                if ((password.equals("") || password.isEmpty()) || (no_hp.equals("") || no_hp.isEmpty()) ||
                        (alamat.equals("") || alamat.isEmpty()) || (jenis_kelamin.equals("") || jenis_kelamin.isEmpty()) ||
                        (tgl_lahir.equals("") || tgl_lahir.isEmpty())) {
                    Toast.makeText(getApplicationContext(), "Isi Semua Form Terlebih Dahulu", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Update_Profil().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfilRelawanActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            txt_tgl_lahir.setText(new StringBuilder(new StringBuilder().append(i).append("-").append(i1 + 1).append("-").append(i2)));
        }
    };

    private void startCropImageActivity() {
        CropImage.activity()
                .start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            CropImage.ActivityResult res = CropImage.getActivityResult(data);
            croppedImage = res.getUri();
            String path = croppedImage.getPath();
            image = new File(path);
            image_name = croppedImage.getLastPathSegment();
            img_foto_profil_relawan.setImageURI(croppedImage);
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .setFixAspectRatio(true)
                .setRequestedSize(200,200)
                .start(this);
    }

    class Profil extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProfilRelawanActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Profil");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.profil_pengguna(email, tipe_pengguna);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            JSONObject innerObject = jsonArray.getJSONObject(0);
                            status = "sukses";
                            nama = innerObject.getString("nama");
                            password = innerObject.getString("pass");
                            no_hp = innerObject.getString("no_hp");
                            tgl_lahir = innerObject.getString("tgl_lahir");
                            alamat = innerObject.getString("alamat");
                            foto_profil = innerObject.getString("foto_profil");
                            jenis_kelamin = innerObject.getString("id_jenis_kelamin");
                            divisi = innerObject.getString("pangkat_divisi") + " " + innerObject.getString("divisi");
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
                session.setFoto_profil(foto_profil);
                edt_nama_relawan.setText(nama);
                edt_pass_relawan.setText(password);
                edt_alamat.setText(alamat);
                edt_no_hp.setText(no_hp);
                txt_tgl_lahir.setText(tgl_lahir);
                txt_divisi_relawan.setText(divisi);
//                edt_nama_relawan.setEnabled(false);
                Picasso.with(ProfilRelawanActivity.this).load(session.getURL() + session.getFoto_profil()).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(img_foto_profil_relawan);
                String[] tLahir = tgl_lahir.split("-");
                year = Integer.parseInt(tLahir[0]);
                month = Integer.parseInt(tLahir[1]) - 1;
                day = Integer.parseInt(tLahir[2]);

                String compareValue = null;
                if (jenis_kelamin.equals("1")) {
                    compareValue = "Laki-laki";
                } else if (jenis_kelamin.equals("2")) {
                    compareValue = "Perempuan";
                }
                int spinnerPostition = jenis_kelamin_adapter.getPosition(compareValue);
                spin_jenis_kelamin.setSelection(spinnerPostition);
            } else {
                Toast.makeText(getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Update_Profil extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProfilRelawanActivity.this);
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Permintaan Sedang Diproses");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = null;
            if (image_name.equals("imgNull")) {
                jsonObject = JSONParser.update_profil(
                        email, tipe_pengguna, jenis_kelamin, nama, password, no_hp, tgl_lahir, alamat
                );
            } else if (!image_name.equals("imgNull")) {
                jsonObject = JSONParser.update_profil_w_photo(
                        email, tipe_pengguna, jenis_kelamin, nama, password, no_hp, tgl_lahir, alamat, image, image_name
                );
            }
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
                Toast.makeText(getApplicationContext(), "Data Telah Diperbarui", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ProfilRelawanActivity.this, ProfilRelawanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (status.equals("gagal")) {
                Toast.makeText(getApplicationContext(), "Gagal Memperbarui Data", Toast.LENGTH_LONG).show();
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
