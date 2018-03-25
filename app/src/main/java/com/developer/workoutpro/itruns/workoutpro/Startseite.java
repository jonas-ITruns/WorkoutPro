package com.developer.workoutpro.itruns.workoutpro;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;

public class Startseite extends AppCompatActivity {

    // Menüleiste:
    private DrawerLayout mDrawerLayout;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startseite);

        menueleiste();
    }

    public void menueleiste() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch(menuItem.getItemId()) {
                            case R.id.overview:
                                setContentView(R.layout.activity_startseite);
                                break;
                            case R.id.exercises:
                                setContentView(R.layout.activity_uebungen_uebersicht);
                                break;
                            case R.id.premium:
                                setContentView(R.layout.activity_premium);
                                break;
                            case R.id.support:
                                setContentView(R.layout.activity_support);
                                break;
                            case R.id.settings:
                                setContentView(R.layout.activity_settings);
                                break;
                        } // switch

                        menueleiste();
                        mDrawerLayout.openDrawer(GravityCompat.START);

                        return true;
                    }
                });

        menuButton = findViewById(R.id.imgbtnMenue);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    } // Methode menueLeiste

}
