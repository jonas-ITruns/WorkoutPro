package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentExerciseRow extends Fragment {

    // Generell
    View view;
    TextView tvName;
    TextView tvMuskelgruppe;
    TextView tvBeschreibung;

    // Aktuelle Übung
    private int aktUebung;

    // Name
    private String name;
    private int nameId;

    // Muskelgruppe
    private String muskelgruppe;
    private int muskelgruppeId;

    // Beschreibung
    private String beschreibung;
    private int beschreibungId;

    // Datenbank
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise_row, container, false);

        // Datenbank deklarieren
        String aktUebungStr = Integer.toString(aktUebung + 1);
        DatabaseReference mRootRef = database.getReference();

        // Auf den Namen zugreifen
        DatabaseReference mNameRefChild = mRootRef.child(aktUebungStr).child("Name");

        // Auf die Muskelgruppe zugreifen
        DatabaseReference mMuskelgruppeRefChild = mRootRef.child(aktUebungStr).child("Muskelgruppe");

        // Auf die Beschreibung zugreifen
        DatabaseReference mBeschreibungRefChild = mRootRef.child(aktUebungStr).child("Beschreibung");

        // Namen setzen
        mNameRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                tvName = view.findViewById(R.id.tvName);
                tvName.setText(name);
                tvName.setId(nameId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Muskelgruppe setzen
        mMuskelgruppeRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                muskelgruppe = dataSnapshot.getValue(String.class);
                tvMuskelgruppe = view.findViewById(R.id.tvMuskelgruppe);
                tvMuskelgruppe.setText(muskelgruppe);
                tvMuskelgruppe.setId(muskelgruppeId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Beschreibung setzen
        mBeschreibungRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                beschreibung = dataSnapshot.getValue(String.class);
                tvBeschreibung = view.findViewById(R.id.tvBeschreibung);
                tvBeschreibung.setText(beschreibung);
                tvBeschreibung.setId(beschreibungId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void setzeAktUebung(int pAktUebung, int pIdName, int pIdMuskelgruppe, int pIdBeschreibung) {
        aktUebung = pAktUebung;
        nameId = pIdName;
        muskelgruppeId = pIdMuskelgruppe;
        beschreibungId = pIdBeschreibung;
    } // Methode setzeAtkUebung

}
