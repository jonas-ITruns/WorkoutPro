package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FrWorkoutHinzufuegen extends Fragment implements SwipeRecyclerViewAdapterWorkoutAnsicht.OnStartDragListener {

    View frView;
    private int workoutNummer;
    private int anzahlWorkoutUebungen;
    private ArrayList<String> mMinuten = new ArrayList<>();
    private ArrayList<String> mSekunden = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe= new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private ArrayList<String> mDauer = new ArrayList<>();
    private static SwipeRecyclerViewAdapterWorkoutAnsicht adapter;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        frView = inflater.inflate(R.layout.fr_workout_hinzufuegen, container, false);

        MainClass mainClass = (MainClass) getActivity();

        // das zu bearbeitende Workout holen
        workoutNummer = mainClass.gibBearbeitendesWorkout();

        // Workountnamen anzeigen
        if (mainClass.gibWorkoutNameHinzugefuegt(workoutNummer)) {
            Button btnWorkoutName = frView.findViewById(R.id.btnWorkoutName);
            btnWorkoutName.setText(mainClass.gibWorkoutName(workoutNummer));
        }

        uebungenHolen();

        initViews();

        return frView;
    }

    private void uebungenHolen() {
        MainClass mainClass = (MainClass) getActivity();

        anzahlWorkoutUebungen = mainClass.gibWorkoutUebungAnzahl(workoutNummer);

        if (anzahlWorkoutUebungen == 0) {
            TextView tvHinweisUebungHinzufuegen = frView.findViewById(R.id.tvHinweisUebungHinzufuegen);
            tvHinweisUebungHinzufuegen.setVisibility(View.VISIBLE);
        }
        else {
            TextView tvHinweisUebungHinzufuegen = frView.findViewById(R.id.tvHinweisUebungHinzufuegen);
            tvHinweisUebungHinzufuegen.setVisibility(View.INVISIBLE);
            mName = new ArrayList<>();
            mMuskelgruppe = new ArrayList<>();
            mBeschreibung = new ArrayList<>();
            mDauer = new ArrayList<>();

            for (int index = 0; index < anzahlWorkoutUebungen; index++) {
                mName.add(mainClass.gibWorkoutUebungName(workoutNummer, index));
                mMuskelgruppe.add(mainClass.gibWorkoutUebungMuskelgruppe(workoutNummer, index));
                mBeschreibung.add(mainClass.gibWorkoutUebungBeschreibung(workoutNummer, index));
                mMinuten.add(Integer.toString(mainClass.gibWorkoutUebungMinuten(workoutNummer, index)));
                mSekunden.add(Integer.toString(mainClass.gibWorkoutUebungSekunden(workoutNummer, index)));
            } // for
        } // else
    }

    private void initViews(){
        recyclerView = frView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SwipeRecyclerViewAdapterWorkoutAnsicht(getActivity(), mName, mMuskelgruppe, mBeschreibung, mMinuten, mSekunden, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        //initSwipe();
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }


}
