package id.web.skytacco.sysuka.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.base.fragment.AboutFragment;
import id.web.skytacco.sysuka.base.fragment.tabLayout.HomeTabFragment;
import id.web.skytacco.sysuka.base.fragment.tabLayout.KesukaanTabFragment;
import id.web.skytacco.sysuka.util.Utils;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int judulHalaman;
    private final static String COLLAPSE_TAG = "collapsing_toolbar";
    private final static String SELECTED_TAG = "selected_index";
    private final static int COLLAPSING_TOOLBAR = 0;
    private final static int COLLAPSING_TOOLBAR2 = 1;
    private final static int CATEGORY = 2;
    private final static int FAVORITE = 3;
    private final static int KULINER_WORLD = 4;
    private final static int RATING = 5;
    private final static int MORE = 6;
    private final static int SHARE = 7;
    private final static int ABOUT = 8;

    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mdrawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, mdrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mdrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState != null) {
            navigationView.getMenu().getItem(savedInstanceState.getInt(SELECTED_TAG)).setChecked(true);
            return;
        }

        judulHalaman = COLLAPSING_TOOLBAR;

        getSupportFragmentManager().beginTransaction().add(R.id.content_main,
                new HomeTabFragment(), COLLAPSE_TAG).commit();

    }

 /*   @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TAG, selectedIndex);
    }
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (Utils.ENABLE_EXIT_DIALOG) {

                AlertDialog.Builder mdialog = new AlertDialog.Builder(NavigationActivity.this);
                mdialog.setIcon(R.mipmap.ic_launcher);
                mdialog.setTitle(R.string.app_name);
                mdialog.setMessage(R.string.pesan_keluar);
                mdialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigationActivity.this.finish();
                    }
                });

                /*mdialog.setNegativeButton(R.string.beri_love, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String namaAplikasi = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + namaAplikasi)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + namaAplikasi)));
                        }

                        NavigationActivity.this.finish();
                    }
                });
*/
                mdialog.setNeutralButton(R.string.app_more, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.playstore_app_more))));

                        NavigationActivity.this.finish();
                    }
                });
                mdialog.show();

            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_resep) {
            judulHalaman = COLLAPSING_TOOLBAR;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new HomeTabFragment(), COLLAPSE_TAG).commit();
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_resep2) {
            judulHalaman = COLLAPSING_TOOLBAR2;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new HomeTabFragment(), COLLAPSE_TAG).commit();
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_favorite) {
            judulHalaman = FAVORITE;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new KesukaanTabFragment(), COLLAPSE_TAG).commit();
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_category) {
            judulHalaman = CATEGORY;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new HomeTabFragment(), COLLAPSE_TAG).commit();
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_duren) {
            judulHalaman = KULINER_WORLD;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new HomeTabFragment(), COLLAPSE_TAG).commit();
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_rating) {
            judulHalaman = RATING;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_more) {
            judulHalaman = MORE;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Handoyo")));
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_share) {
            judulHalaman = SHARE;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.teksShare) + "\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
            i.setType("text/plain");
            startActivity(Intent.createChooser(i, "Share"));
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_about) {
            judulHalaman = ABOUT;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new AboutFragment(), "about").commit();
            mdrawer.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    public void setupNavigationDrawer(Toolbar toolbar) {
        toggle = new ActionBarDrawerToggle(this, mdrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mdrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
