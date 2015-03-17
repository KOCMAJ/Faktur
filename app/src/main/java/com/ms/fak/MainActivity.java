package com.ms.fak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ms.fak.auto.Auto;
import com.ms.fak.entities.Faktura;
import com.ms.fak.entities.Komitent;
import com.ms.fak.entities.Opis;
import com.ms.fak.faktura.FakturaFragment;
import com.ms.fak.faktura.FakturaFragmentDetail;
import com.ms.fak.komitent.KomitentFragment;
import com.ms.fak.komitent.KomitentFragmentDetail;
import com.ms.fak.memorandum.Memorandum;
import com.ms.fak.opis.OpisFragment;
import com.ms.fak.opis.OpisFragmentDetail;
import com.ms.fak.pdf.PdfPregled;
import com.ms.fak.utl.DeviderItemDecoration;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.MenuAdapter;
import com.ms.fak.utl.Updater;


public class MainActivity extends ActionBarActivity implements FragmentsCallbacks {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;

    private String[] menuItems;
    private String[] tags;

    private String currentTag;
    private int level;
    private Fragment currentFragment;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.left_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(5);

        title = drawerTitle = getTitle();
        menuItems = getResources().getStringArray(R.array.menu_items);
        tags = getResources().getStringArray(R.array.menu_tags);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MenuAdapter(menuItems, this));
        recyclerView.addItemDecoration(new DeviderItemDecoration(this, getResources().getIntArray(R.array.menu_underlined)));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_SETTLING) {
                    boolean isOpen = drawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT);
                    if (!isOpen) {
                        // počinje da se otvara drawer
                        if (currentFragment instanceof Updater) {
                            Updater updater = (Updater) currentFragment;
                            updater.gasiDekor(MainActivity.this);
                        }
                    }
                }
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        if (savedInstanceState == null) {
            selectMenuItem(0);
            drawerLayout.openDrawer(recyclerView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_about:
                Toast.makeText(this, "O ovoj aplikaciji...", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(this.title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(recyclerView);
    }

    @Override
    public boolean isFirstTime() {
        return firstTime;
    }

    @Override
    public void setFirstTime(boolean value) {
        firstTime = value;
    }

    @Override
    public void onCallForDetail(Object obj) {
        selectDetail(obj);
    }

    @Override
    public void onMenuItemClick(View view, final int position) {
        if (tags[position].equals(currentTag) && level == 0) {
            drawerLayout.closeDrawer(recyclerView);
            return;
        }
        if (currentFragment != null && currentFragment instanceof Updater) {
            final Updater updater = (Updater) currentFragment;
            if (updater.isModified()) {
                String title = getString(R.string.izmene);
                String message = getString(R.string.da_snimim);
                new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.snimi), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                drawerLayout.closeDrawer(recyclerView);
                                // sad proveri validnost
                                if (updater.isValid(MainActivity.this)) {
                                    updater.saveData(MainActivity.this);
                                }
                                //nemoj da produžiš sa drowerom
                            }
                        })
                        .setNegativeButton(getString(R.string.ne), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // poništi izmene
                                updater.updateData();
                                // produži sa drawerom!!!
                                selectMenuItem(position);
                            }
                        })
                        .show();
                return;
            }
        }
        selectMenuItem(position);
    }

    private void selectMenuItem(int position) {
        // level 0
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new FakturaFragment();
                break;
            case 1:
                fragment = new KomitentFragment();
                break;
            case 2:
                fragment = new OpisFragment();
                break;
            case 3:
                fragment = new Memorandum();
                break;
            case 4:
                fragment = new Auto();
                break;
            default:
                fragment = new PdfPregled();
                break;
        }
        currentFragment = fragment;
        currentTag = tags[position];
        level = 0;
        title = menuItems[position];
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, currentFragment, currentTag)
                .commit();
        drawerLayout.closeDrawer(recyclerView);
    }

    private void selectDetail(Object object) {
        // level 1
        Fragment fragment = null;
        Bundle args;
        if (tags[0].equals(currentTag)) {
            Faktura faktura = (Faktura) object;
            args = new Bundle();
            args.putSerializable("faktura", faktura);
            fragment = new FakturaFragmentDetail();
            fragment.setArguments(args);
        } else if (tags[1].equals(currentTag)) {
            Komitent komitent = (Komitent) object;
            args = new Bundle();
            args.putSerializable("komitent", komitent);
            fragment = new KomitentFragmentDetail();
            fragment.setArguments(args);
        } else if (tags[2].equals(currentTag)) {
            Opis opis = (Opis) object;
            args = new Bundle();
            args.putSerializable("opis", opis);
            fragment = new OpisFragmentDetail();
            fragment.setArguments(args);
        } else if (tags[3].equals(currentTag)) {
            // nemoguće
        } else if (tags[4].equals(currentTag)) {

        } else if (tags[5].equals(currentTag)) {

        }
        if (fragment != null) {
            currentFragment = fragment;
            level = 1;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, currentFragment, currentTag)
                    .addToBackStack(null)
                    .commit();
        }

    }

}
