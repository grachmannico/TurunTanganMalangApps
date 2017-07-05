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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicko.turuntanganmalangapps.R;
import com.example.nicko.turuntanganmalangapps.activities.DetailBarangActivity;
import com.example.nicko.turuntanganmalangapps.activities.KeranjangBelanjaActivity;
import com.example.nicko.turuntanganmalangapps.adapters.GarageSaleAdapter;
import com.example.nicko.turuntanganmalangapps.models.GarageSale;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.utils.InternetConnection;
import com.example.nicko.turuntanganmalangapps.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nicko on 6/11/2017.
 */

public class GarageSaleFragment extends Fragment {

    private ListView listView;
    private ArrayList<GarageSale> list;
    private GarageSaleAdapter adapter;

    private Button btn_keranjang_belanja;

    private Session session;

    private String email, invoice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_garage_sale, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Garage Sale");

        session = new Session(getActivity());
        email = session.getEmail();
        if (session.getInvoice() == null) {
            invoice = "";
        } else {
            invoice = session.getInvoice();
        }

        listView = (ListView) getActivity().findViewById(R.id.list_barang);
        list = new ArrayList<>();
        adapter = new GarageSaleAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
            new Lihat_Garage_Sale().execute();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailBarangActivity.class);
                    intent.putExtra("id_barang", list.get(position).getId_barang_garage_sale());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
        }

        btn_keranjang_belanja = (Button) getActivity().findViewById(R.id.btn_keranjang_belanja);
        btn_keranjang_belanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KeranjangBelanjaActivity.class);
                startActivity(intent);
            }
        });
    }

    class Lihat_Garage_Sale extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Tunggu Sebentar");
            dialog.setMessage("Mengunduh Data Garage Sale");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray = JSONParser.lihat_garage_sale(email, invoice);
            try {
                if (jsonArray != null) {
                    int lenArray = jsonArray.length();
                    if (lenArray > 0) {
                        for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                            GarageSale model = new GarageSale(getActivity().getApplicationContext());
                            JSONObject innerObject = jsonArray.getJSONObject(jIndex);
                            String id_barang_garage_sale = innerObject.getString("id_barang_garage_sale");
                            String nama_barang = innerObject.getString("nama_barang");
                            String harga = innerObject.getString("harga");
                            String gambar_barang = innerObject.getString("gambar_barang");

                            model.setId_barang_garage_sale(id_barang_garage_sale);
                            model.setNama_barang(nama_barang);
                            model.setHarga(harga);
                            model.setGambar_barang(gambar_barang);
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
                Toast.makeText(getActivity().getApplicationContext(), "Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
            }
        }
    }
}
