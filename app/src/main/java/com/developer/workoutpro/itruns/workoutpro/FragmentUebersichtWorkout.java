package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentUebersichtWorkout extends Fragment {

    // Generelle Attribute
    private int aktWorkout;

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

        // Workout Übungen setzen
        workoutUebungen = "20";
        tvUebungenWert = view.findViewById(R.id.tvUebungenWert);
        tvUebungenWert.setText(workoutUebungen);
        tvUebungenWert.setId(uebungenWertId);

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
