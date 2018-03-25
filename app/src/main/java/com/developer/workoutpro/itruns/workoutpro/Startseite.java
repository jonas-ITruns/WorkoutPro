package com.developer.workoutpro.itruns.workoutpro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class Startseite extends AppCompatActivity {

    // Menüleiste:
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startseite);

        oeffneOverviewFragment();
        menueleiste();
    }

    public void oeffneOverviewFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentOverview fragmentOverview = new FragmentOverview();
        fragmentTransaction.add(R.id.bereichFragments, fragmentOverview);
        fragmentTransaction.commit();
    } // Methode oeffneAktFragment

    public void menueleiste() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setCheckedItem(R.id.overview);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch(menuItem.getItemId()) {
                            case R.id.overview:
                                FragmentOverview fragmentOverview = new FragmentOverview();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentOverview);
                                break;
                            case R.id.exercises:
                                FragmentExercises fragmentExercises = new FragmentExercises();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentExercises);
                                break;
                            case R.id.premium:
                                FragmentPremium fragmentPremium = new FragmentPremium();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentPremium);
                                break;
                            case R.id.support:
                                FragmentSupport fragmentSupport = new FragmentSupport();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSupport);
                                break;
                            case R.id.settings:
                                FragmentSettings fragmentSettings = new FragmentSettings();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSettings);
                                break;
                        } // switch

                        fragmentManager.executePendingTransactions();
                        fragmentTransaction.commit();

                        return true;
                    }
                });

        menuButton = findViewById(R.id.imgbtnMenue);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    } // Methode menueLeiste

}
