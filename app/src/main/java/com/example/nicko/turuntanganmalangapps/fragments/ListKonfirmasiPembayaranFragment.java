package com.example.nicko.turuntanganmalangapps.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.DetailPembelianActivity;
import com.example.nicko.turuntanganmalangapps.adapters.KonfirmasiPembayaranAdapter;
import com.example.nicko.turuntanganmalangapps.models.Pembayaran;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nicko on 6/12/2017.
 */

public class ListKonfirmasiPembayaranFragment extends Fragment {
    private ListView listView;
    private ArrayList<Pembayaran> list;
    private KonfirmasiPembayaranAdapter adapter;

    private Session session;
    private String email;

    private TextView txt_null_pembayaran;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_konfirmasi_pembayaran, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Daftar Konfirmasi Pembayaran");

        session = new Session(getActivity());
        email = session.getEmail();

        txt_null_pembayaran = (TextView) getActivity().findViewById(R.id.txt_null_pembayaran);
        listView = (ListView) getActivity().findViewById(R.id.list_konfirmasi_pembayaran);
        list = new ArrayList<>();
        adapter = new KonfirmasiPembayaranAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
            new List_Konfirmasi_Pembayaran().execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailPembelianActivity.class);
                    intent.putExtra("id_invoice", list.get(position).getId_invoice());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }
    }

    class List_Konfirmasi_Pembayaran extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Konfirmasi Pembayaran");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.list_konfirmasi_pembayaran(email);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            Pembayaran model = new Pembayaran();
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            String id_invoice = innerObject.getString("id_invoice");
                            String tanggal_pembelian = innerObject.getString("tanggal_pembelian");
                            double total_tagihan = Double.parseDouble(innerObject.getString("total_tagihan"));

                            model.setId_invoice(id_invoice);
                            model.setTanggal_pembelian(tanggal_pembelian);
                            model.setTotal_tagihan(total_tagihan);
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
            } else {
                txt_null_pembayaran.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        }
    }
}
