package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class FrUebersichtRow extends Fragment {

    // Generelle Attribute
    private static View view;
    private int aktWorkout;
    private boolean menueOffen = false;

    // Attribute Workout Name
    private static TextView tvWorkoutName;
    private String workoutName;
    private int workoutNameId;
    private boolean nameScrollt = false;

    // Attribute Workout Art
    private TextView tvArtWert;
    private String workoutArt;
    private int artWertId;

    // Attribute Workout Zeit
    private TextView tvZeitWert;
    private int workoutZeitInt;
    private String workoutZeitStr;
    private int workoutZeitStundenInt;
    private String workoutZeitStundenStr;
    private int workoutZeitMinInt;
    private String workoutZeitMinStr;
    private int workoutZeitSekInt;
    private String workoutZeitSekStr;
    private int zeitWertId;

    // Attribute Workout Übungen
    private TextView tvUebungenWert;
    private String workoutUebungen;
    private int uebungenWertId;

    // Menü Items Deklaration
    // Buttons deklarieren
    ImageButton imgbtnWorkoutStart;
    ImageButton imgbtnWorkoutMenue;
    ImageButton btnBearbeiten;
    ImageButton btnUmbennen;
    ImageButton btnLoeschen;

    // Animationen deklarieren
    Animation fabOpen;
    Animation fabClose;
    Animation fabClockwise;
    Animation fabAnticlockwiese;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_uebersicht_row, container, false);

        final MainClass mainClass = (MainClass) getActivity();

        // Workout Name setzen
        workoutName = mainClass.gibWorkoutName(aktWorkout);
        tvWorkoutName = view.findViewById(R.id.tvWorkoutName);
        tvWorkoutName.setText(workoutName);
        tvWorkoutName.setId(workoutNameId);
        tvWorkoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameScrollt) {
                    tvWorkoutName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tvWorkoutName.setSelected(true);
                    tvWorkoutName.setSingleLine(true);
                    nameScrollt = true;
                } // then
                else {
                    tvWorkoutName.setEllipsize(TextUtils.TruncateAt.END);
                    tvWorkoutName.setSelected(true);
                    tvWorkoutName.setSingleLine(true);
                    nameScrollt = false;
                } // else
            }
        });

        // Workout Art setzen
        workoutArt = mainClass.gibWorkoutMuskelfokus(aktWorkout);
        tvArtWert = view.findViewById(R.id.tvWorkoutArtWert);
        tvArtWert.setText(workoutArt);
        tvArtWert.setId(artWertId);

        // Workout Zeit setzen
        workoutZeitInt = mainClass.gibDauerWorkoutUebungenUebersicht(aktWorkout);
        workoutZeitMinInt = workoutZeitInt / 60;
        // Ins Format umwandeln
        // Stunden
        if (workoutZeitMinInt >= 60) {
            workoutZeitStundenInt = workoutZeitMinInt / 60;
            workoutZeitMinInt = workoutZeitMinInt % 60;
            if (workoutZeitStundenInt < 10) {
                workoutZeitStundenStr = "0" + Integer.toString(workoutZeitStundenInt) + ":";
            } else {
                workoutZeitStundenStr = Integer.toString(workoutZeitStundenInt) + ":";
            } // if
        } else {
            workoutZeitStundenInt = 0;
            workoutZeitStundenStr = "";
        } // if
        // Minuten
        if (workoutZeitMinInt < 10) {
            workoutZeitMinStr = "0" + Integer.toString(workoutZeitMinInt);
        } else {
            workoutZeitMinStr = Integer.toString(workoutZeitMinInt);
        } // if
        // Sekunden
        workoutZeitSekInt = workoutZeitInt % 60;
        if (workoutZeitSekInt < 10) {
            workoutZeitSekStr = "0" + Integer.toString(workoutZeitSekInt);
        } else {
            workoutZeitSekStr = Integer.toString(workoutZeitSekInt);
        } // if
        workoutZeitStr = workoutZeitStundenStr + workoutZeitMinStr + ":" + workoutZeitSekStr;
        // Zeit ausgeben
        tvZeitWert = view.findViewById(R.id.tvZeitWert);
        tvZeitWert.setText(workoutZeitStr);
        tvZeitWert.setId(zeitWertId);

        // Workout Übungen setzen
        workoutUebungen = Integer.toString(mainClass.gibWorkoutUebungAnzahlUebersicht(aktWorkout));
        tvUebungenWert = view.findViewById(R.id.tvUebungenWert);
        tvUebungenWert.setText(workoutUebungen);
        tvUebungenWert.setId(uebungenWertId);


        // Menü Items setzen
        imgbtnWorkoutStart = view.findViewById(R.id.imgbtnWorkoutStart);
        imgbtnWorkoutMenue = view.findViewById(R.id.imgbtnWorkoutMenue);
        btnBearbeiten = view.findViewById(R.id.btnBearbeiten);
        btnUmbennen = view.findViewById(R.id.btnUmbennen);
        btnLoeschen = view.findViewById(R.id.btnLoeschen);

        // Animationen deklarieren
        fabOpen = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        fabClockwise = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_clockwise);
        fabAnticlockwiese = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_anticlockwise);

        imgbtnWorkoutMenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menueOffen) {
                    btnBearbeiten.startAnimation(fabClose);
                    btnUmbennen.startAnimation(fabClose);
                    btnLoeschen.startAnimation(fabClose);
                    imgbtnWorkoutMenue.startAnimation(fabAnticlockwiese);
                    btnBearbeiten.setClickable(false);
                    btnUmbennen.setClickable(false);
                    btnLoeschen.setClickable(false);
                    imgbtnWorkoutStart.setClickable(true);
                    imgbtnWorkoutStart.startAnimation(fabOpen);
                    menueOffen = false;
                } // then
                else {
                    imgbtnWorkoutStart.startAnimation(fabClose);
                    imgbtnWorkoutStart.setClickable(false);
                    btnBearbeiten.startAnimation(fabOpen);
                    btnUmbennen.startAnimation(fabOpen);
                    btnLoeschen.startAnimation(fabOpen);
                    imgbtnWorkoutMenue.startAnimation(fabClockwise);
                    btnBearbeiten.setClickable(true);
                    btnUmbennen.setClickable(true);
                    btnLoeschen.setClickable(true);
                    menueOffen = true;
                } // else
            }
        });

        btnLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClass.workoutLoeschen(aktWorkout);
            }
        });

        btnUmbennen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClass.workoutUmbennen(aktWorkout);
            }
        });

        btnBearbeiten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClass.workoutBearbeiten(aktWorkout);
            }
        });

        imgbtnWorkoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClass.workoutStart(aktWorkout);
            }
        });

        return view;
    }

    public void setzeAktWorkout(int pAktWorkout, int pWorkoutNameId, int pArtWertId, int pZeitWertId, int pUebungenWertId) {
        aktWorkout = pAktWorkout;
        workoutNameId = pWorkoutNameId;
        artWertId = pArtWertId;
        zeitWertId = pZeitWertId;
        uebungenWertId = pUebungenWertId;
    } // Methode setzeAtkUebung

    public static void workoutNamenAktualisieren(String workoutName) {
        tvWorkoutName.setText(workoutName);
    }

}
