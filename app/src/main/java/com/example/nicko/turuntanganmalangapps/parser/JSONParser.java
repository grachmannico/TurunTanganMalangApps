package com.example.nicko.turuntanganmalangapps.parser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by nicko on 6/8/2017.
 */

public class JSONParser {
    private Context context;
    private Session session;
    private static String MAIN_URL;
    //    private static final String MAIN_URL = "http://192.168.43.133:80/ttm/REST_API/";
    //    private static final String MAIN_URL = "http://turuntanganmalang.pe.hu/REST_API/";
    public static final String TAG = "TAG";
    public static final String keyAPI = "448fe35e6645e3c31f5c67f2cb868216";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private static Response response;

    public JSONParser(Context context) {
        this.context = context;
        session = new Session(this.context);
        MAIN_URL = session.getURL() + "REST_API/";
    }

    public static JSONObject register(String nama, String email, String password) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("nama", nama)
                    .add("email", email)
                    .add("password", password)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "register")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject login(String email, String password, String token) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("password", password)
                    .add("token", token)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "auth")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray tampil_kegiatan(String id_status_kegiatan) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("id_status_kegiatan", id_status_kegiatan)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "tampil_kegiatan")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject detail_kegiatan(String id_kegiatan, String email) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("id_kegiatan", id_kegiatan)
                    .add("email", email)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "detail_kegiatan")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject gabung_kegiatan(String id_kegiatan, String email) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("gabung", id_kegiatan)
                    .add("email", email)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "gabung_kegiatan")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject donasi(String id_kegiatan, String email, String nominal_donasi) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("donasi", id_kegiatan)
                    .add("email", email)
                    .add("nominal_donasi", nominal_donasi)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "donasi")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray list_konfirmasi_donasi(String email) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "list_konfirmasi_donasi")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject konfirmasi_donasi(String id_donasi, File image, String image_name) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("struk", image_name, RequestBody.create(MEDIA_TYPE_PNG, image))
                    .addFormDataPart("id_donasi", id_donasi)
                    .addFormDataPart("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "konfirmasi_donasi")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray list_kegiatan_diikuti(String email) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "list_kegiatan_diikuti")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray lihat_garage_sale(String email, String invoice) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("invoice", invoice)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "lihat_garage_sale")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray detail_barang(String email, String invoice, String id_barang_garage_sale) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("invoice", invoice)
                    .add("id_barang_garage_sale", id_barang_garage_sale)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "detail_barang")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject beli_barang(String email, String invoice, String id_barang_garage_sale, String qty) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("invoice", invoice)
                    .add("id_barang_garage_sale", id_barang_garage_sale)
                    .add("qty", qty)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "beli_barang")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray keranjang_belanja(String email, String invoice) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("invoice", invoice)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "keranjang_belanja")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject hapus_barang(String email, String invoice, String id_keranjang_belanja) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("invoice", invoice)
                    .add("id_keranjang_belanja", id_keranjang_belanja)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "hapus_barang")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject pembelian(String email, String invoice) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("invoice", invoice)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "pembelian")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject batal_beli(String invoice) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("invoice", invoice)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "batal_beli")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray list_konfirmasi_pembayaran(String email) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "list_konfirmasi_pembayaran")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray detail_pembelian(String invoice) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("invoice", invoice)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "detail_pembelian")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject konfirmasi_pembelian(String invoice, String alamat_pengiriman, String no_hp_pembeli, File image, String image_name) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("struk", image_name, RequestBody.create(MEDIA_TYPE_PNG, image))
                    .addFormDataPart("invoice", invoice)
                    .addFormDataPart("alamat_pengiriman", alamat_pengiriman)
                    .addFormDataPart("no_hp_pembeli", no_hp_pembeli)
                    .addFormDataPart("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "konfirmasi_pembelian")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray monitor_dana(String email, String id_kegiatan) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("id_kegiatan", id_kegiatan)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "monitor_dana")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray lihat_feedback(String id_kegiatan, String tipe_pengguna) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("id_kegiatan", id_kegiatan)
                    .add("tipe_pengguna", tipe_pengguna)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "lihat_feedback")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject kirim_feedback(String email, String id_kegiatan, String komentar, String rating, String tipe_pengguna) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("id_kegiatan", id_kegiatan)
                    .add("komentar", komentar)
                    .add("rating", rating)
                    .add("tipe_pengguna", tipe_pengguna)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "kirim_feedback")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray lihat_balasan_feedback(String id_feedback_kegiatan, String tipe_pengguna) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("id_feedback_kegiatan", id_feedback_kegiatan)
                    .add("tipe_pengguna", tipe_pengguna)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "lihat_balasan_feedback")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject kirim_balasan_feedback(String id_feedback_kegiatan, String email, String komentar, String tipe_pengguna) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("id_feedback_kegiatan", id_feedback_kegiatan)
                    .add("email", email)
                    .add("komentar", komentar)
                    .add("tipe_pengguna", tipe_pengguna)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "kirim_balasan_feedback")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray dokumentasi(String id_kegiatan) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("id_kegiatan", id_kegiatan)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "dokumentasi")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray profil_pengguna(String email, String tipe_pengguna) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("tipe_pengguna", tipe_pengguna)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "profil_pengguna")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject update_profil_w_photo(String email, String tipe_pengguna, String id_jenis_kelamin,
                                           String nama, String pass, String no_hp, String tgl_lahir, String alamat,
                                           File image, String image_name) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("foto_profil", image_name, RequestBody.create(MEDIA_TYPE_PNG, image))
                    .addFormDataPart("email", email)
                    .addFormDataPart("tipe_pengguna", tipe_pengguna)
                    .addFormDataPart("id_jenis_kelamin", id_jenis_kelamin)
                    .addFormDataPart("nama", nama)
                    .addFormDataPart("pass", pass)
                    .addFormDataPart("no_hp", no_hp)
                    .addFormDataPart("tgl_lahir", tgl_lahir)
                    .addFormDataPart("alamat", alamat)
                    .addFormDataPart("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "update_profil")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject update_profil(String email, String tipe_pengguna, String id_jenis_kelamin,
                                           String nama, String pass, String no_hp, String tgl_lahir, String alamat) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("tipe_pengguna", tipe_pengguna)
                    .add("id_jenis_kelamin", id_jenis_kelamin)
                    .add("nama", nama)
                    .add("pass", pass)
                    .add("no_hp", no_hp)
                    .add("tgl_lahir", tgl_lahir)
                    .add("alamat", alamat)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "update_profil")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONArray achievement(String email) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
                    .add("keyAPI", keyAPI)
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL + "achievement")
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
}
