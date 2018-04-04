package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FrWorkoutHinzufuegen extends Fragment {

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
    private Paint p = new Paint();

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
        adapter = new SwipeRecyclerViewAdapterWorkoutAnsicht(getActivity(), mName, mMuskelgruppe, mBeschreibung, mMinuten, mSekunden);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        //initSwipe();
        //adapter.notifyDataSetChanged();
    }

    /*private void initSwipe(){
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
                    adapter.removeItem(position);
                } // then
                else {
                    adapter.removeItem(position);
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
    }*/

}
