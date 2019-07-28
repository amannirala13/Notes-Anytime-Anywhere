package com.asdev.naa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.asdev.naa.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private notes notesFragment;
    private schedule scheduleFragment;
    private settings settingsFragment;
    private com.asdev.naa.fragments.shared sharedFragment;

    private BottomNavigationView mainBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBottomNav = findViewById(R.id.main_bottom_navBar);
        mainBottomNav.setOnNavigationItemSelectedListener(ItemSelectedListener);

        notesFragment = new notes();
        scheduleFragment = new schedule();
        settingsFragment = new settings();
        sharedFragment = new shared();

        //Load default Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_holder, notesFragment, null);
        fragmentTransaction.commit();

    }

    //Bottom Navigation Click Listener
    private BottomNavigationView.OnNavigationItemSelectedListener ItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_note:
                    switchFragment(notesFragment);
                    return true;

                case R.id.navigation_schedule:
                    switchFragment(scheduleFragment);
                    return true;

                case R.id.navigation_settings:
                    switchFragment(settingsFragment);
                    return true;

                case R.id.navigation_shared:
                    switchFragment(sharedFragment);
                    return true;

                default:
                    return false;
            }
        }
    };

    //Swaps frames in the Frame Holder
    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment, null);
        fragmentTransaction.commit();
    }
}
