package com.developer.workoutpro.itruns.workoutpro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainClass extends AppCompatActivity {

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oeffneOverviewFragment();
        menueleiste();
    } // Methode onCreate

    @Override
    public void onBackPressed() {
        // Menüleiste schließen, falls sie geöffnent ist
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } // then
        else {
            super.onBackPressed();
        } // else
    } // Methode onBackPressed

    public void oeffneOverviewFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentOverview fragmentOverview = new FragmentOverview();
        fragmentTransaction.add(R.id.bereichFragments, fragmentOverview);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode oeffneAktFragment

    public void menueleiste() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Hintergrund dunkler machen
        mDrawerLayout.setScrimColor(Color.parseColor("#33000000"));

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Start-Up aktuelles Item markieren
        navigationView.setCheckedItem(R.id.overview);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // aktuelles Item markieren
                        menuItem.setChecked(true);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
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

                            // Änderung sofort durchführen
                            fragmentManager.executePendingTransactions();
                            fragmentTransaction.commit();

                        return true;
                    }
                });

        // Menü über Button öffnen
        menuButton = findViewById(R.id.imgbtnMenue);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    } // Methode menueLeiste


    // Verschiedene Workoutarten öffnen


    public void workoutZeitOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutZeit fragmentWorkoutZeit = new FragmentWorkoutZeit();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutZeit);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutZeitOeffnen

    public void workoutWiederholungOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutWiederholung fragmentWorkoutWiederholung = new FragmentWorkoutWiederholung();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutWiederholung);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutWiederholungOeffnen

    public void tabataOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentTabata fragmentTabata = new FragmentTabata();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentTabata);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode tabataOeffnen

}
