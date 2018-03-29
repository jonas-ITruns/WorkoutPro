package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentMeineUebungen extends Fragment {

    ListView listview;
    private static int anzahlMeineUebungen;
    private String [] name;
    private String [] muskelgruppe;
    private String [] beschreibung;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meine_uebungen, container, false);

        anzahlMeineUebungen = MainClass.gibAnzahlMeineUebungen();

        name = new String[anzahlMeineUebungen];
        muskelgruppe = new String[anzahlMeineUebungen];
        beschreibung = new String[anzahlMeineUebungen];

        for (int index = 0; index < anzahlMeineUebungen; index++) {
            name[index] = MainClass.gibMeineUebungenName(index);
            muskelgruppe[index] = MainClass.gibMeineUebungenMuskelgruppe(index);
            beschreibung[index] = MainClass.gibMeineUebungenBeschreibung(index);
        } // for

        listview = view.findViewById(R.id.listview);
        listview.setAdapter(new yourAdapter(getActivity(), name, muskelgruppe, beschreibung));

       /* FragmentExerciseRowMeineUebungen fragmentExerciseRowMeineUebungens[] = new FragmentExerciseRowMeineUebungen[anzahlMeineUebungen];

        // Rows in der Tabelle hinzufügen
        FragmentTransaction transaction;

        for (int index = 0; index < anzahlMeineUebungen; index++) {

            fragmentExerciseRowMeineUebungens[index] = new FragmentExerciseRowMeineUebungen();
            fragmentExerciseRowMeineUebungens[index].setzeAktUebung(index, index + 1000, index + 2000, index + 3000);
            transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.tlTabelle, fragmentExerciseRowMeineUebungens[index], "exerciseRow");
            transaction.commit();

        } // for*/

        return view;
    }

}