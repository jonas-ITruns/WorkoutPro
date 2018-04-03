package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FrStandardUebungen extends Fragment {

    View frView;
    LayoutInflater pInflater;
    ViewGroup pContainer;
    private int anzahlStandardUebungen;
    private ArrayList<String> mUebungSort1 = new ArrayList<>();
    private ArrayList<String> mUebungSort2 = new ArrayList<>();
    private String [] mUebungArray1;
    private String [] mUebungArray2;
    private String [] mUebungArray3;
    private String sortierung;
    private ArrayList<String> mNummer = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe = new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private SwipeRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        pInflater = inflater;
        pContainer = container;
        frView = inflater.inflate(R.layout.fr_standard_uebungen, container, false);

        MainClass mainClass = (MainClass) getActivity();
        ProgressBar progressBarStandardUebungen = frView.findViewById(R.id.progressBarStandardUebungen);

        if(mainClass.gibSynchronisation()) {
            progressBarStandardUebungen.setVisibility(View.VISIBLE);
        } // then
        else {
            progressBarStandardUebungen.setVisibility(View.INVISIBLE);
            anzahlStandardUebungen = MainClass.gibAnzahlStandardUebungen();
            sortieren();
        } // else

        return frView;
    }

    private void sortieren() {
        MainClass mainClass = (MainClass) getActivity();
        sortierung = mainClass.gibStandardUebungenSortierung();
        if (sortierung.equals("datum")) {
            datumSortieren();
        } else if (sortierung.equals("name")) {
            nameSortieren();
        } else if (sortierung.equals("muskelgruppe")) {
            muskelgruppeSortieren();
        } // if
        initViews();
    } // Methode sortieren

    private void datumSortieren() {
        for (int index = 0; index < anzahlStandardUebungen; index++) {
            mUebungSort1.add(Integer.toString(MainClass.gibStandardUebungenNummer(index)) + "~" + MainClass.gibStandardUebungenName(index) + "<" + MainClass.gibStandardUebungenMuskelgruppe(index) + ">" + MainClass.gibStandardUebungenBeschreibung(index));
        } // for

        Collections.sort(mUebungSort1, new AlphanumComparator());

        mUebungArray1 = new String[2];
        mUebungArray2 = new String[2];
        mUebungArray3 = new String[2];

        mName = new ArrayList<>();
        mMuskelgruppe = new ArrayList<>();
        mBeschreibung = new ArrayList<>();

        MainClass mainClass = (MainClass) getActivity();

        for (int index = 0; index < anzahlStandardUebungen; index++) {
            mUebungArray1 = mUebungSort1.get(index).split("~");
            mNummer.add(mUebungArray1[0]);
            mUebungArray2 = mUebungArray1[1].split("<");
            mName.add(mUebungArray2[0]);
            mUebungArray3 = mUebungArray2[1].split(">");
            mMuskelgruppe.add(mUebungArray3[0]);
            mBeschreibung.add(mUebungArray3[1]);

            // Meine Übungen neu sortieren

            mainClass.setzeStandardUebungNummer(Integer.parseInt(mNummer.get(index)), index);
            mainClass.setzeStandardUebungName(mName.get(index), index);
            mainClass.setzeStandardUebungMuskelgruppe(mMuskelgruppe.get(index), index);
            mainClass.setzeStandardUebungBeschreibung(mBeschreibung.get(index), index);

        } // for
    } // Methode datumSortieren

    private void nameSortieren() {
        for (int index = 0; index < anzahlStandardUebungen; index++) {
            mUebungSort1.add(MainClass.gibStandardUebungenName(index) + "~" + MainClass.gibStandardUebungenMuskelgruppe(index) + "<" + Integer.toString(MainClass.gibStandardUebungenNummer(index)) + ">" + MainClass.gibStandardUebungenBeschreibung(index));
        } // for

        // Sortierung nach Zahlen
        Collections.sort(mUebungSort1, new AlphanumComparator());

        ArrayList<Character> buchstaben = new ArrayList<>();
        for (int index = 0; index < 26; index++) {
            buchstaben.add((char) (index + 65));
        } // for
        for (int index = 0; index < 26; index++) {
            buchstaben.add((char) (index + 97));
        } // for

        mUebungArray1 = new String[2];
        mUebungArray2 = new String[2];
        mUebungArray3 = new String[2];

        mName = new ArrayList<>();
        mMuskelgruppe = new ArrayList<>();
        mBeschreibung = new ArrayList<>();

        MainClass mainClass = (MainClass) getActivity();

        Boolean [] keinBuchstabe = new Boolean[anzahlStandardUebungen];
        int anzahlNichtBuchstaben = 0;

        for (int index = 0; index < anzahlStandardUebungen; index++) {
            keinBuchstabe[index] = false;
            for (int pIndex = 0; pIndex < buchstaben.size(); pIndex++) {
                if (! keinBuchstabe[index]) {
                    if (mUebungSort1.get(index).charAt(0) == buchstaben.get(pIndex)) {
                        mUebungSort2.add(mUebungSort1.get(index));
                        keinBuchstabe[index] = true;
                    } // if
                } // if
            } // for
            // Alles was kein Buchstabe ist schonmal hinzufügen
            if (! keinBuchstabe[index]) {
                mUebungArray1 = mUebungSort1.get(index).split("~");
                mName.add(mUebungArray1[0]);
                mUebungArray2 = mUebungArray1[1].split("<");
                mMuskelgruppe.add(mUebungArray2[0]);
                mUebungArray3 = mUebungArray2[1].split(">");
                mNummer.add(mUebungArray3[0]);
                mBeschreibung.add(mUebungArray3[1]);

                // Meine Übungen neu sortieren

                mainClass.setzeStandardUebungNummer(Integer.parseInt(mNummer.get(anzahlNichtBuchstaben)), anzahlNichtBuchstaben);
                mainClass.setzeStandardUebungName(mName.get(anzahlNichtBuchstaben), anzahlNichtBuchstaben);
                mainClass.setzeStandardUebungMuskelgruppe(mMuskelgruppe.get(anzahlNichtBuchstaben), anzahlNichtBuchstaben);
                mainClass.setzeStandardUebungBeschreibung(mBeschreibung.get(anzahlNichtBuchstaben), anzahlNichtBuchstaben);
                anzahlNichtBuchstaben++;
            } // if
        } // for

        // Sortierung nach Buchstaben
        Collections.sort(mUebungSort2, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        for (int index = anzahlNichtBuchstaben; index < anzahlStandardUebungen; index++) {
            mUebungArray1 = mUebungSort2.get(index - anzahlNichtBuchstaben).split("~");
            mName.add(mUebungArray1[0]);
            mUebungArray2 = mUebungArray1[1].split("<");
            mMuskelgruppe.add(mUebungArray2[0]);
            mUebungArray3 = mUebungArray2[1].split(">");
            mNummer.add(mUebungArray3[0]);
            mBeschreibung.add(mUebungArray3[1]);

            // Meine Übungen neu sortieren

            mainClass.setzeStandardUebungNummer(Integer.parseInt(mNummer.get(index)), index);
            mainClass.setzeStandardUebungName(mName.get(index), index);
            mainClass.setzeStandardUebungMuskelgruppe(mMuskelgruppe.get(index), index);
            mainClass.setzeStandardUebungBeschreibung(mBeschreibung.get(index), index);
        } // for
    } // Methode nameSortieren

    private void muskelgruppeSortieren() {
        for (int index = 0; index < anzahlStandardUebungen; index++) {
            mUebungSort1.add(MainClass.gibStandardUebungenMuskelgruppe(index) + "~" + MainClass.gibStandardUebungenName(index) + "<" + Integer.toString(MainClass.gibStandardUebungenNummer(index)) + ">" + MainClass.gibStandardUebungenBeschreibung(index));
        } // for

        Collections.sort(mUebungSort1, new AlphanumComparator());

        mUebungArray1 = new String[2];
        mUebungArray2 = new String[2];
        mUebungArray3 = new String[2];

        mName = new ArrayList<>();
        mMuskelgruppe = new ArrayList<>();
        mBeschreibung = new ArrayList<>();

        MainClass mainClass = (MainClass) getActivity();

        for (int index = 0; index < anzahlStandardUebungen; index++) {
            mUebungArray1 = mUebungSort1.get(index).split("~");
            mMuskelgruppe.add(mUebungArray1[0]);
            mUebungArray2 = mUebungArray1[1].split("<");
            mName.add(mUebungArray2[0]);
            mUebungArray3 = mUebungArray2[1].split(">");
            mNummer.add(mUebungArray3[0]);
            mBeschreibung.add(mUebungArray3[1]);

            // Meine Übungen neu sortieren

            mainClass.setzeStandardUebungNummer(Integer.parseInt(mNummer.get(index)), index);
            mainClass.setzeStandardUebungName(mName.get(index), index);
            mainClass.setzeStandardUebungMuskelgruppe(mMuskelgruppe.get(index), index);
            mainClass.setzeStandardUebungBeschreibung(mBeschreibung.get(index), index);

        } // for
    } // Methode muskelgruppeSortieren

    private void initViews(){
        recyclerView = frView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(getActivity(), mName, mMuskelgruppe, mBeschreibung, false);
        recyclerView.setAdapter(adapter);
    }

}