package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainClass extends AppCompatActivity {

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    // Attribute für onPause und onResume
    private String aktFragment = "";

    // Attribute speichern
    private boolean gespeichert = false;

    // Attribute für neue Übung
    private static int maxAnzahlUebungen = 1000;
    private static ObjMeineUebungen objMeineUebungen[] = new ObjMeineUebungen[maxAnzahlUebungen];
    private static int anzahlMeineUebungen = 0;

    // Attribute für Workout hinzufügen
    private boolean supportedOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        meineUebungenLaden();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrUebersicht frUebersicht = new FrUebersicht();
        fragmentTransaction.add(R.id.bereichFragments, frUebersicht, "uebersicht");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

        menueleiste();

    } // Methode onCreate

    @Override
    protected void onPause() {
        super.onPause();
        Fragment myFragment;
        myFragment = getFragmentManager().findFragmentByTag("uebersicht");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "uebersicht";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("standardUebungen");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "standardUebungen";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("objMeineUebungen");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "objMeineUebungen";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("premium");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "premium";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("support");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "support";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("einstellungen");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "einstellungen";
        } // if
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        if (supportedOpen) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        } // if


        meineUebungenSpeichern();
    } // Methode onPause

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (aktFragment.equals ("uebersicht")) {
            FrUebersicht frUebersicht = new FrUebersicht();
            fragmentTransaction.add(R.id.bereichFragments, frUebersicht, "uebersicht");
        } // if
        else if (aktFragment.equals ("standardUebungen")) {
            FrStandardUebungen frStandardUebungen = new FrStandardUebungen();
            fragmentTransaction.add(R.id.bereichFragments, frStandardUebungen, "standardUebungen");
        } // if
        else if (aktFragment.equals ("objMeineUebungen")) {
            FrMeineUebungen frMeineUebungen = new FrMeineUebungen();
            fragmentTransaction.add(R.id.bereichFragments, frMeineUebungen, "objMeineUebungen");
        } // if
        else if (aktFragment.equals ("premium")) {
            FrPremium frPremium = new FrPremium();
            fragmentTransaction.add(R.id.bereichFragments, frPremium, "premium");
        } // if
        else if (aktFragment.equals ("support")) {
            FrSupport frSupport = new FrSupport();
            fragmentTransaction.add(R.id.bereichFragments, frSupport, "support");
        } // if
        else if (aktFragment.equals ("einstellungen")) {
            FrEinstellungen frEinstellungen = new FrEinstellungen();
            fragmentTransaction.add(R.id.bereichFragments, frEinstellungen, "einstellungen");
        } // if
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode onResume

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    } // Methode onBackPressed


    // meine Übungen speichern


    public void meineUebungenSpeichern() {
        gespeichert = true;
        // Anzahl meiner Übungen speichern
        // 1. Preference erstellen --> Tag angeben
        SharedPreferences anzahlUebungenPref = getSharedPreferences("anzahlUebungen", 0);
        // 2. Editor hinzufügen --> Preference bearbeiten
        SharedPreferences.Editor editorAnzahlUebungen = anzahlUebungenPref.edit();
        // 3. Wert in die Preference legen --> 1. Tag angeben, 2. Wert angeben
        editorAnzahlUebungen.putInt("anzahlUebungen", anzahlMeineUebungen);
        // 4. Bestätigen
        editorAnzahlUebungen.commit();

        SharedPreferences gespeichertPref = getSharedPreferences("gespeichert", 0);
        SharedPreferences.Editor editorGespeichert = gespeichertPref.edit();
        editorGespeichert.putBoolean("gespeichert", gespeichert);
        editorGespeichert.commit();

        // meine Übungen speichern
        Gson gson = new Gson();

        SharedPreferences uebungPref [] = new SharedPreferences[anzahlMeineUebungen];
        String uebungPrefTag [] = new String[anzahlMeineUebungen];
        SharedPreferences.Editor editor [] = new SharedPreferences.Editor[anzahlMeineUebungen];
        String json [] = new String[anzahlMeineUebungen];

        for (int index = 0; index < anzahlMeineUebungen; index++) {
            uebungPrefTag[index] = Integer.toString(index);
            uebungPref[index] = getSharedPreferences(uebungPrefTag[index], 0);
            editor[index] = uebungPref[index].edit();
            json[index] = gson.toJson(objMeineUebungen[index]);
            editor[index].putString(uebungPrefTag[index], json[index]);
            editor[index].commit();
        } // for
    } // Methode meineÜbungenSpeichern


    // meine Übungen laden


    public void meineUebungenLaden() {
        // Objekt bilden
        for (int index = 0; index < maxAnzahlUebungen; index++) {
            objMeineUebungen[index] = new ObjMeineUebungen();
        } // for

        // Anzahl meiner Übungen laden
        // 1. Preference erstellen --> Tag angeben
        SharedPreferences anzahlUebungenPref = getSharedPreferences("anzahlUebungen", 0);
        // 2. Wert aus der Preference lesen --> Tag angeben
        anzahlMeineUebungen = anzahlUebungenPref.getInt("anzahlUebungen", 0);

        SharedPreferences gespeichertPref = getSharedPreferences("gespeichert", 0);
        gespeichert = gespeichertPref.getBoolean("gespeichert", false);

        if (gespeichert) {
            // meine Übungen laden
            Gson gson = new Gson();

            SharedPreferences uebungPref[] = new SharedPreferences[anzahlMeineUebungen];
            String uebungPrefTag[] = new String[anzahlMeineUebungen];
            String json[] = new String[anzahlMeineUebungen];

            for (int index = 0; index < anzahlMeineUebungen; index++) {
                uebungPrefTag[index] = Integer.toString(index);
                uebungPref[index] = getSharedPreferences(uebungPrefTag[index], 0);
                json[index] = uebungPref[index].getString(uebungPrefTag[index], null);
                objMeineUebungen[index] = gson.fromJson(json[index], ObjMeineUebungen.class);
            } // for
        } // if
    } // Methode meineUebungenLaden


    // Menüleiste


    public void menueleiste() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Hintergrund dunkler machen
        mDrawerLayout.setScrimColor(Color.parseColor("#33000000"));

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // nach dem Auswählen den Navigator wieder schließen
                        mDrawerLayout.closeDrawers();

                        if (supportedOpen) {
                            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
                        } // if

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.uebersicht:
                                FrUebersicht frUebersicht = new FrUebersicht();
                                fragmentTransaction.replace(R.id.bereichFragments, frUebersicht, "uebersicht");
                                break;
                            case R.id.standardUebungen:
                                FrStandardUebungen frStandardUebungen = new FrStandardUebungen();
                                fragmentTransaction.replace(R.id.bereichFragments, frStandardUebungen, "standardUebungen");
                                break;
                            case R.id.meineUebungen:
                                FrMeineUebungen frMeineUebungen = new FrMeineUebungen();
                                fragmentTransaction.replace(R.id.bereichFragments, frMeineUebungen, "meineUebungen");
                                break;
                            case R.id.premium:
                                FrPremium frPremium = new FrPremium();
                                fragmentTransaction.replace(R.id.bereichFragments, frPremium, "premium");
                                break;
                            case R.id.support:
                                FrSupport frSupport = new FrSupport();
                                fragmentTransaction.replace(R.id.bereichFragments, frSupport, "support");
                                break;
                            case R.id.einstellungen:
                                FrEinstellungen frEinstellungen = new FrEinstellungen();
                                fragmentTransaction.replace(R.id.bereichFragments, frEinstellungen, "einstellungen");
                                break;
                        } // switch

                        fragmentTransaction.addToBackStack(null);

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


    // Übungen verwalten


    public void uebungHinzufuegen (View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrMeineUebungHinzufuegen frMeineUebungHinzufuegen = new FrMeineUebungHinzufuegen();
        fragmentTransaction.replace(R.id.bereichFragments, frMeineUebungHinzufuegen, "uebungHinzufuegen");
        fragmentTransaction.addToBackStack(null);
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
        else {
            name = etName.getText().toString();
            muskelgruppe = etMuskelgruppe.getText().toString();
            beschreibung = etBeschreibung.getText().toString();
        } // else

        // Übung hinzufügen

        objMeineUebungen[anzahlMeineUebungen].neueUebung(name, muskelgruppe, beschreibung);

        anzahlMeineUebungen++;

        // Übungsübersicht anzeigen
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrMeineUebungen frMeineUebungen = new FrMeineUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, frMeineUebungen, "objMeineUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

    } // Methode uebungSpeichern

    public static String gibMeineUebungenName(int index) {
        return objMeineUebungen[index].gibName();
    }

    public static String gibMeineUebungenMuskelgruppe (int index) {
        return objMeineUebungen[index].gibMuskelgruppe();
    }

    public static String gibMeineUebungenBeschreibung(int index) {
        return objMeineUebungen[index].gibBeschreibung();
    }

    public static int gibAnzahlMeineUebungen() {
        return anzahlMeineUebungen;
    }

    public void uebungLoeschen(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        for (int zähler = tag + 1; zähler < anzahlMeineUebungen; zähler++) {
            objMeineUebungen[zähler - 1] = objMeineUebungen[zähler];
            objMeineUebungen[zähler] = null;
        } // for
        anzahlMeineUebungen--;

        // Seite neu laden
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrMeineUebungen frMeineUebungen = new FrMeineUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, frMeineUebungen, "objMeineUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode uebungLoeschen


    // Workout Hinzufügen


    public void workoutHinzufuegenOeffnen(View v) {
        supportedOpen = true;
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrWorkoutHinzufuegen frWorkoutHinzufuegen = new FrWorkoutHinzufuegen();
        fragmentTransaction.replace(R.id.bereichFragments, frWorkoutHinzufuegen, "workoutHinzufuegen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutHinzufuegenOeffnen


} // Klasse MainClass