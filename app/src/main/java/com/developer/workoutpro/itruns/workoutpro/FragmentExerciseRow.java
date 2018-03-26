package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentExerciseRow extends Fragment {

    // Generell
    View view;
    Button btnName;
    Button btnMuskelgruppe;
    Button btnBeschreibung;

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
                btnName = view.findViewById(R.id.btnName);
                btnName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        btnName.setSelected(true);
                        btnName.setSingleLine(true);
                    }
                });
                btnName.setText(name);
                btnName.setId(nameId);
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
                btnMuskelgruppe = view.findViewById(R.id.btnMuskelgruppe);

                btnMuskelgruppe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        btnMuskelgruppe.setSelected(true);
                        btnMuskelgruppe.setSingleLine(true);
                    }
                });
                btnMuskelgruppe.setText(muskelgruppe);
                btnMuskelgruppe.setId(muskelgruppeId);
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
                btnBeschreibung = view.findViewById(R.id.btnBeschreibung);
                btnBeschreibung.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnBeschreibung.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        btnBeschreibung.setSelected(true);
                        btnBeschreibung.setSingleLine(true);
                    }
                });
                btnBeschreibung.setText(beschreibung);
                btnBeschreibung.setId(beschreibungId);
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
