package com.example.nicko.turuntanganmalangapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nicko.turuntanganmalangapps.activities.LoginActivity;
import com.example.nicko.turuntanganmalangapps.fragments.GarageSaleFragment;
import com.example.nicko.turuntanganmalangapps.fragments.KegiatanDiikutiFragment;
import com.example.nicko.turuntanganmalangapps.fragments.KegiatanFragment;
import com.example.nicko.turuntanganmalangapps.fragments.ListKonfirmasiDonasiFragment;
import com.example.nicko.turuntanganmalangapps.fragments.ListKonfirmasiPembayaranFragment;
import com.example.nicko.turuntanganmalangapps.fragments.NotificationFragment;
import com.example.nicko.turuntanganmalangapps.parser.JSONParser;
import com.example.nicko.turuntanganmalangapps.sqlite.SQLiteHelper;
import com.example.nicko.turuntanganmalangapps.utils.Session;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View navHeaderView;
    private TextView txt_nama, txt_divisi;

    private Session session;
    private String trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(this);
        if (session.getTipePengguna().equals("relawan")) {
            setContentView(R.layout.activity_main_relawan);
        } else if (session.getTipePengguna().equals("donatur")) {
            setContentView(R.layout.activity_main_donatur);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeaderView = navigationView.getHeaderView(0);
        txt_nama = (TextView) navHeaderView.findViewById(R.id.txt_nama);
        txt_divisi = (TextView) navHeaderView.findViewById(R.id.txt_divisi);

        if (session.getTipePengguna().equals("relawan")) {
            txt_nama.setText(session.getNama());
            txt_divisi.setText(session.getPangkatDivisi() + " " + session.getDivisi());
        } else if (session.getTipePengguna().equals("donatur")) {
            txt_nama.setText(session.getNama());
            txt_divisi.setText("Donatur");
        }

        new JSONParser(getApplicationContext());

        if (!session.loggedin()) {
            logout();
        }

        if (getIntent().getExtras() != null) {
            trigger = getIntent().getExtras().getString("trigger");
            if (trigger.equals("konfirmasi_donasi")) {
                displaySelectedScreen(R.id.nav_konfirmasi_donasi);
            } else if (trigger.equals("konfirmasi_pembayaran")) {
                displaySelectedScreen(R.id.nav_konfirmasi_pembayaran);
            } else if (trigger.equals(("notification"))) {
                if (session.getTipePengguna().equals("relawan")) {
                    displaySelectedScreen(R.id.nav_notif_relawan);
                } else if (session.getTipePengguna().equals("donatur")) {
                    displaySelectedScreen(R.id.nav_notif_donatur);
                }
            }
        } else {
            displaySelectedScreen(R.id.nav_kegiatan);
        }

//        displaySelectedScreen(R.id.nav_kegiatan);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {
        session = new Session(this);
        Fragment fragment = null;
        if (session.getTipePengguna().equals("relawan")) {
            switch (id) {
                case R.id.nav_kegiatan:
                    fragment = new KegiatanFragment();
                    break;
                case R.id.nav_notif_relawan:
                    fragment = new NotificationFragment();
                    break;
                case R.id.nav_kegiatan_diikuti:
                    fragment = new KegiatanDiikutiFragment();
                    break;
                case R.id.nav_logout:
                    logout();
                    break;
            }
        } else if (session.getTipePengguna().equals("donatur")) {
            switch (id) {
                case R.id.nav_kegiatan:
                    fragment = new KegiatanFragment();
                    break;
                case R.id.nav_notif_donatur:
                    fragment = new NotificationFragment();
                    break;
                case R.id.nav_konfirmasi_donasi:
                    fragment = new ListKonfirmasiDonasiFragment();
                    break;
                case R.id.nav_kegiatan_diikuti:
                    fragment = new KegiatanDiikutiFragment();
                    break;
                case R.id.nav_garage_sale:
                    fragment = new GarageSaleFragment();
                    break;
                case R.id.nav_konfirmasi_pembayaran:
                    fragment = new ListKonfirmasiPembayaranFragment();
                    break;
                case R.id.nav_logout:
                    logout();
                    break;
            }
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    private void logout() {
        session.setLoggedIn(false);
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.deleteAllRecords();
        finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
