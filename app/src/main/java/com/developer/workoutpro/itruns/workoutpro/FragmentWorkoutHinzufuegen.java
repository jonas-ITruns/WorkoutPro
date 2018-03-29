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

public class FragmentWorkoutHinzufuegen extends Fragment {

    private static final String TAG = "Workout Hinzufuegen";
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_hinzufuegen, container, false);

        Log.d(TAG, "onCreate: Starting.");

        mSectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());

        // Set up ViewPager with the sections adapter
        mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentWorkoutHinzufuegenWorkout(), "Workout");
        adapter.addFragment(new FragmentWorkoutHinzufuegenTabata(), "Tabata");
        viewPager.setAdapter(adapter);
    }

}
