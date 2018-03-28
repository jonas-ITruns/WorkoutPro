package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainClass extends AppCompatActivity {

    // Daten zum Nutzer
    private int anzahlNutzer;
    private String email;
    private String benutzername;
    private String passwort;
    private int anzahlMeineUebungen;

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    // Attribute für onPause und onResume
    private String aktFragment = "";

    // Anmeldung überprüfen
    private boolean anmeldungLaeuft = false;
    private boolean nameBestimmt;
    private boolean passwortBestimmt;
    private String pBenutzer [];
    private int aktNutzer;
    private String pPasswort [];
    private int aktPasswort;
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anmeldebildschirm);

        // Anmeldebildschrim öffnen
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentAnmelden fragmentAnmelden = new FragmentAnmelden();
        fragmentTransaction.add(R.id.bereichFragmentsAnmelden, fragmentAnmelden, "anmelden");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

    } // Methode onCreate

    @Override
    protected void onPause() {
        super.onPause();
        Fragment myFragment;
        myFragment = getFragmentManager().findFragmentByTag("anmelden");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "anmelden";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("registrieren");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "registrieren";
        } // if
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
    } // Methode onPause

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (aktFragment.equals ("anmelden") || aktFragment.equals ("")) {
            FragmentAnmelden fragmentAnmelden = new FragmentAnmelden();
            fragmentTransaction.add(R.id.bereichFragmentsAnmelden, fragmentAnmelden, "anmelden");
        } // if
        else if (aktFragment.equals ("registrieren")) {
            FragmentRegistrieren fragmentRegistrieren = new FragmentRegistrieren();
            fragmentTransaction.add(R.id.bereichFragmentsAnmelden, fragmentRegistrieren, "registrieren");
        } // if
        else if (aktFragment.equals ("uebersicht")) {
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


    // Anmeldevorgang


    public void anmelden(View v) {
        if (! anmeldungLaeuft) {
            anmeldungLaeuft = true;
            // Datenbank
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Datenbank deklarieren
            final DatabaseReference mRootRef = database.getReference();

            EditText etBenutzername = findViewById(R.id.etBenutzername);
            EditText etPasswort = findViewById(R.id.etPasswort);
            if (etBenutzername.getText().toString().isEmpty()) {
                Toast.makeText(this, "Bitte Benutzernamen eintragen", Toast.LENGTH_SHORT).show();
                anmeldungLaeuft = false;
                return;
            } // if
            else if (etPasswort.getText().toString().isEmpty()) {
                Toast.makeText(this, "Bitte Passwort eintragen", Toast.LENGTH_SHORT).show();
                anmeldungLaeuft = false;
                return;
            } // if
            else {
                benutzername = etBenutzername.getText().toString();
                passwort = etPasswort.getText().toString();
            } // else

            // Überprüfen, ob der Nutzer existiert

            // Anzahl Nutzer ermitteln
            DatabaseReference mAnzahlNutzerRef = mRootRef.child("Benutzer Verwaltung").child("Gesamt Anzahl");
            mAnzahlNutzerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    anzahlNutzer = dataSnapshot.getValue(Integer.class);
                    if (anzahlNutzer != 0) {
                        pBenutzer = new String[anzahlNutzer];
                        aktNutzer = 0;
                        pPasswort = new String[anzahlNutzer];
                        aktPasswort = 0;
                        nameBestimmt = false;
                        passwortBestimmt = false;
                        for (int zähler = 1; zähler <= anzahlNutzer; zähler++) {

                            // Name bestimmen
                            DatabaseReference mNameRef = mRootRef.child("Benutzer Verwaltung").child(Integer.toString(zähler)).child("Benutzer Name");
                            mNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    pBenutzer[aktNutzer] = dataSnapshot.getValue(String.class);
                                    aktNutzer++;
                                    if (aktNutzer == anzahlNutzer) {
                                        nameBestimmt = true;
                                    } // if
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            // Passwort bestimmen
                            DatabaseReference mPasswortRef = mRootRef.child("Benutzer Verwaltung").child(Integer.toString(zähler)).child("Passwort");
                            mPasswortRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    pPasswort[aktPasswort] = dataSnapshot.getValue(String.class);
                                    aktPasswort++;
                                    if (aktPasswort == anzahlNutzer) {
                                        passwortBestimmt = true;
                                    } // if
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } // for

                        // Übereinstimmung überprüfen
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                if (nameBestimmt && passwortBestimmt) {
                                    for (int zähler = 1; zähler <= anzahlNutzer; zähler++) {
                                        if (pBenutzer[zähler - 1].equals(benutzername) && pPasswort[zähler - 1].equals(passwort)) {
                                            anmeldebildschirmSchließen();
                                            anmeldungLaeuft = false;
                                            return;
                                        } // if
                                    } // for
                                    Toast.makeText(MainClass.this, "Dieser Account existiert nicht", Toast.LENGTH_SHORT).show();
                                    anmeldungLaeuft = false;
                                } // if
                            }
                        },SPLASH_TIME_OUT);
                    } // if
                    else {
                        Toast.makeText(MainClass.this, "Dieser Account existiert nicht", Toast.LENGTH_SHORT).show();
                        anmeldungLaeuft = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        } // if
    } // Methode anmelden

    public void zumRegistrieren(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentRegistrieren fragmentRegistrieren = new FragmentRegistrieren();
        fragmentTransaction.replace(R.id.bereichFragmentsAnmelden, fragmentRegistrieren, "registrieren");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode zumRegistrieren

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void registrieren(View v) {
        EditText etEmailAdresse = findViewById(R.id.etEmailAdresse);
        EditText etBenutzername = findViewById(R.id.etBenutzername);
        EditText etPasswort = findViewById(R.id.etPasswort);

        if (isEmailValid(etEmailAdresse.getText().toString())==false){
            Toast.makeText(this, "E-mail Adresse existiert nicht", Toast.LENGTH_SHORT).show();
            return;
        } // if
        else if (etBenutzername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bitte Benutzernamen eintragen", Toast.LENGTH_SHORT).show();
            return;
        } // if
        else if (etPasswort.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bitte Passwort eintragen", Toast.LENGTH_SHORT).show();
            return;
        } // if
        else {
            email = etEmailAdresse.getText().toString();
            benutzername = etBenutzername.getText().toString();
            passwort = etPasswort.getText().toString();
        } // else

        neuenBenutzerZurDatenbankHinzufuegen();
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragmentsAnmelden)).commit();
        anmeldebildschirmSchließen();
    } // Methode registrieren

    public void neuenBenutzerZurDatenbankHinzufuegen() {
        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        final DatabaseReference mRootRef = database.getReference();

        // Anzahl Nutzer erhöhen
        final DatabaseReference mAnzahlNutzerRef = mRootRef.child("Benutzer Verwaltung").child("Gesamt Anzahl");
        mAnzahlNutzerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anzahlNutzer = dataSnapshot.getValue(Integer.class);
                anzahlNutzer++;
                mAnzahlNutzerRef.setValue(anzahlNutzer);

                // Email hinzufügen
                DatabaseReference mEmailRef = mRootRef.child("Benutzer Verwaltung").child(Integer.toString(anzahlNutzer)).child("E-Mail");
                mEmailRef.setValue(email);

                // Benutzername hinzufügen
                DatabaseReference mName2Ref = mRootRef.child("Benutzer Verwaltung").child(Integer.toString(anzahlNutzer)).child("Benutzer Name");
                mName2Ref.setValue(benutzername);

                // Passwort hinzufügen
                DatabaseReference MPasswortRef = mRootRef.child("Benutzer Verwaltung").child(Integer.toString(anzahlNutzer)).child("Passwort");
                MPasswortRef.setValue(passwort);

                // Pfad für Übungen hinzufügen
                DatabaseReference MUebungRef = mRootRef.child("Benutzer Verwaltung").child(Integer.toString(anzahlNutzer)).child("Übungen").child("Gesamt Anzahl");
                MUebungRef.setValue("0");
                anzahlMeineUebungen = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode neuenBenutzerZurDatenbankHinzufügen

    public void anmeldebildschirmSchließen() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragmentsAnmelden)).commit();
        setContentView(R.layout.activity_main);
        menueleiste();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentUebersicht fragmentUebersicht = new FragmentUebersicht();
        fragmentTransaction.add(R.id.bereichFragments, fragmentUebersicht, "uebersicht");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode anmeldebildschirmSchließen


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


    // Verschiedene Workoutarten öffnen


    public void workoutZeitOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutZeit fragmentWorkoutZeit = new FragmentWorkoutZeit();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutZeit, "workoutZeit");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutZeitOeffnen

    public void workoutWiederholungOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutWiederholung fragmentWorkoutWiederholung = new FragmentWorkoutWiederholung();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutWiederholung, "workoutWiederholung");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutWiederholungOeffnen

    public void tabataOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentTabata fragmentTabata = new FragmentTabata();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentTabata, "tabata");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode tabataOeffnen


    // Übungen hinzufügen


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
        String aktUebungStr = Integer.toString(anzahlMeineUebungen + 1);
        DatabaseReference mRootRef = database.getReference();

        // Anzahl Übungen erneuern
        anzahlMeineUebungen++;
        DatabaseReference mAnzahl = mRootRef.child("Benutzer Verwaltung").child("Übungen").child("Gesamt Anzahl");
        mAnzahl.setValue(anzahlMeineUebungen);

        // Namen erstellen
        DatabaseReference mNameRefChild = mRootRef.child("Benutzer Verwaltung").child(benutzername).child("Übungen").child(aktUebungStr).child("Name");
        mNameRefChild.setValue(name);

        // Muskelgruppe erstellen
        DatabaseReference mMuskelgruppeRefChild =  mRootRef.child("Benutzer Verwaltung").child(benutzername).child("Übungen").child(aktUebungStr).child("Muskelgruppe");
        mMuskelgruppeRefChild.setValue(muskelgruppe);

        // Beschreibung erstellen
        DatabaseReference mBeschreibungRefChild = mRootRef.child("Benutzer Verwaltung").child(benutzername).child("Übungen").child(aktUebungStr).child("Beschreibung");
        mBeschreibungRefChild.setValue(beschreibung);

        // Übungsübersicht anzeigen
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentMeineUebungen fragmentMeineUebungen = new FragmentMeineUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentMeineUebungen, "meineUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

    } // Methode uebungSpeichern


} // Klasse MainClass
