package com.kitchenworks.chefclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Second_activity extends AppCompatActivity {
    private String empresa;
    private Boolean nodispo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        empresa = bundle.getString("empresa");
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
        navView.enableAnimation(false);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.sodexo_white);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public String getEmpresa() {
        return empresa;
    }
    public boolean getDispo() {
        return nodispo;
    }
    public void setDispo(boolean dispo) {
        this.nodispo = dispo;
    }
}
