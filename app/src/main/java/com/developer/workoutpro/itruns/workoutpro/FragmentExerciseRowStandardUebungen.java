package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentExerciseRowStandardUebungen extends Fragment {

    // Generell
    View view;
    private boolean userUebung;

    // Aktuelle Übung
    private int aktUebung;

    // Name
    Button btnName;
    private String name;
    private int nameId;
    private boolean nameScrollt = false;

    // Muskelgruppe
    Button btnMuskelgruppe;
    private String muskelgruppe;
    private int muskelgruppeId;
    private boolean muskelgruppeScrollt = false;

    // Beschreibung
    Button btnBeschreibung;
    private String beschreibung;
    private int beschreibungId;
    private boolean beschreibungScrollt = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_exercise_row_standard_uebungen, container, false);

        // Datenbank
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Datenbank deklarieren
        String aktUebungStr = Integer.toString(aktUebung + 1);
        DatabaseReference mRootRef = database.getReference();

        DatabaseReference mNameRefChild;
        DatabaseReference mMuskelgruppeRefChild;
        DatabaseReference mBeschreibungRefChild;

        // Auf den Namen zugreifen
        mNameRefChild = mRootRef.child("Standard Übungen").child(aktUebungStr).child("Name");

        // Auf die Muskelgruppe zugreifen
        mMuskelgruppeRefChild = mRootRef.child("Standard Übungen").child(aktUebungStr).child("Muskelgruppe");

        // Auf die Beschreibung zugreifen
        mBeschreibungRefChild = mRootRef.child("Standard Übungen").child(aktUebungStr).child("Beschreibung");

        // Namen setzen
        mNameRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                btnName = view.findViewById(R.id.btnName);
                btnName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nameScrollt) {
                            btnName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            btnName.setSelected(true);
                            btnName.setSingleLine(true);
                            nameScrollt = true;
                        } // then
                        else {
                            btnName.setEllipsize(TextUtils.TruncateAt.END);
                            btnName.setSelected(true);
                            btnName.setSingleLine(true);
                            nameScrollt = false;
                        } // else
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
                        if (!muskelgruppeScrollt) {
                            btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            btnMuskelgruppe.setSelected(true);
                            btnMuskelgruppe.setSingleLine(true);
                            muskelgruppeScrollt = true;
                        } // then
                        else {
                            btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.END);
                            btnMuskelgruppe.setSelected(true);
                            btnMuskelgruppe.setSingleLine(true);
                            muskelgruppeScrollt = false;
                        } // else
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
                        if (!beschreibungScrollt) {
                            btnBeschreibung.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            btnBeschreibung.setSelected(true);
                            btnBeschreibung.setSingleLine(true);
                            beschreibungScrollt = true;
                        } // then
                        else {
                            btnBeschreibung.setEllipsize(TextUtils.TruncateAt.END);
                            btnBeschreibung.setSelected(true);
                            btnBeschreibung.setSingleLine(true);
                            beschreibungScrollt = false;
                        } // else
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
