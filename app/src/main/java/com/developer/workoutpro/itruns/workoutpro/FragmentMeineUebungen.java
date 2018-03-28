package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMeineUebungen extends Fragment {

    private int anzahlMeineUebungen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        anzahlUebungenBestimmen();
        FragmentExerciseRowMeineUebungen fragmentExerciseRowMeineUebungens[] = new FragmentExerciseRowMeineUebungen[anzahlMeineUebungen];

        // Rows in der Tabelle hinzufügen
        FragmentTransaction transaction;

        for (int index = 0; index < anzahlMeineUebungen; index++) {

            fragmentExerciseRowMeineUebungens[index] = new FragmentExerciseRowMeineUebungen();
            fragmentExerciseRowMeineUebungens[index].setzeAktUebung(index, index + 1000, index + 2000, index + 3000);
            transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.tlTabelle, fragmentExerciseRowMeineUebungens[index], "exerciseRow");
            transaction.commit();

        } // for

        return inflater.inflate(R.layout.fragment_meine_uebungen, container, false);

    }

    public void anzahlUebungenBestimmen() {
        MainClass mainClass = (MainClass) getActivity();
        anzahlMeineUebungen = mainClass.gibAnzahlMeineUebungen();
    }

}