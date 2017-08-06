package com.example.nicko.turuntanganmalangapps.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.MainActivity;
import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.NumberFormatter;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;

public class KonfirmasiDonasiActivity extends AppCompatActivity {

    private TextView txt_nama_kegiatan_donasi_detail, txt_nominal_donasi_detail;
    private Button btn_foto_struk_transfer, btn_konfirmasi_donasi;
    private ImageView img_struk_donasi;

    private File image;
    private String image_name;
    private static final int FILE_SELECT_CODE = 0;
//    IMAGE CROPPER
    Uri mCropImageUri;
    Uri croppedImage;

    private String id_donasi, nama_kegiatan, status;
    private double nominal_donasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_donasi);

        this.setTitle(" Konfirmasi Donasi");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_favorite);

        txt_nama_kegiatan_donasi_detail = (TextView) findViewById(R.id.txt_nama_kegiatan_donasi_detail);
        txt_nominal_donasi_detail = (TextView) findViewById(R.id.txt_nominal_donasi_detail);
        img_struk_donasi = (ImageView) findViewById(R.id.img_struk_donasi);
        btn_foto_struk_transfer = (Button) findViewById(R.id.btn_foto_struk_transfer);
        btn_konfirmasi_donasi = (Button) findViewById(R.id.btn_konfirmasi_donasi);

        id_donasi = getIntent().getExtras().getString("id_donasi");
        nama_kegiatan = getIntent().getExtras().getString("nama_kegiatan");
        nominal_donasi = getIntent().getExtras().getDouble("nominal_donasi");

        txt_nama_kegiatan_donasi_detail.setText("Kegiatan " + nama_kegiatan);
        txt_nominal_donasi_detail.setText(NumberFormatter.money(nominal_donasi));
        img_struk_donasi.setVisibility(View.GONE);

        image_name = "imgNull";
        btn_foto_struk_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showFileChooser();
                startCropImageActivity();
            }
        });

        btn_konfirmasi_donasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_name.equals("imgNull")) {
                    Toast.makeText(getApplicationContext(), "Anda Belum Memilih Struk Transfer Untuk Diunggah", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new Konfirmasi_Donasi().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

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
            img_struk_donasi.setVisibility(View.VISIBLE);
            img_struk_donasi.setImageURI(croppedImage);
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .setFixAspectRatio(true)
                .setRequestedSize(200,200)
                .start(this);
    }

//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        try {
//            startActivityForResult(
//                    Intent.createChooser(intent, "Select a File to Upload"),
//                    FILE_SELECT_CODE);
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case FILE_SELECT_CODE:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    Uri uri = data.getData();
//                    // Get the path
//                    String path = null;
//                    try {
//                        path = KonfirmasiDonasiActivity.getPath(this, uri);
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    // Get the file instance
//                    image = new File(path);
//                    image_name = image.getName();
//                    // Initiate the upload
//                    Toast.makeText(this, image.getPath(), Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public static String getPath(Context context, Uri uri) throws URISyntaxException {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = {"_data"};
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                // Eat it
//            }
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }

    class Konfirmasi_Donasi extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
