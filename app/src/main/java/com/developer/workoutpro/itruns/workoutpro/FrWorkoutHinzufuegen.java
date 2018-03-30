package com.developer.workoutpro.itruns.workoutpro;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FrWorkoutHinzufuegen extends Fragment {

    private static final String TAG = "Workout Hinzufuegen";
    private SwipeViewAdapter mSwipeViewAdapter;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_workout_hinzufuegen, container, false);

        Log.d(TAG, "onCreate: Starting.");

        mSwipeViewAdapter = new SwipeViewAdapter(getChildFragmentManager());

        // Set up ViewPager with the sections adapter
        mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SwipeViewAdapter adapter = new SwipeViewAdapter(getChildFragmentManager());
        adapter.addFragment(new FrWorkoutHinzufuegenWorkout(), "Workout");
        viewPager.setAdapter(adapter);
    }

}
