package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainClass extends AppCompatActivity {

    private int anzahlUebungen;

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    // Fragmente
    private String aktFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anzahlUebungenBestimmen();

        menueleiste();
    } // Methode onCreate

    public void anzahlUebungenBestimmen() {
        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        DatabaseReference mRootRef = database.getReference();

        // Anzahl Übungen erneuern
        DatabaseReference mAnzahlRef = mRootRef.child("0").child("Anzahl Übungen");
        mAnzahlRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anzahlUebungen = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode anzahlUebungenBestimmen

    @Override
    protected void onPause() {
        super.onPause();
        Fragment myFragment;
        myFragment = getFragmentManager().findFragmentByTag("overview");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "overview";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("exercises");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "exercises";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("premium");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "premium";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("support");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "support";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("settings");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "settings";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("workoutZeit");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "workoutZeit";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("workoutWiederholung");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "workoutWiederholung";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("tabata");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "tabata";
        } // if
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
    } // Methode onPause

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (aktFragment.equals ("overview") || aktFragment.equals ("")) {
            FragmentOverview fragmentOverview = new FragmentOverview();
            fragmentTransaction.add(R.id.bereichFragments, fragmentOverview, "overview");
        } // if
        else if (aktFragment.equals ("exercises")) {
            FragmentExercises fragmentExercises = new FragmentExercises();
            fragmentTransaction.add(R.id.bereichFragments, fragmentExercises, "exercises");
        } // if
        else if (aktFragment.equals ("premium")) {
            FragmentPremium fragmentPremium = new FragmentPremium();
            fragmentTransaction.add(R.id.bereichFragments, fragmentPremium, "premium");
        } // if
        else if (aktFragment.equals ("support")) {
            FragmentSupport fragmentSupport = new FragmentSupport();
            fragmentTransaction.add(R.id.bereichFragments, fragmentSupport, "support");
        } // if
        else if (aktFragment.equals ("settings")) {
            FragmentSettings fragmentSettings = new FragmentSettings();
            fragmentTransaction.add(R.id.bereichFragments, fragmentSettings, "settings");
        } // if
        else if (aktFragment.equals ("workoutZeit")) {
            FragmentWorkoutZeit fragmentWorkoutZeit = new FragmentWorkoutZeit();
            fragmentTransaction.add(R.id.bereichFragments, fragmentWorkoutZeit, "workoutZeit");
        } // if
        else if (aktFragment.equals ("workoutWiederholung")) {
            FragmentWorkoutWiederholung fragmentWorkoutWiederholung = new FragmentWorkoutWiederholung();
            fragmentTransaction.add(R.id.bereichFragments, fragmentWorkoutWiederholung, "workoutWiederholung");
        } // if
        else if (aktFragment.equals ("tabata")) {
            FragmentTabata fragmentTabata = new FragmentTabata();
            fragmentTransaction.add(R.id.bereichFragments, fragmentTabata, "tabata");
        } // if
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode onResume

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

                        // nach dem Auswählen den Navigator wieder schließen
                        mDrawerLayout.closeDrawers();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.overview:
                                FragmentOverview fragmentOverview = new FragmentOverview();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentOverview, "overview");
                                break;
                            case R.id.exercises:
                                FragmentExercises fragmentExercises = new FragmentExercises();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentExercises, "exercises");
                                break;
                            case R.id.premium:
                                FragmentPremium fragmentPremium = new FragmentPremium();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentPremium, "premium");
                                break;
                            case R.id.support:
                                FragmentSupport fragmentSupport = new FragmentSupport();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSupport, "support");
                                break;
                            case R.id.settings:
                                FragmentSettings fragmentSettings = new FragmentSettings();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSettings, "settings");
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
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutZeit, "workoutZeit");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutZeitOeffnen

    public void workoutWiederholungOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutWiederholung fragmentWorkoutWiederholung = new FragmentWorkoutWiederholung();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutWiederholung, "workoutWiederholung");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutWiederholungOeffnen

    public void tabataOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentTabata fragmentTabata = new FragmentTabata();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentTabata, "tabata");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode tabataOeffnen


    // Übungen managen

    public void uebungHinzufuegen (View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentUebungHinzufuegen fragmentUebungHinzufuegen = new FragmentUebungHinzufuegen();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentUebungHinzufuegen, "uebungHinzufuegen");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode uebungHinzufuegen

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void uebungSpeichern (View v) {
        // Deklarieren der Variablen
        String name;
        String muskelgruppe;
        String beschreibung;

        // Deklarieren der Textfelder
        EditText etName = findViewById(R.id.etUebungName);
        EditText etMuskelgruppe = findViewById(R.id.etUebungMuskelgruppe);
        EditText etBeschreibung = findViewById(R.id.etUebungBeschreibung);

        // Daten einlesen
        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bitte Übungsnamen eintragen", Toast.LENGTH_SHORT).show();
            return;
        } // if
        else if (etMuskelgruppe.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bitte Muskelgruppe eintragen", Toast.LENGTH_SHORT).show();
            return;
        } // if
        else if (etBeschreibung.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bitte Beschreibung eintragen", Toast.LENGTH_SHORT).show();
            return;
        } // if
        else if (isNetworkAvailable() == false){
            Toast.makeText(this, "Bitte eine Verbindung zum Internet herstellen", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            name = etName.getText().toString();
            muskelgruppe = etMuskelgruppe.getText().toString();
            beschreibung = etBeschreibung.getText().toString();
        } // else

        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        String aktUebungStr = Integer.toString(anzahlUebungen + 1);
        DatabaseReference mRootRef = database.getReference();

        // Anzahl Übungen erneuern
        anzahlUebungen++;
        DatabaseReference mAnzahl = mRootRef.child("0").child("Anzahl Übungen");
        mAnzahl.setValue(anzahlUebungen);

        // Namen erstellen
        DatabaseReference mNameRefChild =  mRootRef.child(aktUebungStr).child("Name");
        mNameRefChild.setValue(name);

        // Muskelgruppe erstellen
        DatabaseReference mMuskelgruppeRefChild =  mRootRef.child(aktUebungStr).child("Muskelgruppe");
        mMuskelgruppeRefChild.setValue(muskelgruppe);

        // Beschreibung erstellen
        DatabaseReference mBeschreibungRefChild = mRootRef.child(aktUebungStr).child("Beschreibung");
        mBeschreibungRefChild.setValue(beschreibung);

        // Übungsübersicht anzeigen
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentExercises fragmentExercises = new FragmentExercises();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentExercises, "uebungHinzufuegen");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

    } // Methode uebungSpeichern

} // Klasse WelcomeScreen
