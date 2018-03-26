package com.developer.workoutpro.itruns.workoutpro;

import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Datenbank extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Bezug zur Datenbank
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //Bezug zum genauen Bereich in der Datenbank
    DatabaseReference mConditionRef = mRootRef.child("condition");

    @Override
    protected void onStart() {
        super.onStart();

        // Wartet darauf, dass etwas mit den Daten im Bereich "condition" passiert
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override

            // Wird ausgeführt, wenn sich Daten ändern
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Wenn sich die Daten ändern, wird der Text eingelesen
                String text = dataSnapshot.getValue(String.class);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Neuen Wert für den Bereich "condition" setzen
        mConditionRef.setValue("neuer Wert");
    }
}