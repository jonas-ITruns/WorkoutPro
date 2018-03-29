package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainClass extends AppCompatActivity {

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    // Attribute für onPause und onResume
    private String aktFragment = "";

    // Attribute speichern
    private boolean gespeichert = false;

    // Attribute für neue Übung
    private int maxAnzahlUebungen = 1000;
    private MeineUebungen meineUebungen [] = new MeineUebungen[maxAnzahlUebungen];
    private int anzahlMeineUebungen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meineUebungenLaden();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentUebersicht fragmentUebersicht = new FragmentUebersicht();
        fragmentTransaction.add(R.id.bereichFragments, fragmentUebersicht, "uebersicht");
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
        myFragment = getFragmentManager().findFragmentByTag("meineUebungen");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "meineUebungen";
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

        meineUebungenSpeichern();
    } // Methode onPause

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (aktFragment.equals ("uebersicht")) {
            FragmentUebersicht fragmentUebersicht = new FragmentUebersicht();
            fragmentTransaction.add(R.id.bereichFragments, fragmentUebersicht, "uebersicht");
        } // if
        else if (aktFragment.equals ("standardUebungen")) {
            FragmentStandardUebungen fragmentStandardUebungen = new FragmentStandardUebungen();
            fragmentTransaction.add(R.id.bereichFragments, fragmentStandardUebungen, "standardUebungen");
        } // if
        else if (aktFragment.equals ("meineUebungen")) {
            FragmentMeineUebungen fragmentMeineUebungen = new FragmentMeineUebungen();
            fragmentTransaction.add(R.id.bereichFragments, fragmentMeineUebungen, "meineUebungen");
        } // if
        else if (aktFragment.equals ("premium")) {
            FragmentPremium fragmentPremium = new FragmentPremium();
            fragmentTransaction.add(R.id.bereichFragments, fragmentPremium, "premium");
        } // if
        else if (aktFragment.equals ("support")) {
            FragmentSupport fragmentSupport = new FragmentSupport();
            fragmentTransaction.add(R.id.bereichFragments, fragmentSupport, "support");
        } // if
        else if (aktFragment.equals ("einstellungen")) {
            FragmentEinstellungen fragmentEinstellungen = new FragmentEinstellungen();
            fragmentTransaction.add(R.id.bereichFragments, fragmentEinstellungen, "einstellungen");
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
            json[index] = gson.toJson(meineUebungen[index]);
            editor[index].putString(uebungPrefTag[index], json[index]);
            editor[index].commit();
        } // for
    } // Methode meineÜbungenSpeichern


    // meine Übungen laden


    public void meineUebungenLaden() {
        // Objekt bilden
        for (int index = 0; index < maxAnzahlUebungen; index++) {
            meineUebungen[index] = new MeineUebungen();
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
                meineUebungen[index] = gson.fromJson(json[index], MeineUebungen.class);
            } // for
        } // if
    } // Methode meineUebungenLaden


    // Menüleiste


    public void menueleiste() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Hintergrund dunkler machen
        mDrawerLayout.setScrimColor(Color.parseColor("#33000000"));

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Start-Up aktuelles Item markieren
        navigationView.setCheckedItem(R.id.uebersicht);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // aktuelles Item markieren
                        // menuItem.setChecked(true);

                        // nach dem Auswählen den Navigator wieder schließen
                        mDrawerLayout.closeDrawers();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.uebersicht:
                                FragmentUebersicht fragmentUebersicht = new FragmentUebersicht();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentUebersicht, "uebersicht");
                                break;
                            case R.id.standardUebungen:
                                FragmentStandardUebungen fragmentStandardUebungen = new FragmentStandardUebungen();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentStandardUebungen, "standardUebungen");
                                break;
                            case R.id.meineUebungen:
                                FragmentMeineUebungen fragmentMeineUebungen = new FragmentMeineUebungen();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentMeineUebungen, "meineUebungen");
                                break;
                            case R.id.premium:
                                FragmentPremium fragmentPremium = new FragmentPremium();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentPremium, "premium");
                                break;
                            case R.id.support:
                                FragmentSupport fragmentSupport = new FragmentSupport();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSupport, "support");
                                break;
                            case R.id.einstellungen:
                                FragmentEinstellungen fragmentEinstellungen = new FragmentEinstellungen();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentEinstellungen, "einstellungen");
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
        FragmentUebungHinzufuegen fragmentUebungHinzufuegen = new FragmentUebungHinzufuegen();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentUebungHinzufuegen, "uebungHinzufuegen");
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

        meineUebungen[anzahlMeineUebungen].neueUebung(name, muskelgruppe, beschreibung);

        anzahlMeineUebungen++;

        // Übungsübersicht anzeigen
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentMeineUebungen fragmentMeineUebungen = new FragmentMeineUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentMeineUebungen, "meineUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

    } // Methode uebungSpeichern

    public String gibMeineUebungenName(int index) {
        return meineUebungen[index].gibName();
    }

    public String gibMeineUebungenMuskelgruppe (int index) {
        return meineUebungen[index].gibMuskelgruppe();
    }

    public String gibMeineUebungenBeschreibung(int index) {
        return meineUebungen[index].gibBeschreibung();
    }

    public int gibAnzahlMeineUebungen() {
        return anzahlMeineUebungen;
    }

    public void uebungLoeschen(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        for (int zähler = tag + 1; zähler < anzahlMeineUebungen; zähler++) {
            meineUebungen[zähler - 1] = meineUebungen[zähler];
            meineUebungen[zähler] = null;
        } // for
        anzahlMeineUebungen--;

        // Seite neu laden
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentMeineUebungen fragmentMeineUebungen = new FragmentMeineUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentMeineUebungen, "meineUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode uebungLoeschen


    // Workout Hinzufügen

    public void workoutHinzufuegenOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutHinzufügen fragmentWorkoutHinzufügen = new  FragmentWorkoutHinzufügen();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutHinzufügen, "fragmentWorkoutHinzufuegen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutHinzufuegenOeffnen


} // Klasse MainClass
