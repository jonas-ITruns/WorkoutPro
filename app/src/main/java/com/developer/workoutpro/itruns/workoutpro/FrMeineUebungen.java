package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FrMeineUebungen extends Fragment {

    View view;
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe = new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_meine_uebungen, container, false);

        uebungenHolen();

        return view;
    }

    private void uebungenHolen() {
        // Anzahl, Name, Muskelgruppe und Beschreibung der Übungen aus der MainClass holen
        int anzahlMeineUebungen = MainClass.gibAnzahlMeineUebungen();

        for (int index = 0; index < anzahlMeineUebungen; index++) {
            mName.add(MainClass.gibMeineUebungenName(index));
            mMuskelgruppe.add(MainClass.gibMeineUebungenMuskelgruppe(index));
            mBeschreibung.add(MainClass.gibMeineUebungenBeschreibung(index));
        } // for

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapterMeineUebungen adapter = new RecyclerViewAdapterMeineUebungen(getActivity(), mName, mMuskelgruppe, mBeschreibung);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
