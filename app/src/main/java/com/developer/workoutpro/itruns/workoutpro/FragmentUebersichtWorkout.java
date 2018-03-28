package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentUebersichtWorkout extends Fragment {

    // Generelle Attribute
    private int aktWorkout;
    private boolean menueOffen = false;

    // Attribute Workout Name
    TextView tvWorkoutName;
    private String workoutName;
    private int workoutNameId;

    // Attribute Workout Art
    TextView tvArtWert;
    private String workoutArt;
    private int artWertId;

    // Attribute Workout Zeit
    TextView tvZeitWert;
    private String workoutZeit;
    private int zeitWertId;

    // Attribute Workout Übungen
    TextView tvUebungenWert;
    private String workoutUebungen;
    private int uebungenWertId;

    // Menü Items Deklaration
    // Buttons deklarieren
    ImageButton imgbtnWorkoutStart;
    ImageButton imgbtnWorkoutMenue;
    FloatingActionButton btnBearbeiten;
    FloatingActionButton btnUmbennen;
    FloatingActionButton btnLoeschen;

    // Animationen deklarieren
    Animation fabOpen;
    Animation fabClose;
    Animation fabClockwise;
    Animation fabAnticlockwiese;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uebersicht_workout, container, false);

        MainClass mainClass = (MainClass) getActivity();

        // Workout Name setzen
        workoutName = "Planche Training";
        tvWorkoutName = view.findViewById(R.id.tvWorkoutName);
        tvWorkoutName.setText(workoutName);
        tvWorkoutName.setId(workoutNameId);

        // Workout Art setzen
        workoutArt = "Tabata";
        tvArtWert = view.findViewById(R.id.tvWorkoutArtWert);
        tvArtWert.setText(workoutArt);
        tvArtWert.setId(artWertId);

        // Workout Zeit setzen
        workoutZeit = "10:00";
        tvZeitWert = view.findViewById(R.id.tvZeitWert);
        tvZeitWert.setText(workoutZeit);
        tvZeitWert.setId(zeitWertId);


        // Menü Items setzen
        // Workout Übungen setzen
        workoutUebungen = "20";
        tvUebungenWert = view.findViewById(R.id.tvUebungenWert);
        tvUebungenWert.setText(workoutUebungen);
        tvUebungenWert.setId(uebungenWertId);

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
                    imgbtnWorkoutStart.setVisibility(View.VISIBLE);
                    menueOffen = false;
                } // then
                else {
                    imgbtnWorkoutStart.setVisibility(View.INVISIBLE);
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

        return view;
    }

    public void setzeAktWorkout(int pAktWorkout, int pWorkoutNameId, int pArtWertId, int pZeitWertId, int pUebungenWertId) {
        aktWorkout = pAktWorkout;
        workoutNameId = pWorkoutNameId;
        artWertId = pArtWertId;
        zeitWertId = pZeitWertId;
        uebungenWertId = pUebungenWertId;
    } // Methode setzeAtkUebung

}
