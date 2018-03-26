package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentExercises extends Fragment {

    FragmentExerciseRow fragmentExerciseRow [] = new FragmentExerciseRow[15];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Rows in der Tabelle hinzufügen
        FragmentTransaction transaction;

        for (int index = 0; index < 15; index++) {

            fragmentExerciseRow[index] = new FragmentExerciseRow();
            fragmentExerciseRow[index].setzeAktUebung(index);
            transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.tlTabelle, fragmentExerciseRow[index], "exerciseRow");
            transaction.commit();

        } // for

        return inflater.inflate(R.layout.fragment_exercises, container, false);

    }
}