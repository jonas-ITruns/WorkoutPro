package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainClass extends AppCompatActivity {

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;


    // Attribute für onPause und onResume
    private String aktFragment = "";


    // Attribute für meine Übungen

    // Übungen hinzufügen

    // Fenster, das geöffnet wird
    public AlertDialog alert;
    private EditText etName;
    private EditText etBeschreibung;
    // Muskelgrupppe, die ausgewählt ist
    private boolean muskelgruppeAusgewaehlt = false;
    private boolean besonderes;
    private boolean ganzkoerper;
    private boolean arme;
    private boolean beine;
    private boolean bauch;
    private boolean brust;
    private boolean ruecken;
    // Objekt von der hinzugefügten Übung erstellen
    private static int maxAnzahlUebungen = 1000;
    private static ObjMeineUebungen objMeineUebungen[] = new ObjMeineUebungen[maxAnzahlUebungen];
    private int anzahlJeErstellterUebungen;
    private static int anzahlMeineUebungen;


    // Attribute für standard Übungenen
    private DatabaseReference mRootRef;
    private boolean synchronisierenLaeuft = false;
    private boolean erstesSynchronisieren;
    private static int anzahlStandardUebungen;
    private static ObjMeineUebungen objStandardUebungen[] = new ObjMeineUebungen[maxAnzahlUebungen];
    private int indexStandardUebung;

    // Übungen sortieren
    private String meineUebungenSortierung;
    private String standardUebungenSortierung = "name";


    // Attribute für Workout hinzufügen
    private int anzahlAlleUebungen = 0;
    private ObjMeineUebungen objAlleUebungen[] = new ObjMeineUebungen[maxAnzahlUebungen];
    private ObjMeineUebungen objAngezeigteUebungen[];
    private boolean menueOffen = false;


    // Attribute für Workouts
    private int anzahlWorkouts;
    private int aktuellesWorkout;
    private int anzahlWorkoutUebungen[] = new int[maxAnzahlUebungen];
    private int dauerWorkoutUebungen[] = new int[maxAnzahlUebungen];
    private ObjMeineUebungen objWorkoutUebungen[][] = new ObjMeineUebungen[maxAnzahlUebungen][maxAnzahlUebungen];
    private String workoutName[] = new String[maxAnzahlUebungen];
    private boolean workoutNameHinzugefuegt[] = new boolean[maxAnzahlUebungen];
    private String muskelfokus[] = new String[maxAnzahlUebungen];

    // Attribute für Workout bearbeiten
    private int anzahlWorkoutUebungenKopie;
    private int dauerWorkoutUebungenKopie;
    private ObjMeineUebungen objWorkoutUebungenKopie[] = new ObjMeineUebungen[maxAnzahlUebungen];
    private String workoutNameKopie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        datenLaden();

        menueleiste();

    } // Methode onCreate

    @Override
    protected void onPause() {
        super.onPause();
        // Fragment merken
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

        // Fragment löschen
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();

        datenSpeichern();
    } // Methode onPause

    @Override
    protected void onResume() {
        super.onResume();
        // Fragment laden
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
        else if (aktFragment.equals ("meineUebungen")) {
            FrMeineUebungen frMeineUebungen = new FrMeineUebungen();
            fragmentTransaction.add(R.id.bereichFragments, frMeineUebungen, "meineUebungen");
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


    // Shared Preferences


    public void datenSpeichern() {
        // Anzahl meiner Übungen speichern
        // 1. Preference erstellen --> Tag angeben
        SharedPreferences anzahlMeineUebungenPref = getSharedPreferences("anzahlMeineUebungen", 0);
        // 2. Editor hinzufügen --> Preference bearbeiten
        SharedPreferences.Editor editorAnzahlMeineUebungen = anzahlMeineUebungenPref.edit();
        // 3. Wert in die Preference legen --> 1. Tag angeben, 2. Wert angeben
        editorAnzahlMeineUebungen.putInt("anzahlMeineUebungen", anzahlMeineUebungen);
        // 4. Bestätigen
        editorAnzahlMeineUebungen.commit();

        // speichern, ob schonmal synchronisiert wurde
        SharedPreferences synchronisiertPref = getSharedPreferences("synchronisiert", 0);
        SharedPreferences.Editor editorSynchronisiert = synchronisiertPref.edit();
        editorSynchronisiert.putBoolean("synchronisiert", erstesSynchronisieren);
        editorSynchronisiert.commit();

        // Anzahl der standard Übungen speichern
        SharedPreferences anzahlStandardUebungenPref = getSharedPreferences("anzahlStandardUebungen", 0);
        SharedPreferences.Editor editorAnzahlStandardUebungen = anzahlStandardUebungenPref.edit();
        editorAnzahlStandardUebungen.putInt("anzahlStandardUebungen", anzahlStandardUebungen);
        editorAnzahlStandardUebungen.commit();

        // Sortierung für meine Übungen speichern
        SharedPreferences meineUebungenSortierungPref = getSharedPreferences("meineUebungenSortierung", 0);
        SharedPreferences.Editor editorMeineUebungenSortierung = meineUebungenSortierungPref.edit();
        editorMeineUebungenSortierung.putString("meineUebungenSortierung", meineUebungenSortierung);
        editorMeineUebungenSortierung.commit();

        // Anzahl je erstellter Übungen speichern
        SharedPreferences anzahlJeErstellterUebungenRef = getSharedPreferences("anzahlJeErstellterUebungen", 0);
        SharedPreferences.Editor editorAnzahlJeErstellterUebungen = anzahlJeErstellterUebungenRef.edit();
        editorAnzahlJeErstellterUebungen.putInt("anzahlJeErstellterUebungen", anzahlJeErstellterUebungen);
        editorAnzahlJeErstellterUebungen.commit();

        Gson gson = new Gson();

        // meine Übungen speichern
        SharedPreferences meineUebungPref [] = new SharedPreferences[anzahlMeineUebungen];
        String meineUebungPrefTag [] = new String[anzahlMeineUebungen];
        SharedPreferences.Editor meineUebungEditor [] = new SharedPreferences.Editor[anzahlMeineUebungen];
        String meineUebungJson [] = new String[anzahlMeineUebungen];

        for (int index = 0; index < anzahlMeineUebungen; index++) {
            meineUebungPrefTag[index] = Integer.toString(index);
            meineUebungPref[index] = getSharedPreferences(meineUebungPrefTag[index], 0);
            meineUebungEditor[index] = meineUebungPref[index].edit();
            meineUebungJson[index] = gson.toJson(objMeineUebungen[index]);
            meineUebungEditor[index].putString(meineUebungPrefTag[index], meineUebungJson[index]);
            meineUebungEditor[index].commit();
        } // for

        // standard Übungen speichern
        SharedPreferences standardUebungPref [] = new SharedPreferences[anzahlStandardUebungen];
        String standardUebungPrefTag [] = new String[anzahlStandardUebungen];
        SharedPreferences.Editor standardUebungEditor [] = new SharedPreferences.Editor[anzahlStandardUebungen];
        String standardUebungJson [] = new String[anzahlStandardUebungen];

        for (int index = 0; index < anzahlStandardUebungen; index++) {
            standardUebungPrefTag[index] = Integer.toString(index + 10000);
            standardUebungPref[index] = getSharedPreferences(standardUebungPrefTag[index], 0);
            standardUebungEditor[index] = standardUebungPref[index].edit();
            standardUebungJson[index] = gson.toJson(objStandardUebungen[index]);
            standardUebungEditor[index].putString(standardUebungPrefTag[index], standardUebungJson[index]);
            standardUebungEditor[index].commit();
        } // for

        // Workouts speichern

        // Anzahl Workouts speichern
        SharedPreferences anzahlWorkoutsPref = getSharedPreferences("anzahlWorkouts", 0);
        SharedPreferences.Editor editorAnzahlWorkouts = anzahlWorkoutsPref.edit();
        editorAnzahlWorkouts.putInt("anzahlWorkouts", anzahlWorkouts);
        editorAnzahlWorkouts.commit();

        // Anzahl der Übungen pro Workout speichern
        SharedPreferences anzahlWorkoutUebungenPref [] = new SharedPreferences[anzahlWorkouts];
        String anzahlWorkoutUebungenTag [] = new String[anzahlWorkouts];
        SharedPreferences.Editor anzahlWorkoutUebungenEditor [] = new SharedPreferences.Editor[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            anzahlWorkoutUebungenTag[index] = "anzahlWorkoutUebungen" + Integer.toString(index);
            anzahlWorkoutUebungenPref[index] = getSharedPreferences(anzahlWorkoutUebungenTag[index], 0);
            anzahlWorkoutUebungenEditor[index] = anzahlWorkoutUebungenPref[index].edit();
            anzahlWorkoutUebungenEditor[index].putInt(anzahlWorkoutUebungenTag[index], anzahlWorkoutUebungen[index]);
            anzahlWorkoutUebungenEditor[index].commit();
        } // for

        // Dauer des Workouts speichern
        SharedPreferences dauerWorkoutUebungenPref [] = new SharedPreferences[anzahlWorkouts];
        String dauerWorkoutUebungenTag [] = new String[anzahlWorkouts];
        SharedPreferences.Editor dauerWorkoutUebungenEditor [] = new SharedPreferences.Editor[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            dauerWorkoutUebungenTag[index] = "dauerWorkoutUebungen" + Integer.toString(index);
            dauerWorkoutUebungenPref[index] = getSharedPreferences(dauerWorkoutUebungenTag[index], 0);
            dauerWorkoutUebungenEditor[index] = dauerWorkoutUebungenPref[index].edit();
            dauerWorkoutUebungenEditor[index].putInt(dauerWorkoutUebungenTag[index], dauerWorkoutUebungen[index]);
            dauerWorkoutUebungenEditor[index].commit();
        } // for

        // Workouts mit Übungen speichern
        SharedPreferences objWorkoutUebungenPref [][] = new SharedPreferences[anzahlWorkouts][maxAnzahlUebungen];
        String objWorkoutUebungenTag [][] = new String[anzahlWorkouts][maxAnzahlUebungen];
        SharedPreferences.Editor objWorkoutUebungenEditor [][] = new SharedPreferences.Editor[anzahlWorkouts][maxAnzahlUebungen];
        String objWorkoutUebungenJson [][] = new String[anzahlWorkouts][maxAnzahlUebungen];

        for (int index1 = 0; index1 < anzahlWorkouts; index1++) {
            for (int index2 = 0; index2 < anzahlWorkoutUebungen[index1]; index2++) {
                objWorkoutUebungenTag[index1][index2] = "objWorkoutUebungenPref" + Integer.toString(index1) + "+" + Integer.toString(index2);
                objWorkoutUebungenPref[index1][index2] = getSharedPreferences(objWorkoutUebungenTag[index1][index2], 0);
                objWorkoutUebungenEditor[index1][index2] = objWorkoutUebungenPref[index1][index2].edit();
                objWorkoutUebungenJson[index1][index2] = gson.toJson(objWorkoutUebungen[index1][index2]);
                objWorkoutUebungenEditor[index1][index2].putString(objWorkoutUebungenTag[index1][index2], objWorkoutUebungenJson[index1][index2]);
                objWorkoutUebungenEditor[index1][index2].commit();
            } // for
        } // for

        // Speichern, ob schon ein Workout Name erstellt wurde
        SharedPreferences workoutNameHinzugefuegtPref [] = new SharedPreferences[anzahlWorkouts];
        String workoutNameHinzugefuegtTag [] = new String[anzahlWorkouts];
        SharedPreferences.Editor workoutNameHinzugefuegtEditor [] = new SharedPreferences.Editor[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            workoutNameHinzugefuegtTag[index] = "workoutNameHinzugefuegt" + Integer.toString(index);
            workoutNameHinzugefuegtPref[index] = getSharedPreferences(workoutNameHinzugefuegtTag[index], 0);
            workoutNameHinzugefuegtEditor[index] = workoutNameHinzugefuegtPref[index].edit();
            workoutNameHinzugefuegtEditor[index].putBoolean(workoutNameHinzugefuegtTag[index], workoutNameHinzugefuegt[index]);
            workoutNameHinzugefuegtEditor[index].commit();
        } // for

        // Workout Namen speichern
        SharedPreferences workoutNamePref [] = new SharedPreferences[anzahlWorkouts];
        String workoutNameTag [] = new String[anzahlWorkouts];
        SharedPreferences.Editor workoutNameEditor [] = new SharedPreferences.Editor[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            workoutNameTag[index] = "workoutName" + Integer.toString(index);
            workoutNamePref[index] = getSharedPreferences(workoutNameTag[index], 0);
            workoutNameEditor[index] = workoutNamePref[index].edit();
            workoutNameEditor[index].putString(workoutNameTag[index], workoutName[index]);
            workoutNameEditor[index].commit();
        } // for

        // Workout Muskelfokus speichern
        SharedPreferences muskelfokusPref [] = new SharedPreferences[anzahlWorkouts];
        String muskelfokusTag [] = new String[anzahlWorkouts];
        SharedPreferences.Editor muskelfokusEditor [] = new SharedPreferences.Editor[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            muskelfokusTag[index] = "muskelfokus" + Integer.toString(index);
            muskelfokusPref[index] = getSharedPreferences(muskelfokusTag[index], 0);
            muskelfokusEditor[index] = muskelfokusPref[index].edit();
            muskelfokusEditor[index].putString(muskelfokusTag[index], muskelfokus[index]);
            muskelfokusEditor[index].commit();
        } // for

    } // Methode datenSpeichern

    public void datenLaden() {
        // Anzahl meiner Übungen laden
        // 1. Preference erstellen --> Tag angeben
        SharedPreferences anzahlMeineUebungenPref = getSharedPreferences("anzahlMeineUebungen", 0);
        // 2. Wert aus der Preference lesen --> Tag angeben
        anzahlMeineUebungen = anzahlMeineUebungenPref.getInt("anzahlMeineUebungen", 0);

        // laden, ob schonmal synchronisiert wurde
        SharedPreferences synchronisiertPref = getSharedPreferences("synchronisiert", 0);
        erstesSynchronisieren = synchronisiertPref.getBoolean("synchronisiert", true);

        // Anzahl der standard Übungen laden
        SharedPreferences anzahlStandardUebungenPref = getSharedPreferences("anzahlStandardUebungen", 0);
        anzahlStandardUebungen = anzahlStandardUebungenPref.getInt("anzahlStandardUebungen", 0);

        // Sortierung für meine Übungen laden
        SharedPreferences meineUebungenSortierungPref = getSharedPreferences("meineUebungenSortierung", 0);
        meineUebungenSortierung = meineUebungenSortierungPref.getString("meineUebungenSortierung", "datum");

        // ingesamte Anzahl je erstellter Übungen laden
        SharedPreferences anzahlJeErstellterUebungenRef = getSharedPreferences("anzahlJeErstellterUebungen", 0);
        anzahlJeErstellterUebungen = anzahlJeErstellterUebungenRef.getInt("anzahlJeErstellterUebungen", 0);

        Gson gson = new Gson();

        // meine Übungen laden
        SharedPreferences meineUebungPref[] = new SharedPreferences[anzahlMeineUebungen];
        String meineUebungPrefTag[] = new String[anzahlMeineUebungen];
        String meineUebungJson[] = new String[anzahlMeineUebungen];

        for (int index = 0; index < anzahlMeineUebungen; index++) {
            meineUebungPrefTag[index] = Integer.toString(index);
            meineUebungPref[index] = getSharedPreferences(meineUebungPrefTag[index], 0);
            meineUebungJson[index] = meineUebungPref[index].getString(meineUebungPrefTag[index], null);
            objMeineUebungen[index] = gson.fromJson(meineUebungJson[index], ObjMeineUebungen.class);
        } // for

        // standard Übungen laden
        if (! erstesSynchronisieren) {
            SharedPreferences standardUebungPref[] = new SharedPreferences[anzahlStandardUebungen];
            String standardUebungPrefTag[] = new String[anzahlStandardUebungen];
            String standardUebungJson[] = new String[anzahlStandardUebungen];

            for (int index = 0; index < anzahlStandardUebungen; index++) {
                standardUebungPrefTag[index] = Integer.toString(index + 10000);
                standardUebungPref[index] = getSharedPreferences(standardUebungPrefTag[index], 0);
                standardUebungJson[index] = standardUebungPref[index].getString(standardUebungPrefTag[index], null);
                objStandardUebungen[index] = gson.fromJson(standardUebungJson[index], ObjMeineUebungen.class);
            } // for
        } // if

        // Workouts laden

        // Anzahl Workouts laden
        SharedPreferences anzahlWorkoutsPref = getSharedPreferences("anzahlWorkouts", 0);
        anzahlWorkouts = anzahlWorkoutsPref.getInt("anzahlWorkouts", 0);

        // Anzahl der Übungen pro Workout laden
        SharedPreferences anzahlWorkoutUebungenPref[] = new SharedPreferences[anzahlWorkouts];
        String anzahlWorkoutUebungenTag[] = new String[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            anzahlWorkoutUebungenTag[index] = "anzahlWorkoutUebungen" + Integer.toString(index);
            anzahlWorkoutUebungenPref[index] = getSharedPreferences(anzahlWorkoutUebungenTag[index], 0);
            anzahlWorkoutUebungen[index] = anzahlWorkoutUebungenPref[index].getInt(anzahlWorkoutUebungenTag[index], 0);
        } // for

        // Dauer des Workouts laden
        SharedPreferences dauerWorkoutUebungenPref [] = new SharedPreferences[anzahlWorkouts];
        String dauerWorkoutUebungenTag [] = new String[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            dauerWorkoutUebungenTag[index] = "dauerWorkoutUebungen" + Integer.toString(index);
            dauerWorkoutUebungenPref[index] = getSharedPreferences(dauerWorkoutUebungenTag[index], 0);
            dauerWorkoutUebungen[index] = dauerWorkoutUebungenPref[index].getInt(dauerWorkoutUebungenTag[index], 0);
        } // for

        // Workouts mit Übungen speichern
        SharedPreferences objWorkoutUebungenPref[][] = new SharedPreferences[anzahlWorkouts][maxAnzahlUebungen];
        String objWorkoutUebungenTag[][] = new String[anzahlWorkouts][maxAnzahlUebungen];
        String objWorkoutUebungenJson[][] = new String[anzahlWorkouts][maxAnzahlUebungen];

        for (int index1 = 0; index1 < anzahlWorkouts; index1++) {
            for (int index2 = 0; index2 < anzahlWorkoutUebungen[index1]; index2++) {
                objWorkoutUebungenTag[index1][index2] = "objWorkoutUebungenPref" + Integer.toString(index1) + "+" + Integer.toString(index2);
                objWorkoutUebungenPref[index1][index2] = getSharedPreferences(objWorkoutUebungenTag[index1][index2], 0);
                objWorkoutUebungenJson[index1][index2] = objWorkoutUebungenPref[index1][index2].getString(objWorkoutUebungenTag[index1][index2], null);
                objWorkoutUebungen[index1][index2] = gson.fromJson(objWorkoutUebungenJson[index1][index2], ObjMeineUebungen.class);
            } // for
        } // for

        // Laden, ob schon ein Workout Name erstellt wurde
        SharedPreferences workoutNameHinzugefuegtPref[] = new SharedPreferences[anzahlWorkouts];
        String workoutNameHinzugefuegtTag[] = new String[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            workoutNameHinzugefuegtTag[index] = "workoutNameHinzugefuegt" + Integer.toString(index);
            workoutNameHinzugefuegtPref[index] = getSharedPreferences(workoutNameHinzugefuegtTag[index], 0);
            workoutNameHinzugefuegt[index] = workoutNameHinzugefuegtPref[index].getBoolean(workoutNameHinzugefuegtTag[index], false);
        } // for

        // Workout Namen laden
        SharedPreferences workoutNamePref[] = new SharedPreferences[anzahlWorkouts];
        String workoutNameTag[] = new String[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            workoutNameTag[index] = "workoutName" + Integer.toString(index);
            workoutNamePref[index] = getSharedPreferences(workoutNameTag[index], 0);
            workoutName[index] = workoutNamePref[index].getString(workoutNameTag[index], null);
        } // for

        // Workout Muskelfokus laden

        SharedPreferences muskelfokusPref[] = new SharedPreferences[anzahlWorkouts];
        String muskelfokusTag[] = new String[anzahlWorkouts];

        for (int index = 0; index < anzahlWorkouts; index++) {
            muskelfokusTag[index] = "muskelfokus" + Integer.toString(index);
            muskelfokusPref[index] = getSharedPreferences(muskelfokusTag[index], 0);
            muskelfokus[index] = muskelfokusPref[index].getString(muskelfokusTag[index], null);
        } // for

        // Seite laden
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrUebersicht frUebersicht = new FrUebersicht();
        fragmentTransaction.add(R.id.bereichFragments, frUebersicht, "uebersicht");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode datenLaden


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

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.uebersicht:
                                FrUebersicht frUebersicht = new FrUebersicht();
                                fragmentTransaction.replace(R.id.bereichFragments, frUebersicht, "uebersicht");
                                break;
                            case R.id.standardUebungen:
                                if (erstesSynchronisieren) {
                                    standardUebungenSynchronisieren();
                                    return false;
                                } // then
                                else {
                                    FrStandardUebungen frStandardUebungen = new FrStandardUebungen();
                                    fragmentTransaction.replace(R.id.bereichFragments, frStandardUebungen, "standardUebungen");
                                } // else
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

                        // return true um das Item zu markieren
                        return false;
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


    // meine Übungen verwalten


    public void standardUebungenOeffnen() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrStandardUebungen frStandardUebungen = new FrStandardUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, frStandardUebungen, "standardUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    }

    public void meineUebungenOeffnen() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrMeineUebungen frMeineUebungen = new FrMeineUebungen();
        fragmentTransaction.replace(R.id.bereichFragments, frMeineUebungen, "meineUebungen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    }

    public void uebungHinzufuegen (View v) {
        // Hinzufügen-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_uebung_hinzufuegen);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Übung hinzufügen");

        etName = alert.findViewById(R.id.etUebungName);
        etBeschreibung = alert.findViewById(R.id.etUebungBeschreibung);

        // Tastatur automatisch öffnen
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etName.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) MainClass.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        etName.requestFocus();

        // Muskelgruppen-Buttons initialisieren
        muskelgruppeInitialisieren();

        // Übung hinzufügen speichern
        ImageButton btnUebungSpeichern = alert.findViewById(R.id.imgbtnUebungSpeichern);
        btnUebungSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deklarieren der Variablen
                String name;
                String muskelgruppe;
                String beschreibung;

                // Daten einlesen und sonst Nachricht ausgeben, dass etwas fehlt
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Übungsnamen eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etName.getText().toString().length() > 30) {
                    Toast.makeText(MainClass.this, "Name ist zu lang", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (!muskelgruppeAusgewaehlt) {
                    Toast.makeText(MainClass.this, "Bitte Muskelgruppe eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etBeschreibung.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Beschreibung eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etBeschreibung.getText().toString().length() > 80) {
                    Toast.makeText(MainClass.this, "Beschreibung ist zu lang", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else {
                    // Name bestimmen
                    name = etName.getText().toString();

                    // Muskelgruppe bestimmen
                    if (besonderes) {
                        muskelgruppe = "besonderes";
                    } else if (ganzkoerper) {
                        muskelgruppe = "ganzkoerper";
                    } else if (arme) {
                        muskelgruppe = "arme";
                    } else if (beine) {
                        muskelgruppe = "beine";
                    } else if (bauch) {
                        muskelgruppe = "bauch";
                    } else if (brust) {
                        muskelgruppe = "brust";
                    } else if (ruecken) {
                        muskelgruppe = "ruecken";
                    } else {
                        muskelgruppe = "ganzkoerper";
                    } // else

                    // Beschreibung bestimmen
                    beschreibung = etBeschreibung.getText().toString();

                    // Übung hinzufügen
                    objMeineUebungen[anzahlMeineUebungen] = new ObjMeineUebungen();
                    objMeineUebungen[anzahlMeineUebungen].neueUebung(anzahlJeErstellterUebungen, name, muskelgruppe, beschreibung, 0, 0);

                    anzahlJeErstellterUebungen++;
                    anzahlMeineUebungen++;

                    // Übungsübersicht anzeigen
                    meineUebungenOeffnen();

                    alert.cancel();
                    muskelgruppeAusgewaehlt = false;
                } // else
            }
        });

        // Übung hinzufügen abbrechen
        ImageButton btnUebungAbbrechen = alert.findViewById(R.id.imgbtnUebungAbbrechen);
        btnUebungAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
                muskelgruppeAusgewaehlt = false;
            }
        });

    } // Methode uebungHinzufuegen

    public void muskelgruppeInitialisieren() {
        // Image Buttons deklarieren
        final ImageButton imgbtnBesonderes = alert.findViewById(R.id.imgbtnBesonderes);
        final ImageButton imgbtnGanzkoerper = alert.findViewById(R.id.imgbtnGanzkoerper);
        final ImageButton imgbtnArme = alert.findViewById(R.id.imgbtnArme);
        final ImageButton imgbtnBeine = alert.findViewById(R.id.imgbtnBeine);
        final ImageButton imgbtnBauch = alert.findViewById(R.id.imgbtnBauch);
        final ImageButton imgbtnBrust = alert.findViewById(R.id.imgbtnBrust);
        final ImageButton imgbtnRuecken = alert.findViewById(R.id.imgbtnRuecken);

        // Immer nur eine Muskelgruppe auswählen, Rest wieder entfernen

        imgbtnBesonderes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = true; imgbtnBesonderes.setBackgroundResource(R.color.blauTransparent);
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
                muskelgruppeAusgewaehlt = true;
            }
        });

        imgbtnGanzkoerper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = false; imgbtnBesonderes.setBackgroundColor(0x0041577d);
                ganzkoerper = true; imgbtnGanzkoerper.setBackgroundResource(R.color.blauTransparent);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
                muskelgruppeAusgewaehlt = true;
            }
        });

        imgbtnArme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = false; imgbtnBesonderes.setBackgroundColor(0x0041577d);
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = true; imgbtnArme.setBackgroundResource(R.color.blauTransparent);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
                muskelgruppeAusgewaehlt = true;
            }
        });

        imgbtnBeine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = false; imgbtnBesonderes.setBackgroundColor(0x0041577d);
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = true; imgbtnBeine.setBackgroundResource(R.color.blauTransparent);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
                muskelgruppeAusgewaehlt = true;
            }
        });

        imgbtnBauch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = false; imgbtnBesonderes.setBackgroundColor(0x0041577d);
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = true; imgbtnBauch.setBackgroundResource(R.color.blauTransparent);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
                muskelgruppeAusgewaehlt = true;
            }
        });

        imgbtnBrust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = false; imgbtnBesonderes.setBackgroundColor(0x0041577d);
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = true; imgbtnBrust.setBackgroundResource(R.color.blauTransparent);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
                muskelgruppeAusgewaehlt = true;
            }
        });

        imgbtnRuecken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                besonderes = false; imgbtnBesonderes.setBackgroundColor(0x0041577d);
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = true; imgbtnRuecken.setBackgroundResource(R.color.blauTransparent);
                muskelgruppeAusgewaehlt = true;
            }
        });

    }

    public void uebungLoeschen(int pTag) {
        int tag = pTag;
        for (int zähler = tag + 1; zähler < anzahlMeineUebungen; zähler++) {
            objMeineUebungen[zähler - 1] = objMeineUebungen[zähler];
            objMeineUebungen[zähler] = null;
        } // for
        anzahlMeineUebungen--;
    } // Methode uebungLoeschen


    // Gib- und Setze-Methode für meine Uebungen


    public static int gibMeineUebungenNummer(int index) {
        return objMeineUebungen[index].gibUebungNummer();
    }

    public void setzeMeineUebungNummer(int pNummer, int index) {
        objMeineUebungen[index].setzeNummer(pNummer);
    }

    public static String gibMeineUebungenName(int index) {
        return objMeineUebungen[index].gibName();
    }

    public void setzeMeineUebungName(String pUebungName, int index) {
        objMeineUebungen[index].setzeName(pUebungName);
    }

    public static String gibMeineUebungenMuskelgruppe (int index) {
        return objMeineUebungen[index].gibMuskelgruppe();
    }

    public void setzeMeineUebungMuskelgruppe(String pUebungMuskelgruppe, int index) {
        objMeineUebungen[index].setzeMuskelgruppe(pUebungMuskelgruppe);
    }

    public static String gibMeineUebungenBeschreibung(int index) {
        return objMeineUebungen[index].gibBeschreibung();
    }

    public void setzeMeineUebungBeschreibung(String pUebungBeschreibung, int index) {
        objMeineUebungen[index].setzeBeschreibung(pUebungBeschreibung);
    }

    public static int gibAnzahlMeineUebungen() {
        return anzahlMeineUebungen;
    }


    // standard Übungen verwalten


    public void standardUebungenSynchronisieren() {
        if (erstesSynchronisieren) {
            standardUebungenOeffnen();
        } // then
        else {
            Animation rotate_synchronize;
            rotate_synchronize = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.rotate_synchronize);
            ImageButton imgbtnSynchronisieren = findViewById(R.id.imgbtnSynchronisieren);
            imgbtnSynchronisieren.startAnimation(rotate_synchronize);
        } // else

        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        mRootRef = database.getReference();

        // Anzahl holen
        DatabaseReference mAnzahlStandardUebungenRef = mRootRef.child("Standard Übungen");
        mAnzahlStandardUebungenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anzahlStandardUebungen = (int) dataSnapshot.getChildrenCount();
                indexStandardUebung = 0;
                uebungdetailsHolen();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void standardUebungenSynchronisieren(View v) {
        if (! synchronisierenLaeuft) {
            synchronisierenLaeuft = true;
            standardUebungenSynchronisieren();
        } // if
    }

    public void uebungdetailsHolen() {
        // Namen, Muskelgruppe, Beschreibung holen
        DatabaseReference mUebungRef = mRootRef.child("Standard Übungen").child(Integer.toString(indexStandardUebung + 1));
        mUebungRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                objStandardUebungen[indexStandardUebung] = new ObjMeineUebungen();
                String name = dataSnapshot.child("Name").getValue(String.class);
                String muskelgruppe = dataSnapshot.child("Muskelgruppe").getValue(String.class);
                String beschreibung = dataSnapshot.child("Beschreibung").getValue(String.class);
                objStandardUebungen[indexStandardUebung].neueUebung(indexStandardUebung, name, muskelgruppe, beschreibung, 0, 0);

                indexStandardUebung++;

                // Immer wieder aufrufen, sobald der Wert geholt wurde, sonst Liste erstellen
                if (indexStandardUebung != anzahlStandardUebungen) {
                    uebungdetailsHolen();
                } // if
                else {
                    if (! erstesSynchronisieren) {
                        standardUebungenOeffnen();
                        synchronisierenLaeuft = false;
                    } // then
                    else {
                        erstesSynchronisieren = false;
                        standardUebungenOeffnen();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode uebungHolen


    // Gib- und Setze-Methode für Standard Uebungen


    public boolean gibSynchronisation() {
        return erstesSynchronisieren;
    }

    public static int gibStandardUebungenNummer(int index) {
        return objStandardUebungen[index].gibUebungNummer();
    }

    public void setzeStandardUebungNummer(int pNummer, int index) {
        objStandardUebungen[index].setzeNummer(pNummer);
    }

    public static String gibStandardUebungenName(int index) {
        return objStandardUebungen[index].gibName();
    }

    public void setzeStandardUebungName(String pUebungName, int index) {
        objStandardUebungen[index].setzeName(pUebungName);
    }

    public static String gibStandardUebungenMuskelgruppe (int index) {
        return objStandardUebungen[index].gibMuskelgruppe();
    }

    public void setzeStandardUebungMuskelgruppe(String pUebungMuskelgruppe, int index) {
        objStandardUebungen[index].setzeMuskelgruppe(pUebungMuskelgruppe);
    }

    public static String gibStandardUebungenBeschreibung(int index) {
        return objStandardUebungen[index].gibBeschreibung();
    }

    public void setzeStandardUebungBeschreibung(String pUebungBeschreibung, int index) {
        objStandardUebungen[index].setzeBeschreibung(pUebungBeschreibung);
    }

    public static int gibAnzahlStandardUebungen() {
        return anzahlStandardUebungen;
    }


    // Sortierungen


    public void meineUebungenSortieren(View v) {
        PopupMenu popupMenu = new PopupMenu(MainClass.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_meine_sortierung, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Datum")) {
                    meineUebungenSortierung = "datum";
                    meineUebungenOeffnen();
                } else if (item.getTitle().equals("Name")) {
                    meineUebungenSortierung = "name";
                    meineUebungenOeffnen();
                } else if (item.getTitle().equals("Muskelgruppe")) {
                    meineUebungenSortierung = "muskelgruppe";
                    meineUebungenOeffnen();
                }
                return true;
            }
        });

        popupMenu.show();
    } // Methode meineUebungenSortieren

    public String gibMeineUebungenSortierung() {
        return meineUebungenSortierung;
    } // Methode gibMeineUebungenSortierung

    public void standardUebungenSortieren(View v) {
        PopupMenu popupMenu = new PopupMenu(MainClass.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_standard_sortierung, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Standard")) {
                    standardUebungenSortierung = "datum";
                    standardUebungenOeffnen();
                } else if (item.getTitle().equals("Name")) {
                    standardUebungenSortierung = "name";
                    standardUebungenOeffnen();
                } else if (item.getTitle().equals("Muskelgruppe")) {
                    standardUebungenSortierung = "muskelgruppe";
                    standardUebungenOeffnen();
                }
                return true;
            }
        });

        popupMenu.show();
    } // Methode standardUebungenSortieren

    public String gibStandardUebungenSortierung() {
        return standardUebungenSortierung;
    } // Methode gibStandardUebungenSortierung


    // Workout Hinzufügen


    public void workoutHinzufuegenOeffnenButton(View v) {
        aktuellesWorkout = anzahlWorkouts;

        // alle Übungen zum Objekt hinzufügen
        for (int index = 0; index < anzahlStandardUebungen; index++) {
            objAlleUebungen[index] = new ObjMeineUebungen();
            objAlleUebungen[index] = objStandardUebungen[index];
        } // for
        for (int index = 0; index < anzahlMeineUebungen; index++) {
            objAlleUebungen[index + anzahlStandardUebungen] = new ObjMeineUebungen();
            objAlleUebungen[index + anzahlStandardUebungen] = objMeineUebungen[index];
        } // for

        workoutNameHinzugefuegt[anzahlWorkouts] = false;
        workoutHinzufuegenOeffnen();
    } // Methode workoutHinzufuegenOeffnen

    public void workoutHinzufuegenOeffnen() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrWorkoutHinzufuegen frWorkoutHinzufuegen = new FrWorkoutHinzufuegen();
        fragmentTransaction.replace(R.id.bereichFragments, frWorkoutHinzufuegen, "workoutHinzufuegen");
        fragmentTransaction.addToBackStack(null);
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutHinzufuegenOeffnen

    public void workoutButtonsZeigen(View v) {
        FloatingActionButton fabUebungHinzufuegen = findViewById(R.id.btnUebungHinzufuegen);
        ImageButton btnStandardUebungen = findViewById(R.id.btnStandardUebungen);
        TextView standardUebungenText = findViewById(R.id.btnStandardUebungenText);
        ImageButton btnMeineUebungen = findViewById(R.id.btnMeineUebungen);
        TextView meineUebungenText = findViewById(R.id.btnMeineUebungenText);
        ImageButton btnBesonderes = findViewById(R.id.btnBesondereUebungen);
        TextView besonderesText = findViewById(R.id.btnBesondereUebungenText);
        ImageButton btnGanzkoerper = findViewById(R.id.btnGanzkoerper);
        TextView ganzkoerperText = findViewById(R.id.btnGanzkoerperText);
        ImageButton btnArme = findViewById(R.id.btnArme);
        TextView armeText = findViewById(R.id.btnArmeText);
        ImageButton btnBeine = findViewById(R.id.btnBeine);
        TextView beineText = findViewById(R.id.btnBeineText);
        ImageButton btnBauch = findViewById(R.id.btnBauch);
        TextView bauchText = findViewById(R.id.btnBauchText);
        ImageButton btnBrust = findViewById(R.id.btnBrust);
        TextView brustText = findViewById(R.id.btnBrustText);
        ImageButton btnRuecken = findViewById(R.id.btnRuecken);
        TextView rueckenText = findViewById(R.id.btnRueckenText);

        Animation fabOpen = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.fab_open);
        Animation fabClose = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.fab_close);
        Animation fabClockwise = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.rotate_clockwise_45);
        Animation fabAnticlockwiese = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.rotate_anticlockwise_45);

        if (menueOffen) {
            fabUebungHinzufuegen.startAnimation(fabAnticlockwiese);
            btnStandardUebungen.startAnimation(fabClose);
            standardUebungenText.startAnimation(fabClose);
            btnMeineUebungen.startAnimation(fabClose);
            meineUebungenText.startAnimation(fabClose);
            btnBesonderes.startAnimation(fabClose);
            besonderesText.startAnimation(fabClose);
            btnGanzkoerper.startAnimation(fabClose);
            ganzkoerperText.startAnimation(fabClose);
            btnArme.startAnimation(fabClose);
            armeText.startAnimation(fabClose);
            btnBeine.startAnimation(fabClose);
            beineText.startAnimation(fabClose);
            btnBauch.startAnimation(fabClose);
            bauchText.startAnimation(fabClose);
            btnBrust.startAnimation(fabClose);
            brustText.startAnimation(fabClose);
            btnRuecken.startAnimation(fabClose);
            rueckenText.startAnimation(fabClose);
            btnStandardUebungen.setClickable(false);
            btnMeineUebungen.setClickable(false);
            btnBesonderes.setClickable(false);
            btnGanzkoerper.setClickable(false);
            btnArme.setClickable(false);
            btnBeine.setClickable(false);
            btnBauch.setClickable(false);
            btnBrust.setClickable(false);
            btnRuecken.setClickable(false);
            menueOffen = false;
        } else {
            fabUebungHinzufuegen.startAnimation(fabClockwise);
            btnStandardUebungen.startAnimation(fabOpen);
            standardUebungenText.startAnimation(fabOpen);
            btnMeineUebungen.startAnimation(fabOpen);
            meineUebungenText.startAnimation(fabOpen);
            btnBesonderes.startAnimation(fabOpen);
            besonderesText.startAnimation(fabOpen);
            btnGanzkoerper.startAnimation(fabOpen);
            ganzkoerperText.startAnimation(fabOpen);
            btnArme.startAnimation(fabOpen);
            armeText.startAnimation(fabOpen);
            btnBeine.startAnimation(fabOpen);
            beineText.startAnimation(fabOpen);
            btnBauch.startAnimation(fabOpen);
            bauchText.startAnimation(fabOpen);
            btnBrust.startAnimation(fabOpen);
            brustText.startAnimation(fabOpen);
            btnRuecken.startAnimation(fabOpen);
            rueckenText.startAnimation(fabOpen);
            btnStandardUebungen.setClickable(true);
            btnMeineUebungen.setClickable(true);
            btnBesonderes.setClickable(true);
            btnGanzkoerper.setClickable(true);
            btnArme.setClickable(true);
            btnBeine.setClickable(true);
            btnBauch.setClickable(true);
            btnBrust.setClickable(true);
            btnRuecken.setClickable(true);
            menueOffen = true;
        } // else
    }

    public void workoutNamenHinzufuegen(View v) {
        // Hinzufügen-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_workout_name);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Workout Namen hinzufügen");

        final EditText etWorkoutName = alert.findViewById(R.id.etWorkoutName);

        if (workoutNameHinzugefuegt[aktuellesWorkout]) {
            etWorkoutName.setText(workoutName[aktuellesWorkout]);
        }

        // Tastatur automatisch öffnen
        etWorkoutName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etWorkoutName.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) MainClass.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etWorkoutName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        etWorkoutName.requestFocus();

        // Namen hinzufügen speichern
        ImageButton btnUebungSpeichern = alert.findViewById(R.id.imgbtnWorkoutNameSpeichern);
        btnUebungSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Daten einlesen und sonst Nachricht ausgeben, dass etwas fehlt
                if (etWorkoutName.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Workout Namen eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etWorkoutName.getText().toString().length() > 30) {
                    Toast.makeText(MainClass.this, "Workout Name ist zu lang", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else {
                    // Name bestimmen
                    workoutNameHinzugefuegt[aktuellesWorkout] = true;
                    workoutName[aktuellesWorkout] = etWorkoutName.getText().toString();

                    Button btnWorkoutName = findViewById(R.id.btnWorkoutName);
                    btnWorkoutName.setText(workoutName[aktuellesWorkout]);

                    alert.cancel();
                } // else
            }
        });

        // Übung hinzufügen abbrechen
        ImageButton btnUebungAbbrechen = alert.findViewById(R.id.imgbtnWorkoutNameAbbrechen);
        btnUebungAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

    }

    public void workoutUebungHinzufuegenOeffnen(View v) {
        int tag = Integer.parseInt(v.getTag().toString());

        // Hinzufügen-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_workout_uebung_auswaehlen);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Übung auswählen");

        // Nach Muskelgruppe beziehnungsweise Übungstyp filtern
        anzahlAlleUebungen = anzahlMeineUebungen + anzahlStandardUebungen;

        // Array Listen zum sortieren
        ArrayList<String> uebungenSortiert1 = new ArrayList<>();
        ArrayList<String> uebungenSortiert2 = new ArrayList<>();

        switch (tag) {
            // Besondere Übungen (Pause)
            case 0:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("besonderes")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Ganzkoerper
            case 1:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("ganzkoerper")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Arme
            case 2:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("arme")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Beine
            case 3:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("beine")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Bauch
            case 4:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("bauch")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Brust
            case 5:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("brust")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Ruecken
            case 6:
                for (int index = 0; index < anzahlAlleUebungen; index++) {
                    if (objAlleUebungen[index].gibMuskelgruppe().equals("ruecken")) {
                        uebungenSortiert1.add(objAlleUebungen[index].gibName() + "~" + objAlleUebungen[index].gibMuskelgruppe() + "<" + objAlleUebungen[index].gibBeschreibung());
                    } // if
                } // for
                break;
            // Standard Uebungen
            case 7:
                for (int index = 0; index < anzahlStandardUebungen; index++) {
                    uebungenSortiert1.add(objStandardUebungen[index].gibName() + "~" + objStandardUebungen[index].gibMuskelgruppe() + "<" + objStandardUebungen[index].gibBeschreibung());
                } // for
                break;
            // Meine Uebungen
            case 8:
                for (int index = 0; index < anzahlMeineUebungen; index++) {
                    uebungenSortiert1.add(objMeineUebungen[index].gibName() + "~" + objMeineUebungen[index].gibMuskelgruppe() + "<" + objMeineUebungen[index].gibBeschreibung());
                } // for
                break;
        } // switch

        // Nach Zahlen sortieren
        Collections.sort(uebungenSortiert1, new AlphanumComparator());

        String [] mUebungArray1;
        String [] mUebungArray2;

        ArrayList<Character> buchstaben = new ArrayList<>();
        for (int index = 0; index < 26; index++) {
            buchstaben.add((char) (index + 65));
        } // for
        for (int index = 0; index < 26; index++) {
            buchstaben.add((char) (index + 97));
        } // for

        Boolean [] keinBuchstabe = new Boolean[uebungenSortiert1.size()];
        int anzahlNichtBuchstaben = 0;

        objAngezeigteUebungen = new ObjMeineUebungen[uebungenSortiert1.size()];

        for (int index = 0; index < uebungenSortiert1.size(); index++) {
            keinBuchstabe[index] = false;
            for (int pIndex = 0; pIndex < buchstaben.size(); pIndex++) {
                if (! keinBuchstabe[index]) {
                    if (uebungenSortiert1.get(index).charAt(0) == buchstaben.get(pIndex)) {
                        uebungenSortiert2.add(uebungenSortiert1.get(index));
                        keinBuchstabe[index] = true;
                    } // if
                } // if
            } // for
            // Alles was kein Buchstabe ist schonmal hinzufügen
            if (! keinBuchstabe[index]) {
                objAngezeigteUebungen[index] = new ObjMeineUebungen();
                mUebungArray1 = uebungenSortiert1.get(index).split("~");
                objAngezeigteUebungen[index].setzeName(mUebungArray1[0]);
                mUebungArray2 = mUebungArray1[1].split("<");
                objAngezeigteUebungen[index].setzeMuskelgruppe(mUebungArray2[0]);
                objAngezeigteUebungen[index].setzeBeschreibung(mUebungArray2[1]);
                anzahlNichtBuchstaben++;
            } // if
        } // for

        // Sortierung nach Buchstaben
        Collections.sort(uebungenSortiert2, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        for (int index = anzahlNichtBuchstaben; index < uebungenSortiert1.size(); index++) {
            objAngezeigteUebungen[index] = new ObjMeineUebungen();
            mUebungArray1 = uebungenSortiert2.get(index - anzahlNichtBuchstaben).split("~");
            objAngezeigteUebungen[index].setzeName(mUebungArray1[0]);
            mUebungArray2 = mUebungArray1[1].split("<");
            objAngezeigteUebungen[index].setzeMuskelgruppe(mUebungArray2[0]);
            objAngezeigteUebungen[index].setzeBeschreibung(mUebungArray2[1]);
        } // for

        // Array Lists besetzen
        ArrayList<String> mName = new ArrayList<>();
        ArrayList<String> mMuskelgruppe = new ArrayList<>();
        ArrayList<String> mBeschreibung = new ArrayList<>();
        for (int index = 0; index < uebungenSortiert1.size(); index++) {
            mName.add(objAngezeigteUebungen[index].gibName());
            mMuskelgruppe.add(objAngezeigteUebungen[index].gibMuskelgruppe());
            mBeschreibung.add(objAngezeigteUebungen[index].gibBeschreibung());
        } // for

        // Übungen anzeigen
        SwipeRecyclerViewAdapter adapter;
        RecyclerView recyclerView;
        recyclerView = alert.findViewById(R.id.recycler_view_a);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(this, mName, mMuskelgruppe, mBeschreibung, true);
        recyclerView.setAdapter(adapter);

    }

    public  void workoutUebungAuswaehlenHinzufuegen(View v) {
        // Letztes Fenster schließen
        alert.cancel();

        // Hinzufügen-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_uebung_hinzufuegen);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Übung hinzufügen");

        etName = alert.findViewById(R.id.etUebungName);
        etBeschreibung = alert.findViewById(R.id.etUebungBeschreibung);

        // Tastatur automatisch öffnen
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etName.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) MainClass.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        etName.requestFocus();

        // Muskelgruppen-Buttons initialisieren
        muskelgruppeInitialisieren();

        // Übung hinzufügen speichern
        ImageButton btnUebungSpeichern = alert.findViewById(R.id.imgbtnUebungSpeichern);
        btnUebungSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deklarieren der Variablen
                String name;
                String muskelgruppe;
                String beschreibung;

                // Daten einlesen und sonst Nachricht ausgeben, dass etwas fehlt
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Übungsnamen eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etName.getText().toString().length() > 30) {
                    Toast.makeText(MainClass.this, "Name ist zu lang", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (!muskelgruppeAusgewaehlt) {
                    Toast.makeText(MainClass.this, "Bitte Muskelgruppe eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etBeschreibung.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Beschreibung eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etBeschreibung.getText().toString().length() > 80) {
                    Toast.makeText(MainClass.this, "Beschreibung ist zu lang", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else {
                    // Name bestimmen
                    name = etName.getText().toString();

                    // Muskelgruppe bestimmen
                    if (besonderes) {
                        muskelgruppe = "besonderes";
                    } else if (ganzkoerper) {
                        muskelgruppe = "ganzkoerper";
                    } else if (arme) {
                        muskelgruppe = "arme";
                    } else if (beine) {
                        muskelgruppe = "beine";
                    } else if (bauch) {
                        muskelgruppe = "bauch";
                    } else if (brust) {
                        muskelgruppe = "brust";
                    } else if (ruecken) {
                        muskelgruppe = "ruecken";
                    } else {
                        muskelgruppe = "ganzkoerper";
                    } // else

                    // Beschreibung bestimmen
                    beschreibung = etBeschreibung.getText().toString();

                    // Übung hinzufügen
                    objMeineUebungen[anzahlMeineUebungen] = new ObjMeineUebungen();
                    objMeineUebungen[anzahlMeineUebungen].neueUebung(anzahlJeErstellterUebungen, name, muskelgruppe, beschreibung, 0, 0);

                    anzahlJeErstellterUebungen++;
                    anzahlMeineUebungen++;

                    // Übung als ausgewählte Übung kennzeichnen
                    objAngezeigteUebungen = new ObjMeineUebungen[1];
                    objAngezeigteUebungen[0] = new ObjMeineUebungen();
                    objAngezeigteUebungen[0].setzeName(name);
                    objAngezeigteUebungen[0].setzeMuskelgruppe(muskelgruppe);
                    objAngezeigteUebungen[0].setzeBeschreibung(beschreibung);

                    // Seite wechseln
                    workoutUebungDauer(0);
                    muskelgruppeAusgewaehlt = false;

                } // else
            }
        });

        // Übung hinzufügen abbrechen
        ImageButton btnUebungAbbrechen = alert.findViewById(R.id.imgbtnUebungAbbrechen);
        btnUebungAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
                muskelgruppeAusgewaehlt = false;
            }
        });

    }

    public void workoutUebungDauer(final int index) {
        // Altes Fenster schließen
        alert.cancel();

        // Dauer-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_workout_uebung_zeit);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Dauer der Übung");

        final EditText etUebungDauerSekunden = alert.findViewById(R.id.etUebungDauerSekunden);
        final EditText etUebungDauerMinuten = alert.findViewById(R.id.etUebungDauerMinuten);

        // Voreingestellte Zeit ins Textefeld
        etUebungDauerMinuten.setText("00");
        etUebungDauerSekunden.setText("00");

        //kein Fokus auf dem textfeld
        etUebungDauerSekunden.clearFocus();
        etUebungDauerSekunden.setFocusable(false);
        etUebungDauerSekunden.setFocusableInTouchMode(true);
        etUebungDauerSekunden.setCursorVisible(false);

        etUebungDauerMinuten.clearFocus();
        etUebungDauerMinuten.setFocusable(false);
        etUebungDauerMinuten.setFocusableInTouchMode(true);
        etUebungDauerMinuten.setCursorVisible(false);

        etUebungDauerSekunden.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Hilfsvariabllen deklarieren
                String aktSekundenStr = etUebungDauerSekunden.getText().toString();
                int aktSekunden;

                //00 bei keiner Stelle
                if (aktSekundenStr.isEmpty()) {
                    etUebungDauerSekunden.setText("00");
                }

                // 0 bei einer Stelle
                /*if(aktSekundenStr.length()==1){
                    etUebungDauerSekunden.setText("0" + aktSekundenStr);
                }*/

                // Sekunden einlesen
                aktSekundenStr = etUebungDauerSekunden.getText().toString();
                aktSekunden = Integer.parseInt(aktSekundenStr);

                // Eingabe Grenze 59 setzen
                if (aktSekunden >= 100) {
                    aktSekunden = aktSekunden%10;
                    aktSekundenStr = Integer.toString(aktSekunden);
                    etUebungDauerSekunden.setText(aktSekundenStr);
                }
                if (aktSekunden >= 60) {
                    aktSekunden = 59;
                    aktSekundenStr = Integer.toString(aktSekunden);
                    etUebungDauerSekunden.setText(aktSekundenStr);
                }
            }
        });

        etUebungDauerMinuten.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Hilfsvariablen deklarieren
                String aktMinutenStr = etUebungDauerMinuten.getText().toString();
                int aktMinuten;

                //00 bei keiner Stelle
                if (aktMinutenStr.isEmpty()) {
                    etUebungDauerMinuten.setText("00");
                }

                //0 bei nur einer Stelle
                /*if(aktMinutenStr.length()==1){
                    etUebungDauerMinuten.setText("0" + aktMinutenStr);
                }*/

                //Minuten einlesen und umwandeln
                aktMinutenStr = etUebungDauerMinuten.getText().toString();
                aktMinuten = Integer.parseInt(aktMinutenStr);

                //Eingabe Grenze 59 setzen
                if (aktMinuten >= 100) {
                    aktMinuten = aktMinuten%10;
                    aktMinutenStr = Integer.toString(aktMinuten);
                    etUebungDauerMinuten.setText(aktMinutenStr);
                }
                if (aktMinuten >= 60) {
                    aktMinuten = 59;
                    aktMinutenStr = Integer.toString(aktMinuten);
                    etUebungDauerMinuten.setText(aktMinutenStr);
                }
            }
        });

        //Deklarieren der Button
        ImageButton imgbtnUebungDauerSpeichern = alert.findViewById(R.id.imgbtnUebungDauerSpeichern);
        ImageButton imgbtnUebungDauerAbbrechen = alert.findViewById(R.id.imgbtnUebungDauerAbbrechen);

        imgbtnUebungDauerSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dauer einlesen
                if ((etUebungDauerMinuten.getText().toString().isEmpty() && etUebungDauerSekunden.getText().toString().isEmpty())
                        || (etUebungDauerMinuten.getText().toString().equals("00") && etUebungDauerSekunden.getText().toString().equals("00"))
                        || (etUebungDauerMinuten.getText().toString().equals("0") && etUebungDauerSekunden.getText().toString().equals("0"))) {
                    Toast.makeText(MainClass.this, "Bitte Dauer der Übung eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // then
                else {
                    // Minuten einlesen
                    int minuten;
                    if (etUebungDauerMinuten.getText().toString().isEmpty()) {
                        minuten = 0;
                    } // then
                    else {
                        minuten = Integer.parseInt(etUebungDauerMinuten.getText().toString());
                    } // else

                    //Sekunden einlesen
                    int sekunden;
                    if (etUebungDauerSekunden.getText().toString().isEmpty()) {
                        sekunden = 0;
                    } // then
                    else {
                        sekunden = Integer.parseInt(etUebungDauerSekunden.getText().toString());
                    } // else

                    // Übung hinzufügen
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]] = new ObjMeineUebungen();
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]].setzeNummer(anzahlWorkoutUebungen[aktuellesWorkout]);
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]].setzeName(objAngezeigteUebungen[index].gibName());
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]].setzeMuskelgruppe(objAngezeigteUebungen[index].gibMuskelgruppe());
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]].setzeBeschreibung(objAngezeigteUebungen[index].gibBeschreibung());
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]].setzeMinuten(minuten);
                    objWorkoutUebungen[aktuellesWorkout][anzahlWorkoutUebungen[aktuellesWorkout]].setzeSekunden(sekunden);
                    anzahlWorkoutUebungen[aktuellesWorkout]++;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] + minuten * 60 + sekunden;
                    menueOffen = false;
                    alert.cancel();
                    workoutHinzufuegenOeffnen();
                }
            }
        });

        imgbtnUebungDauerAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
    }

    public void workoutUebungDauerVeraendern (View v){

        // Deklarieren der Textfelder
        EditText etUebungDauerSekunden = alert.findViewById(R.id.etUebungDauerSekunden);
        EditText etUebungDauerMinuten = alert.findViewById(R.id.etUebungDauerMinuten);

        //Deklaration der Hilfsvariablen
        String aktSekundenStr, aktMinutenStr;
        int aktSekunden, aktMinuten;

        //aktuelle Sekunden einlesen
        if (etUebungDauerSekunden.length()==0){
            aktSekunden = 0;
        }
        else{
            aktSekundenStr = etUebungDauerSekunden.getText().toString();
            aktSekunden = Integer.parseInt(aktSekundenStr);
        }

        //Zeit Limit für die Sekunden
        if (aktSekunden >= 59){
            aktSekunden = 59;
        }

        //aktuelle Minuten einlesen
        if (etUebungDauerMinuten.length()==0){
            aktMinuten = 0;
        }
        else{
            aktMinutenStr = etUebungDauerMinuten.getText().toString();
            aktMinuten = Integer.parseInt(aktMinutenStr);
        }

        //Zeit Limit für die Minuten
        if (aktMinuten >= 59){
            aktMinuten = 59;
        }

        switch(v.getId()){
            case (R.id.imgbtnPlusSekunden):
                //Fokus auf das jeweilige Textfeld setzen
                //etUebungDauerSekunden.clearFocus();
                //etUebungDauerSekunden.setFocusable(false);
                etUebungDauerSekunden.requestFocus();
                etUebungDauerSekunden.setFocusableInTouchMode(true);
                etUebungDauerSekunden.setCursorVisible(false);
                //Sekunden um 1 erhöhen
                if (aktSekunden == 59){
                    aktSekunden = 0;
                }//then
                else{
                    aktSekunden = aktSekunden+1;
                }//else
                break;
            case (R.id.imgbtnMinusSekunden):
                //Fokus auf das jeweilige Textfeld setzen
                //etUebungDauerSekunden.clearFocus();
                //etUebungDauerSekunden.setFocusable(false);
                etUebungDauerSekunden.requestFocus();
                etUebungDauerSekunden.setFocusableInTouchMode(true);
                etUebungDauerSekunden.setCursorVisible(false);
                //Sekunden um 1 erniedrigen
                if (aktSekunden == 0){
                    aktSekunden = 59;
                }//then
                else{
                    aktSekunden = aktSekunden-1;
                }//else
                break;
            case (R.id.imgbtnPlusMinuten):
                //Fokus auf das jeweilige Textfeld setzen
                etUebungDauerMinuten.requestFocus();
                etUebungDauerMinuten.setFocusableInTouchMode(true);
                etUebungDauerMinuten.setCursorVisible(false);
                //Minuten um 1 erhöhen
                if (aktMinuten == 59){
                    aktMinuten = 0;
                }//then
                else{
                    aktMinuten = aktMinuten+1;
                }//else
                break;
            case (R.id.imgbtnMinusMinuten):
                etUebungDauerMinuten.requestFocus();
                etUebungDauerMinuten.setFocusableInTouchMode(true);
                etUebungDauerMinuten.setCursorVisible(false);
                //Minuten um 1 erniedrigen
                if (aktMinuten == 0){
                    aktMinuten = 59;
                }//then
                else{
                    aktMinuten = aktMinuten-1;
                }//else
                break;
        }

        //Ausgeben der aktuellen Sekunden
        aktSekundenStr = Integer.toString(aktSekunden);
        if(aktSekundenStr.length()==1){
            aktSekundenStr = "0" + aktSekundenStr;
        }
        etUebungDauerSekunden.setText(aktSekundenStr);

        //Ausgeben der aktuellen Minuten
        aktMinutenStr = Integer.toString(aktMinuten);
        if (aktMinutenStr.length()==1){
            aktMinutenStr = "0" + aktMinutenStr;
        }//else
        etUebungDauerMinuten.setText(aktMinutenStr);
    }

    public void workoutUebungDauerBearbeiten(int tag, int index) {
        int minuten = objWorkoutUebungen[aktuellesWorkout][index].gibMinuten();
        int sekunden = objWorkoutUebungen[aktuellesWorkout][index].gibSekunden();
        switch(tag) {
            case 1:
                if (minuten == 59) {
                    minuten = 0;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] - 59 * 60;
                } else {
                    minuten++;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] + 60;
                } // if
                objWorkoutUebungen[aktuellesWorkout][index].setzeMinuten(minuten);
                break;
            case 2:
                if (minuten == 0) {
                    minuten = 59;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] + 59 * 60;
                } else {
                    minuten--;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] - 60;
                } // if
                objWorkoutUebungen[aktuellesWorkout][index].setzeMinuten(minuten);
                break;
            case 3:
                if (sekunden == 59) {
                    sekunden = 0;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] - 59;
                } else {
                    sekunden++;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] + 1;
                } // if
                objWorkoutUebungen[aktuellesWorkout][index].setzeSekunden(sekunden);
                break;
            case 4:
                if (sekunden == 0) {
                    sekunden = 59;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] + 59;
                } else {
                    sekunden--;
                    dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] - 1;
                } // if
                objWorkoutUebungen[aktuellesWorkout][index].setzeSekunden(sekunden);
                break;
        } // switch
    }

    public void workoutUebungLoeschen(int index) {
        int tag = index;
        dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungen[aktuellesWorkout] - objWorkoutUebungen[aktuellesWorkout][tag].gibMinuten() * 60 - objWorkoutUebungen[aktuellesWorkout][tag].gibSekunden();
        for (int zähler = tag + 1; zähler < anzahlWorkoutUebungen[aktuellesWorkout]; zähler++) {
            objWorkoutUebungen[aktuellesWorkout][zähler - 1] = objWorkoutUebungen[aktuellesWorkout][zähler];
            objWorkoutUebungen[aktuellesWorkout][zähler] = null;
        } // for
        anzahlWorkoutUebungen[aktuellesWorkout]--;
    }

    public void workoutSpeichern(View v) {
        // Workout Namen einlesen
        Button btnWorkoutName = findViewById(R.id.btnWorkoutName);
        if (btnWorkoutName.getText().toString().equals("Workout Namen hinzufügen")) {
            Toast.makeText(this, "Bitte Workout Namen eintragen", Toast.LENGTH_SHORT).show();
            return;
        } // then
        else {
            workoutName[aktuellesWorkout] = btnWorkoutName.getText().toString();
        } // else

        // Muskelgruppenfokus berechnen
        int ganzkoerper = 0, arme = 0, beine = 0, bauch = 0, brust = 0, ruecken = 0;
        for (int index = 0; index < anzahlWorkoutUebungen[aktuellesWorkout]; index++) {
            if (objWorkoutUebungen[aktuellesWorkout][index].gibMuskelgruppe().equals("ganzkoerper")) {
                ganzkoerper++;
            } else if (objWorkoutUebungen[aktuellesWorkout][index].gibMuskelgruppe().equals("arme")) {
                arme++;
            } else if (objWorkoutUebungen[aktuellesWorkout][index].gibMuskelgruppe().equals("beine")) {
                beine++;
            } else if (objWorkoutUebungen[aktuellesWorkout][index].gibMuskelgruppe().equals("bauch")) {
                bauch++;
            } else if (objWorkoutUebungen[aktuellesWorkout][index].gibMuskelgruppe().equals("brust")) {
                brust++;
            } else if (objWorkoutUebungen[aktuellesWorkout][index].gibMuskelgruppe().equals("ruecken")) {
                ruecken++;
            } // if
        } // for
        String maxWertStr = "-";
        int maxWert = 0;
        // Prüfen, ob eine Übung hinzugefügt wurde, sonst "-" als Muskelfokus ausgeben
        if ((ganzkoerper+arme+beine+bauch+brust+ruecken) > 0) {
            if (ganzkoerper >= maxWert) {
                maxWertStr = "Ganzkörper";
                maxWert = ganzkoerper;
            } // if
            if (arme >= maxWert) {
                maxWertStr = "Ganzkörper";
                if (arme > maxWert) {
                    maxWertStr = "Arme";
                    maxWert = arme;
                } // if
            } // if
            if (beine >= maxWert) {
                maxWertStr = "Ganzkörper";
                if (beine > maxWert) {
                    maxWertStr = "Beine";
                    maxWert = beine;
                } // if
            } // if
            if (bauch >= maxWert) {
                maxWertStr = "Ganzkörper";
                if (bauch > maxWert) {
                    maxWertStr = "Bauch";
                    maxWert = bauch;
                } // if
            } // if
            if (brust >= maxWert) {
                maxWertStr = "Ganzkörper";
                if (brust > maxWert) {
                    maxWertStr = "Brust";
                    maxWert = brust;
                } // if
            } // if
            if (ruecken >= maxWert) {
                maxWertStr = "Ganzkörper";
                if (ruecken > maxWert) {
                    maxWertStr = "Rücken";
                    maxWert = ruecken;
                } // if
            } // if
        } // then
        else {
            maxWertStr = "-";
        } // else

        muskelfokus[aktuellesWorkout] = maxWertStr;

        // Wenn es ein neues Workout ist, Anzahl erhöhen
        if (aktuellesWorkout == anzahlWorkouts) {
            anzahlWorkouts++;
        } // if

        // Seite laden
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrUebersicht frUebersicht = new FrUebersicht();
        fragmentTransaction.add(R.id.bereichFragments, frUebersicht, "uebersicht");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    }

    public void workoutErstellungAbbrechen(View v) {
        // Seite laden
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrUebersicht frUebersicht = new FrUebersicht();
        fragmentTransaction.add(R.id.bereichFragments, frUebersicht, "uebersicht");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();

        // Wenn es ein neues Workout war, Werte auf 0 setzen, ansonsten Kopien nutzen
        if (aktuellesWorkout == anzahlWorkouts) {
            workoutName[aktuellesWorkout] = new String();
            for (int index = 0; index < anzahlWorkoutUebungen[aktuellesWorkout]; index++) {
                objWorkoutUebungen[anzahlWorkouts][index] = new ObjMeineUebungen();
            } // for
            anzahlWorkoutUebungen[aktuellesWorkout] = 0;
            dauerWorkoutUebungen[aktuellesWorkout] = 0;
        } // then
        else {
            anzahlWorkoutUebungen[aktuellesWorkout] = anzahlWorkoutUebungenKopie;
            dauerWorkoutUebungen[aktuellesWorkout] = dauerWorkoutUebungenKopie;
            for (int index = 0; index < anzahlWorkoutUebungenKopie; index++) {
                objWorkoutUebungen[aktuellesWorkout][index] = objWorkoutUebungenKopie[index];
            } // for
            workoutName[aktuellesWorkout] = workoutNameKopie;
        } // else

    }

    public void workoutUmbennen(final int workout) {
        // Umbennen-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_workout_name);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Workout Namen ändern");

        final EditText etWorkoutName = alert.findViewById(R.id.etWorkoutName);
        etWorkoutName.setText(workoutName[workout]);

        // Tastatur automatisch öffnen
        etWorkoutName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etWorkoutName.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) MainClass.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etWorkoutName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        etWorkoutName.requestFocus();

        // Namen hinzufügen speichern
        ImageButton btnUebungSpeichern = alert.findViewById(R.id.imgbtnWorkoutNameSpeichern);
        btnUebungSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Daten einlesen und sonst Nachricht ausgeben, dass etwas fehlt
                if (etWorkoutName.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Workout Namen eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etWorkoutName.getText().toString().length() > 30) {
                    Toast.makeText(MainClass.this, "Workout Name ist zu lang", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else {
                    // Name bestimmen
                    workoutName[workout] = etWorkoutName.getText().toString();

                    FrUebersichtRow.workoutNamenAktualisieren(workoutName[workout]);

                    alert.cancel();
                } // else
            }
        });

        // Übung hinzufügen abbrechen
        ImageButton btnUebungAbbrechen = alert.findViewById(R.id.imgbtnWorkoutNameAbbrechen);
        btnUebungAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

    }

    public void workoutLoeschen(final int workout) {
        // Bestätigen-Fenster öffnen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.al_workout_loeschen_bestaetigen);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();

        // Deklarieren der Textfelder
        TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
        tvAlertUeberschrift.setText("Workout wirklich löschen?");

        ImageButton imgbtnWorkoutLoeschenSpeichern = alert.findViewById(R.id.imgbtnWorkoutLoeschenSpeichern);
        imgbtnWorkoutLoeschenSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = workout;
                for (int zähler = tag + 1; zähler < anzahlWorkouts + 1; zähler++) {
                    for (int pZähler = 0; pZähler < maxAnzahlUebungen; pZähler++) {
                        objWorkoutUebungen[zähler - 1][pZähler] = objWorkoutUebungen[zähler][pZähler];
                        objWorkoutUebungen[zähler][pZähler] = null;
                    } // for
                    anzahlWorkoutUebungen[zähler - 1] = anzahlWorkoutUebungen[zähler];
                    anzahlWorkoutUebungen[zähler] = 0;
                    dauerWorkoutUebungen[zähler - 1] = dauerWorkoutUebungen[zähler];
                    dauerWorkoutUebungen[zähler] = 0;
                    workoutName[zähler - 1] = workoutName[zähler];
                    workoutName[zähler] = null;
                    workoutNameHinzugefuegt[zähler - 1] = workoutNameHinzugefuegt[zähler];
                    workoutNameHinzugefuegt[zähler] = false;
                    muskelfokus[zähler - 1] = muskelfokus[zähler];
                    muskelfokus[zähler] = null;
                } // for
                anzahlWorkouts--;

                // Seite laden
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FrUebersicht frUebersicht = new FrUebersicht();
                fragmentTransaction.add(R.id.bereichFragments, frUebersicht, "uebersicht");
                fragmentManager.executePendingTransactions();
                fragmentTransaction.commit();

                alert.cancel();
            }
        });

        ImageButton imgbtnWorkoutLoeschenAbbrechen = alert.findViewById(R.id.imgbtnWorkoutLoeschenAbbrechen);
        imgbtnWorkoutLoeschenAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

    }

    public void workoutBearbeiten(int workout) {
        aktuellesWorkout = workout;

        // Kopie des Workouts erstellen
        anzahlWorkoutUebungenKopie = anzahlWorkoutUebungen[aktuellesWorkout];
        dauerWorkoutUebungenKopie = dauerWorkoutUebungen[aktuellesWorkout];
        for (int index = 0; index < anzahlWorkoutUebungenKopie; index++) {
            objWorkoutUebungenKopie[index] = objWorkoutUebungen[aktuellesWorkout][index];
        } // for
        workoutNameKopie = workoutName[aktuellesWorkout];

        workoutHinzufuegenOeffnen();
    }

    public void workoutUebungDrag(int pos1, int pos2) {
        ObjMeineUebungen objWorkoutUebungSpeichern = objWorkoutUebungen[aktuellesWorkout][pos1];
        objWorkoutUebungen[aktuellesWorkout][pos1] = objWorkoutUebungen[aktuellesWorkout][pos2];
        objWorkoutUebungen[aktuellesWorkout][pos2] = objWorkoutUebungSpeichern;
    }


    // Gib-Methoden für Workout hinzufügen


    public boolean gibWorkoutNameHinzugefuegt(int workout) {
        return workoutNameHinzugefuegt[workout];
    }

    public String gibWorkoutName(int workout) {
        return workoutName[workout];
    }

    public String gibWorkoutUebungName(int workout, int index) {
        return objWorkoutUebungen[workout][index].gibName();
    }

    public String gibWorkoutUebungMuskelgruppe(int workout, int index) {
        return objWorkoutUebungen[workout][index].gibMuskelgruppe();
    }

    public String gibWorkoutUebungBeschreibung(int workout, int index) {
        return objWorkoutUebungen[workout][index].gibBeschreibung();
    }

    public int gibWorkoutUebungMinuten(int workout, int index) {
        return objWorkoutUebungen[workout][index].gibMinuten();
    }

    public int gibWorkoutUebungSekunden(int workout, int index) {
        return objWorkoutUebungen[workout][index].gibSekunden();
    }

    public int gibWorkoutUebungAnzahl(int workout) {
        return anzahlWorkoutUebungen[workout];
    }

    public int gibAnzahlWorkouts() {
        return anzahlWorkouts;
    }

    public int gibWorkoutUebungAnzahlUebersicht(int workout) {
        return anzahlWorkoutUebungen[workout];
    }

    public int gibDauerWorkoutUebungenUebersicht(int workout) {
        return dauerWorkoutUebungen[workout];
    }

    public String gibWorkoutMuskelfokus(int workout) {
        return muskelfokus[workout];
    }

    public int gibBearbeitendesWorkout() {
        return aktuellesWorkout;
    }


    // Workout laufen lassen

    public void workoutStart(int workout) {
        // Seite wechseln
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        setContentView(R.layout.act_workout_start);



    } // Methode workoutStart


} // Klasse MainClass