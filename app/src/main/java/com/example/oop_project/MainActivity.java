package com.example.oop_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
//This is the main activity where we make sure each fragment works and this is also where the navigation menu resides.
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Switch darkModeSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavigationMenuTextColor(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_dark_mode_switch);
        darkModeSwitch = (Switch) menuItem.getActionView().findViewById(R.id.dark_mode_switch);
        darkModeSwitch.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                Toast.makeText(MainActivity.this, "Dark mode enabled", Toast.LENGTH_SHORT).show();

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                Toast.makeText(MainActivity.this, "Dark mode disabled", Toast.LENGTH_SHORT).show();
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DiscoverFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_discover);
        }

    }

    //This is to make sure each fragment is shown correct when the corresponding menu option is chosen.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == R.id.nav_discover) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DiscoverFragment()).commit();
        } else if (itemId == R.id.nav_quiz) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuizFragment()).commit();
        } else if (itemId == R.id.nav_compare) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CompareFragment()).commit();
        } else if (itemId == R.id.nav_info) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).commit();
        }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        @Override
        public void onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    //This is to make sure the layout, themes and colour changes correctly when the dark mode is on.
    private void updateNavigationMenuTextColor(boolean isDarkMode) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        int textColor = isDarkMode ? Color.WHITE : Color.BLACK; // If dark mode, color will be white. If not, it'll be black.
        int iconTint = isDarkMode ? Color.WHITE : Color.BLACK;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new ForegroundColorSpan(textColor), 0, spannableString.length(), 0);
            menuItem.setTitle(spannableString);

            Drawable icon = menuItem.getIcon();
            if (icon != null) {
                icon.setTint(iconTint);
                menuItem.setIcon(icon);
            }  else {
                // Log a message if the icon is null (for debugging purposes)
                Log.d("IconNullCheck", "Icon is null for menu item: " + menuItem.getTitle());
            }
        }
    }

    }


