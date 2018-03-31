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

public class FrStandardUebungen extends Fragment {

    DatabaseReference mRootRef;
    View frView;
    ProgressBar progressBar;
    LayoutInflater pInflater;
    ViewGroup pContainer;
    private int anzahlStandardUebungen;
    private int index = 0;
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

                uebungHolen();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uebungHolen() {
        // Namen, Muskelgruppe, Beschreibung holen
        DatabaseReference mUebungRef = mRootRef.child("Standard Übungen").child(Integer.toString(index + 1));
        mUebungRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mName.add(dataSnapshot.child("Name").getValue(String.class));
                mMuskelgruppe.add(dataSnapshot.child("Muskelgruppe").getValue(String.class));
                mBeschreibung.add(dataSnapshot.child("Beschreibung").getValue(String.class));
                index++;

                // Immer wieder aufrufen, sobald der Wert geholt wurde, sonst Liste erstellen
                if (index != anzahlStandardUebungen) {
                    uebungHolen();
                } // if
                else {
                    initViews();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode uebungHolen

    private void initViews(){
        recyclerView = frView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(getActivity(), mName, mMuskelgruppe, mBeschreibung);
        recyclerView.setAdapter(adapter);
    }

}