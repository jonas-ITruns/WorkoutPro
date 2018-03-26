package com.developer.workoutpro.itruns.workoutpro;

        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.support.constraint.ConstraintLayout;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.TextView;

public class MainClass extends AppCompatActivity {

    public int aktUebung = 1;

    // Menüleiste
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    // Fragmente
    private String aktFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menueleiste();
    } // Methode onCreate

    @Override
    protected void onPause() {
        super.onPause();
        Fragment myFragment;
        myFragment = getFragmentManager().findFragmentByTag("overview");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "overview";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("exercises");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "exercises";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("premium");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "premium";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("support");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "support";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("settings");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "settings";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("workoutZeit");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "workoutZeit";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("workoutWiederholung");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "workoutWiederholung";
        } // if
        myFragment = getFragmentManager().findFragmentByTag("tabata");
        if (myFragment != null && myFragment.isVisible()) {
            aktFragment = "tabata";
        } // if
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.bereichFragments)).commit();
    } // Methode onPause

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (aktFragment.equals ("overview") || aktFragment.equals ("")) {
            FragmentOverview fragmentOverview = new FragmentOverview();
            fragmentTransaction.add(R.id.bereichFragments, fragmentOverview, "overview");
        } // if
        else if (aktFragment.equals ("exercises")) {
            FragmentExercises fragmentExercises = new FragmentExercises();
            fragmentTransaction.add(R.id.bereichFragments, fragmentExercises, "exercises");
        } // if
        else if (aktFragment.equals ("premium")) {
            FragmentPremium fragmentPremium = new FragmentPremium();
            fragmentTransaction.add(R.id.bereichFragments, fragmentPremium, "premium");
        } // if
        else if (aktFragment.equals ("support")) {
            FragmentSupport fragmentSupport = new FragmentSupport();
            fragmentTransaction.add(R.id.bereichFragments, fragmentSupport, "support");
        } // if
        else if (aktFragment.equals ("settings")) {
            FragmentSettings fragmentSettings = new FragmentSettings();
            fragmentTransaction.add(R.id.bereichFragments, fragmentSettings, "settings");
        } // if
        else if (aktFragment.equals ("workoutZeit")) {
            FragmentWorkoutZeit fragmentWorkoutZeit = new FragmentWorkoutZeit();
            fragmentTransaction.add(R.id.bereichFragments, fragmentWorkoutZeit, "workoutZeit");
        } // if
        else if (aktFragment.equals ("workoutWiederholung")) {
            FragmentWorkoutWiederholung fragmentWorkoutWiederholung = new FragmentWorkoutWiederholung();
            fragmentTransaction.add(R.id.bereichFragments, fragmentWorkoutWiederholung, "workoutWiederholung");
        } // if
        else if (aktFragment.equals ("tabata")) {
            FragmentTabata fragmentTabata = new FragmentTabata();
            fragmentTransaction.add(R.id.bereichFragments, fragmentTabata, "tabata");
        } // if
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode onResume

    @Override
    public void onBackPressed() {
        // Menüleiste schließen, falls sie geöffnent ist
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } // then
        else {
            super.onBackPressed();
        } // else
    } // Methode onBackPressed

    public void menueleiste() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Hintergrund dunkler machen
        mDrawerLayout.setScrimColor(Color.parseColor("#33000000"));

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Start-Up aktuelles Item markieren
        navigationView.setCheckedItem(R.id.overview);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // aktuelles Item markieren
                        menuItem.setChecked(true);

                        // nach dem Auswählen den Navigator wieder schließen
                        mDrawerLayout.closeDrawers();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.overview:
                                FragmentOverview fragmentOverview = new FragmentOverview();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentOverview, "overview");
                                break;
                            case R.id.exercises:
                                FragmentExercises fragmentExercises = new FragmentExercises();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentExercises, "exercises");
                                break;
                            case R.id.premium:
                                FragmentPremium fragmentPremium = new FragmentPremium();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentPremium, "premium");
                                break;
                            case R.id.support:
                                FragmentSupport fragmentSupport = new FragmentSupport();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSupport, "support");
                                break;
                            case R.id.settings:
                                FragmentSettings fragmentSettings = new FragmentSettings();
                                fragmentTransaction.replace(R.id.bereichFragments, fragmentSettings, "settings");
                                break;
                        } // switch

                        // Änderung sofort durchführen
                        fragmentManager.executePendingTransactions();
                        fragmentTransaction.commit();

                        return true;
                    }
                });

        // Menü über Button öffnen
        menuButton = findViewById(R.id.imgbtnMenue);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    } // Methode menueLeiste


    // Verschiedene Workoutarten öffnen


    public void workoutZeitOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutZeit fragmentWorkoutZeit = new FragmentWorkoutZeit();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutZeit, "workoutZeit");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutZeitOeffnen

    public void workoutWiederholungOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentWorkoutWiederholung fragmentWorkoutWiederholung = new FragmentWorkoutWiederholung();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentWorkoutWiederholung, "workoutWiederholung");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode workoutWiederholungOeffnen

    public void tabataOeffnen(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentTabata fragmentTabata = new FragmentTabata();
        fragmentTransaction.replace(R.id.bereichFragments, fragmentTabata, "tabata");
        fragmentManager.executePendingTransactions();
        fragmentTransaction.commit();
    } // Methode tabataOeffnen

    /*public void createTextView(String name, int id, View view) {
        TextView tv = new TextView(this);
        tv.setText(name);
        tv.setId(id);
        tv.setWidth(110);
        tv.setHeight(50);
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(000000);
        tv.setTextSize(14);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams)tv.getLayoutParams();
        p.leftMargin = 0;
        p.topMargin = 0;
        p.bottomMargin = 0;
        tv.setLayoutParams(p);
        ((ConstraintLayout) view).addView(tv);
    }*/

} // Klasse WelcomeScreen
