package com.tuliohdev.movieflix.ui;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.tuliohdev.movieflix.R;
import com.tuliohdev.movieflix.data.model.MovieGenre;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private static final String MOVIE_SEARCH_FRAGMENT_TAG = "MOVIE_SEARCH_FRAGMENT_TAG";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    private Unbinder mUnbinder;
    private MaterialMenuDrawable mMaterialMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        setupToolbarAndDrawerMenu();

        replaceContent(MoviesFragment.newInstance(), false);
    }

    private void setupToolbarAndDrawerMenu() {
        setSupportActionBar(mToolbar);

        mNavigationView.setNavigationItemSelectedListener(this);

        mMaterialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        mToolbar.setNavigationIcon(mMaterialMenu);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMaterialMenu.isRunning()) {
                    return;
                }

                if(mMaterialMenu.getIconState() == MaterialMenuDrawable.IconState.BURGER) {
                    toggleMenu();
                } else {
                    onBackPressed();
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount() > 0) { // show arrow
                    mMaterialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);
                } else { // show burger
                    mMaterialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        });
    }

    private void toggleMenu() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchViewMenu = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewMenu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean addToBackStack = getSupportFragmentManager()
                        .findFragmentByTag(MOVIE_SEARCH_FRAGMENT_TAG) == null;
                replaceContent(MoviesFragment.newInstance(query), addToBackStack, MOVIE_SEARCH_FRAGMENT_TAG);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchViewMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if(getSupportFragmentManager().findFragmentByTag(MOVIE_SEARCH_FRAGMENT_TAG) != null) {
                    onBackPressed();
                    getSupportFragmentManager().popBackStack();
                }
                return true;
            }
        });

        return true;
    }

    public void replaceContent(Fragment fragment, boolean addToBackStack) {
        replaceContent(fragment, addToBackStack, null);
    }

    public void replaceContent(Fragment fragment, boolean addToBackStack, String fragmentTag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();


        if(fragmentTag == null) {
            fragmentTransaction.replace(R.id.main_content, fragment);
        } else {
            fragmentTransaction.replace(R.id.main_content, fragment, fragmentTag);
        }

        if(addToBackStack) {
            fragmentTransaction.addToBackStack("");
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout == null) {
            return;
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        MovieGenre movieGenre;

        switch(item.getItemId()) {
            case R.id.actionMovie:
                movieGenre = MovieGenre.ACTION;
                break;
            case R.id.adventureMovie:
                movieGenre = MovieGenre.ADVENTURE;
                break;
            case R.id.animationMovie:
                movieGenre = MovieGenre.ANIMATION;
                break;
            case R.id.comedyMovie:
                movieGenre = MovieGenre.COMEDY;
                break;
            case R.id.crimeMovie:
                movieGenre = MovieGenre.CRIME;
                break;
            case R.id.dramaMovie:
                movieGenre = MovieGenre.DRAMA;
                break;
            case R.id.horrorMovie:
                movieGenre = MovieGenre.HORROR;
                break;
            case R.id.scifiMovie:
                movieGenre = MovieGenre.SCIFI;
                break;
            default:
                replaceContent(MoviesFragment.newInstance(), false);
                return true;
        }

        replaceContent(MoviesFragment.newInstance(movieGenre), false);

        return true;
    }
}
