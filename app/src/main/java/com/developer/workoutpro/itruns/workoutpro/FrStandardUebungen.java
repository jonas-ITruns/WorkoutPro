package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FrStandardUebungen extends Fragment {

    // View
    View view;

    // Datenbank
    DatabaseReference mRootRef;

    // Anzahl, Name, Muskelgruppe und Beschreibung
    private int anzahlStandardUebungen;
    private int index = 0;
    private String [] name;
    private String [] muskelgruppe;
    private String [] beschreibung;

    // Listview
    ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_standard_uebungen, container, false);

        uebungenBestimmen();

        return view;
    }

    public void uebungenBestimmen() {
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
                name = new String[anzahlStandardUebungen];
                muskelgruppe = new String[anzahlStandardUebungen];
                beschreibung = new String[anzahlStandardUebungen];

                uebungHolen();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    } // Methode uebungenBestimmen

    public void uebungHolen() {
        // Namen, Muskelgruppe, Beschreibung holen
        DatabaseReference mUebungRef = mRootRef.child("Standard Übungen").child(Integer.toString(index + 1));
        mUebungRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name[index] = dataSnapshot.child("Name").getValue(String.class);
                muskelgruppe[index] = dataSnapshot.child("Muskelgruppe").getValue(String.class);
                beschreibung[index] = dataSnapshot.child("Beschreibung").getValue(String.class);
                index++;

                // Immer wieder aufrufen, sobald der Wert geholt wurde, sonst Liste erstellen
                if (index != anzahlStandardUebungen) {
                    uebungHolen();
                } // if
                else {
                    listview = view.findViewById(R.id.listview);
                    listview.setAdapter(new ListViewAdapterStandardUebungen(getActivity(), name, muskelgruppe, beschreibung));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // Methode uebungHolen

} // Klasse FrStandardUebungen