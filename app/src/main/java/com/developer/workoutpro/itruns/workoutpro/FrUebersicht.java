package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FrUebersicht extends Fragment {

    private int anzahlWorkouts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_uebersicht, container, false);

        MainClass mainClass = (MainClass) getActivity();

        anzahlWorkouts = mainClass.gibAnzahlWorkouts();
        FrUebersichtRow frUebersichtRows[] = new FrUebersichtRow[anzahlWorkouts];

        TextView tvHinweisWorkoutHinzufuegen = view.findViewById(R.id.tvHinweisWorkoutHinzufuegen);
        if (anzahlWorkouts == 0) {
            tvHinweisWorkoutHinzufuegen.setVisibility(View.VISIBLE);
        } // then
        else {
            tvHinweisWorkoutHinzufuegen.setVisibility(View.INVISIBLE);
        } // else

        // Rows in der Tabelle hinzufügen
        FragmentTransaction transaction;

        for (int index = 0; index < anzahlWorkouts; index++) {

            frUebersichtRows[index] = new FrUebersichtRow();
            frUebersichtRows[index].setzeAktWorkout(index, index + 1000, index + 2000, index + 3000, index + 4000);
            transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.tlTabelle, frUebersichtRows[index], "exerciseRow");
            transaction.commit();

        } // for

        return view;

    }
}
