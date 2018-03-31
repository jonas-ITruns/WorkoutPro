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

    DatabaseReference mRootRef;
    View frView;
    ProgressBar progressBar;
    LayoutInflater pInflater;
    ViewGroup pContainer;
    private int anzahlStandardUebungen;
    private int index = 0;
    private ArrayList<String> mUebung = new ArrayList<>();
    private String [] mUebungArray1;
    private String [] mUebungArray2;
    private String sortierung;
    private int indexSortierung = 0;
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe = new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private ArrayList<String> mNameSortDatum = new ArrayList<>();
    private ArrayList<String> mMuskelgruppeSortDatum = new ArrayList<>();
    private ArrayList<String> mBeschreibungSortDatum = new ArrayList<>();
    private ArrayList<String> mNameSortAlphabet = new ArrayList<>();
    private ArrayList<String> mMuskelgruppeSortAlphabet = new ArrayList<>();
    private ArrayList<String> mBeschreibungSortAlphabet = new ArrayList<>();
    private SwipeRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        pInflater = inflater;
        pContainer = container;
        frView = inflater.inflate(R.layout.fr_standard_uebungen, container, false);

        progressBar = frView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        uebungenHolen();

        return frView;
    }

    private void uebungenHolen() {
        // Anzahl, Name, Muskelgruppe und Beschreibung der Übungen aus der Datenbank holen

        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        mRootRef = database.getReference();

        // Anzahl holen
        DatabaseReference mAnzahlStandardUebungenRef = mRootRef.child("Standard Übungen");
        mAnzahlStandardUebungenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anzahlStandardUebungen = (int) dataSnapshot.getChildrenCount();

                uebungdetailsHolen();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uebungdetailsHolen() {
        // Namen, Muskelgruppe, Beschreibung holen
        DatabaseReference mUebungRef = mRootRef.child("Standard Übungen").child(Integer.toString(index + 1));
        mUebungRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNameSortDatum.add(dataSnapshot.child("Name").getValue(String.class));
                mMuskelgruppeSortDatum.add(dataSnapshot.child("Muskelgruppe").getValue(String.class));
                mBeschreibungSortDatum.add(dataSnapshot.child("Beschreibung").getValue(String.class));
                index++;

                // Immer wieder aufrufen, sobald der Wert geholt wurde, sonst Liste erstellen
                if (index != anzahlStandardUebungen) {
                    uebungdetailsHolen();
                } // if
                else {
                    sortieren();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode uebungHolen

    private void sortieren() {
        MainClass mainClass = (MainClass) getActivity();
        sortierung = mainClass.gibStandardUebungenSortierung();
        if (sortierung.equals("datum")) {
            mName = mNameSortDatum;
            mMuskelgruppe = mMuskelgruppeSortDatum;
            mBeschreibung = mBeschreibungSortDatum;
            initViews();
        } else if (sortierung.equals("name")) {
            nameSortieren();
        } else if (sortierung.equals("muskelgruppe")) {
            muskelgruppeSortieren();
        } // if
    } // Methode sortieren

    private void nameSortieren() {
        DatabaseReference mUebungRef = mRootRef.child("Standard Übungen").child(Integer.toString(indexSortierung + 1));
        mUebungRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUebung.add(dataSnapshot.child("Name").getValue() + "<" + dataSnapshot.child("Muskelgruppe").getValue() + ">" + dataSnapshot.child("Beschreibung").getValue());
                indexSortierung++;
                // Immer wieder aufrufen, sobald der Wert geholt wurde, sonst sortieren
                if (indexSortierung != anzahlStandardUebungen) {
                    nameSortieren();
                } // if
                else {
                    Collections.sort(mUebung, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return s1.compareToIgnoreCase(s2);
                        }
                    });

                    mUebungArray1 = new String[2];
                    mUebungArray2 = new String[2];

                    for (int index = 0; index < anzahlStandardUebungen; index++) {
                        mUebungArray1 = mUebung.get(index).split("<");
                        mNameSortAlphabet.add(mUebungArray1[0]);
                        mUebungArray2 = mUebungArray1[1].split(">");
                        mMuskelgruppeSortAlphabet.add(mUebungArray2[0]);
                        mBeschreibungSortAlphabet.add(mUebungArray2[1]);
                    } // for

                    mName = mNameSortAlphabet;
                    mMuskelgruppe = mMuskelgruppeSortAlphabet;
                    mBeschreibung = mBeschreibungSortAlphabet;

                    initViews();
                } // if
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode nameSortieren

    private void muskelgruppeSortieren() {
        DatabaseReference mUebungRef = mRootRef.child("Standard Übungen").child(Integer.toString(indexSortierung + 1));
        mUebungRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUebung.add(dataSnapshot.child("Muskelgruppe").getValue() + "<" + dataSnapshot.child("Name").getValue() + ">" + dataSnapshot.child("Beschreibung").getValue());
                indexSortierung++;
                // Immer wieder aufrufen, sobald der Wert geholt wurde, sonst sortieren
                if (indexSortierung != anzahlStandardUebungen) {
                    muskelgruppeSortieren();
                } // if
                else {
                    Collections.sort(mUebung, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return s1.compareToIgnoreCase(s2);
                        }
                    });

                    mUebungArray1 = new String[2];
                    mUebungArray2 = new String[2];

                    for (int index = 0; index < anzahlStandardUebungen; index++) {
                        mUebungArray1 = mUebung.get(index).split("<");
                        mMuskelgruppeSortAlphabet.add(mUebungArray1[0]);
                        mUebungArray2 = mUebungArray1[1].split(">");
                        mNameSortAlphabet.add(mUebungArray2[0]);
                        mBeschreibungSortAlphabet.add(mUebungArray2[1]);
                    } // for

                    mName = mNameSortAlphabet;
                    mMuskelgruppe = mMuskelgruppeSortAlphabet;
                    mBeschreibung = mBeschreibungSortAlphabet;

                    initViews();
                } // if
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode muskelgruppeSortieren

    private void initViews(){
        recyclerView = frView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(getActivity(), mName, mMuskelgruppe, mBeschreibung);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

}