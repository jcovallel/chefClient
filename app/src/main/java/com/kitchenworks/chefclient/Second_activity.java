package com.kitchenworks.chefclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Second_activity extends AppCompatActivity {
    private String empresa, adminName;
    private int imgnummenu, imgnumtips;
    private Boolean nodispo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        empresa = bundle.getString("empresa");
        imgnummenu = bundle.getInt("imgnummenu");
        imgnumtips = bundle.getInt("imgnumtips");
        adminName = bundle.getString("admin");
        nodispo = false;

        super.onCreate(savedInstanceState);
        getSupportActionBar().show();
        setContentView(R.layout.activity_main);
        BottomNavigationViewEx navView = (BottomNavigationViewEx) findViewById(R.id.bottom_bar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_tips, R.id.navigation_menu, R.id.navigation_reserva, R.id.navigation_comenta)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setTextSize(10);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.sodexo_white);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public String getEmpresa() {
        return empresa;
    }
    public String getAdminName() {
        return adminName;
    }
    public int getImgnummenu() {
        return imgnummenu;
    }
    public int getImgnumtips() {
        return imgnumtips;
    }
    public boolean getDispo() {
        return nodispo;
    }
    public void setDispo(boolean dispo) {
        this.nodispo = dispo;
    }
}
