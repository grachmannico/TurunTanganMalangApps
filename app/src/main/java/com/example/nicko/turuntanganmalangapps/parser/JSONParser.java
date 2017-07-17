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

    public static JSONArray list_konfirmasi_pembayaran(String email) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder() //FormEncodingBuilder
                    .add("email", email)
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

    public static JSONObject konfirmasi_pembelian(String invoice, File image, String image_name) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("struk", image_name, RequestBody.create(MEDIA_TYPE_PNG, image))
                    .addFormDataPart("invoice", invoice)
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
}
