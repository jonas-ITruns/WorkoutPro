package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentUebersicht extends Fragment {

    private int anzahlWorkouts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uebersicht, container, false);

        anzahlWorkoutsBestimmen();
        FragmentUebersichtWorkout fragmentUebersichtWorkouts [] = new FragmentUebersichtWorkout[anzahlWorkouts];

        // Rows in der Tabelle hinzufügen
        FragmentTransaction transaction;

        for (int index = 0; index < anzahlWorkouts; index++) {

            fragmentUebersichtWorkouts[index] = new FragmentUebersichtWorkout();
            fragmentUebersichtWorkouts[index].setzeAktWorkout(index, index + 1000, index + 2000, index + 3000, index + 4000);
            transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.tlTabelle, fragmentUebersichtWorkouts[index], "exerciseRow");
            transaction.commit();

        } // for

        return view;
    }

    public void anzahlWorkoutsBestimmen() {
        MainClass mainClass = (MainClass) getActivity();
        // anzahlWorkouts = mainClass.gibAnzahlMeineUebungen();
        anzahlWorkouts = 1;
    }

}
