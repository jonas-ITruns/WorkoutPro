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


public class FragmentMeineUebungen extends Fragment {

    private int anzahlMeineUebungen;
    private String benutzername;
    private static int SPLASH_TIME_OUT = 1000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        anzahlUebungenBestimmen();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                FragmentExerciseRow fragmentExerciseRow[] = new FragmentExerciseRow[anzahlMeineUebungen];

                // Rows in der Tabelle hinzufügen
                FragmentTransaction transaction;

                for (int index = 0; index < anzahlMeineUebungen; index++) {

                    fragmentExerciseRow[index] = new FragmentExerciseRow();
                    fragmentExerciseRow[index].setzeAktUebung(index, index + 1000, index + 2000, index + 3000, true, benutzername);
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.tlTabelle, fragmentExerciseRow[index], "exerciseRow");
                    transaction.commit();

                } // for
            }

        },SPLASH_TIME_OUT);

        return inflater.inflate(R.layout.fragment_meine_uebungen, container, false);

    }

    public void anzahlUebungenBestimmen() {
        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        DatabaseReference mRootRef = database.getReference();

        // Anzahl Übungen erneuern
        DatabaseReference mAnzahlMeineUebungenRef = mRootRef.child("Benutzer Verwaltung").child(benutzername).child("Übungen").child("Gesamt Anzahl");
        mAnzahlMeineUebungenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anzahlMeineUebungen = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode anzahlUebungenBestimmen

    public void setzeBenutzernamen(String pBenutzername) {
        benutzername = pBenutzername;
    } // Methode setzeBenutzernamen

}