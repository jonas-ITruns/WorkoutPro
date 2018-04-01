package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class MainClass extends AppCompatActivity {

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    // Attribute für onPause und onResume
    private String aktFragment = "";

    // Attribute für Shared Preferences
    private boolean gespeichert = false;

    // Attribute für meine Übungen
    // Übungen hinzufügen
    // Fenster, das geöffnet wird
    private AlertDialog alert;
    private EditText etName;
    private EditText etBeschreibung;
    // Muskelgrupppe, die ausgewählt ist
    private boolean muskelgruppeAusgewaehlt = false;
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
    // Übungen sortieren
    private String meineUebungenSortierung;
    private String standardUebungenSortierung = "datum";

    // Attribute für Workout hinzufügen
    private boolean supportedOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        datenLaden();

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
        if (supportedOpen) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
        } // if

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
        gespeichert = true;
        // Anzahl meiner Übungen speichern
        // 1. Preference erstellen --> Tag angeben
        SharedPreferences anzahlMeineUebungenPref = getSharedPreferences("anzahlMeineUebungen", 0);
        // 2. Editor hinzufügen --> Preference bearbeiten
        SharedPreferences.Editor editorAnzahlMeineUebungen = anzahlMeineUebungenPref.edit();
        // 3. Wert in die Preference legen --> 1. Tag angeben, 2. Wert angeben
        editorAnzahlMeineUebungen.putInt("anzahlMeineUebungen", anzahlMeineUebungen);
        // 4. Bestätigen
        editorAnzahlMeineUebungen.commit();

        // speichern, dass schonmal gespeichert wurde
        SharedPreferences gespeichertPref = getSharedPreferences("gespeichert", 0);
        SharedPreferences.Editor editorGespeichert = gespeichertPref.edit();
        editorGespeichert.putBoolean("gespeichert", gespeichert);
        editorGespeichert.commit();

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

        // meine Übungen speichern
        Gson gson = new Gson();

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
    } // Methode datenSpeichern

    public void datenLaden() {
        // Anzahl meiner Übungen laden
        // 1. Preference erstellen --> Tag angeben
        SharedPreferences anzahlMeineUebungenPref = getSharedPreferences("anzahlMeineUebungen", 0);
        // 2. Wert aus der Preference lesen --> Tag angeben
        anzahlMeineUebungen = anzahlMeineUebungenPref.getInt("anzahlMeineUebungen", 0);

        // laden, ob schonmal gespeichert wurde
        SharedPreferences gespeichertPref = getSharedPreferences("gespeichert", 0);
        gespeichert = gespeichertPref.getBoolean("gespeichert", false);

        // Sortierung für meine Übungen laden
        SharedPreferences meineUebungenSortierungPref = getSharedPreferences("meineUebungenSortierung", 0);
        meineUebungenSortierung = meineUebungenSortierungPref.getString("meineUebungenSortierung", "datum");

        // ingesamte Anzahl je erstellter Übungen laden
        SharedPreferences anzahlJeErstellterUebungenRef = getSharedPreferences("anzahlJeErstellterUebungen", 0);
        anzahlJeErstellterUebungen = anzahlJeErstellterUebungenRef.getInt("anzahlJeErstellterUebungen", 0);

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
        builder.setView(R.layout.fr_uebung_hinzufuegen);
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
        Button btnUebungSpeichern = alert.findViewById(R.id.btnUebungSpeichern);
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
                else if (!muskelgruppeAusgewaehlt) {
                    Toast.makeText(MainClass.this, "Bitte Muskelgruppe eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else if (etBeschreibung.getText().toString().isEmpty()) {
                    Toast.makeText(MainClass.this, "Bitte Beschreibung eintragen", Toast.LENGTH_SHORT).show();
                    return;
                } // if
                else {
                    // Name bestimmen
                    name = etName.getText().toString();

                    // Muskelgruppe bestimmen
                    if (ganzkoerper) {
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
                    objMeineUebungen[anzahlMeineUebungen].neueUebung(anzahlJeErstellterUebungen, name, muskelgruppe, beschreibung);

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
        Button btnUebungAbbrechen = alert.findViewById(R.id.btnUebungAbbrechen);
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
        final ImageButton imgbtnGanzkoerper = alert.findViewById(R.id.imgbtnGanzkoerper);
        final ImageButton imgbtnArme = alert.findViewById(R.id.imgbtnArme);
        final ImageButton imgbtnBeine = alert.findViewById(R.id.imgbtnBeine);
        final ImageButton imgbtnBauch = alert.findViewById(R.id.imgbtnBauch);
        final ImageButton imgbtnBrust = alert.findViewById(R.id.imgbtnBrust);
        final ImageButton imgbtnRuecken = alert.findViewById(R.id.imgbtnRuecken);

        // Immer nur eine Muskelgruppe auswählen, Rest wieder entfernen

        muskelgruppeAusgewaehlt = true;

        imgbtnGanzkoerper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = true; imgbtnGanzkoerper.setBackgroundResource(R.color.blauTransparent);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);

            }
        });

        imgbtnArme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = true; imgbtnArme.setBackgroundResource(R.color.blauTransparent);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnBeine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = true; imgbtnBeine.setBackgroundResource(R.color.blauTransparent);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnBauch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = true; imgbtnBauch.setBackgroundResource(R.color.blauTransparent);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnBrust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = true; imgbtnBrust.setBackgroundResource(R.color.blauTransparent);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnRuecken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = true; imgbtnRuecken.setBackgroundResource(R.color.blauTransparent);
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


    // Gib- und Setze-Methode für MeineUebungen


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


    // Sortierungen


    public void meineUebungenSortieren(View v) {
        PopupMenu popupMenu = new PopupMenu(MainClass.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

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
        int tag = Integer.parseInt(v.getTag().toString());
        switch (tag) {
            case 1: standardUebungenSortierung = "datum";
                break;
            case 2: standardUebungenSortierung = "name";
                break;
            case 3: standardUebungenSortierung = "muskelgruppe";
                break;
        } // switch
        standardUebungenOeffnen();
    } // Methode standardUebungenSortieren

    public String gibStandardUebungenSortierung() {
        return standardUebungenSortierung;
    } // Methode gibStandardUebungenSortierung


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