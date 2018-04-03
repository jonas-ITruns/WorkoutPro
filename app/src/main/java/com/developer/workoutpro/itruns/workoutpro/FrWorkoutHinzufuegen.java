package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class FrWorkoutHinzufuegen extends Fragment {

    View frView;
    private int anzahlWorkoutUebungen;
    private ArrayList<String> mUebung = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe= new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private static SwipeRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        frView = inflater.inflate(R.layout.fr_workout_hinzufuegen, container, false);

        // Workountnamen anzeigen
        MainClass mainClass = (MainClass) getActivity();
        if (mainClass.gibWorkoutNameHinzugefuegt(mainClass.gibAnzahlWorkouts())) {
            Button btnWorkoutName = frView.findViewById(R.id.btnWorkoutName);
            btnWorkoutName.setText(mainClass.gibWorkoutName(mainClass.gibAnzahlWorkouts()));
        }

        uebungenHolen();

        initViews();

        return frView;
    }

    private void uebungenHolen() {
        MainClass mainClass = (MainClass) getActivity();

        anzahlWorkoutUebungen = mainClass.gibWorkoutUebungAnzahl();

        if (anzahlWorkoutUebungen == 0) {
            mName = new ArrayList<>();
            mMuskelgruppe = new ArrayList<>();
            mBeschreibung = new ArrayList<>();
            mName.add("Planche");
            mMuskelgruppe.add("ganzkoerper");
            mBeschreibung.add("geile scheiße");
        }
        else {
            mName = new ArrayList<>();
            mMuskelgruppe = new ArrayList<>();
            mBeschreibung = new ArrayList<>();

            for (int index = 0; index < anzahlWorkoutUebungen; index++) {
                mName.add(mainClass.gibWorkoutUebungName(index));
                mMuskelgruppe.add(mainClass.gibWorkoutUebungMuskelgruppe(index));
                mBeschreibung.add(mainClass.gibWorkoutUebungBeschreibung(index));
            } // for
        } // else
    }

    private void initViews(){
        recyclerView = frView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(getActivity(), mName, mMuskelgruppe, mBeschreibung, false);
        recyclerView.setAdapter(adapter);
        initSwipe();

        //adapter.notifyDataSetChanged();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Nach links swipen; für nach rechts swipen else Teil
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position, true);
                } // then
                else {
                    adapter.removeItem(position, true);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#40a50000"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                    } else {
                        p.setColor(Color.parseColor("#40a50000"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
