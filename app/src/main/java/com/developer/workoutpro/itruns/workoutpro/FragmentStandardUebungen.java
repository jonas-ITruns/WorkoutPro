package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentStandardUebungen extends Fragment {

    private int anzahlStandardUebungen;
    private static int SPLASH_TIME_OUT = 2500;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        anzahlUebungenBestimmen();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                FragmentExerciseRowStandardUebungen fragmentExerciseRowStandardUebungens[] = new FragmentExerciseRowStandardUebungen[anzahlStandardUebungen];

                // Rows in der Tabelle hinzufügen
                FragmentTransaction transaction;

                for (int index = 0; index < anzahlStandardUebungen; index++) {

                    fragmentExerciseRowStandardUebungens[index] = new FragmentExerciseRowStandardUebungen();
                    fragmentExerciseRowStandardUebungens[index].setzeAktUebung(index, index + 1000, index + 2000, index + 3000);
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.tlTabelle, fragmentExerciseRowStandardUebungens[index], "exerciseRow");
                    transaction.commit();

                } // for
            }

        },SPLASH_TIME_OUT);

        return inflater.inflate(R.layout.fragment_standard_uebungen, container, false);

    }

    public void anzahlUebungenBestimmen() {
        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        DatabaseReference mRootRef = database.getReference();

        // Anzahl Übungen erneuern
        DatabaseReference mAnzahlStandardUebungenRef = mRootRef.child("Standard Übungen").child("Gesamt Anzahl");
        mAnzahlStandardUebungenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anzahlStandardUebungen = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode anzahlUebungenBestimmen

}