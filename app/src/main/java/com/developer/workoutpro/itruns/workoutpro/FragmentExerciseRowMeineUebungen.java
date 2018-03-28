package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentExerciseRowMeineUebungen extends Fragment {

    // Generell
    View view;

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

        view = inflater.inflate(R.layout.fragment_exercise_row_meine_uebungen, container, false);

        MainClass mainClass = (MainClass) getActivity();
        // Name setzen
        name = mainClass.gibMeineUebungenName(aktUebung);
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

        // Muskelgruppe setzen
        muskelgruppe = mainClass.gibMeineUebungenMuskelgruppe(aktUebung);
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

        // Beschreibung setzen
        beschreibung = mainClass.gibMeineUebungenBeschreibung(aktUebung);
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

        ImageButton imgbtnUebungLoeschen = view.findViewById(R.id.imgbtnUebungLoeschen);
        imgbtnUebungLoeschen.setTag(aktUebung);

        return view;
    }

    public void setzeAktUebung(int pAktUebung, int pIdName, int pIdMuskelgruppe, int pIdBeschreibung) {
        aktUebung = pAktUebung;
        nameId = pIdName;
        muskelgruppeId = pIdMuskelgruppe;
        beschreibungId = pIdBeschreibung;
    } // Methode setzeAtkUebung

}
