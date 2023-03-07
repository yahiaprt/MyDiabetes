package com.mydiabetesprt.diabetes.future.navigation;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mydiabetesprt.diabetes.NotificationServiceprt;
import com.mydiabetesprt.diabetes.NotificationServiceprt2;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.future.timeline.tempslineFragment;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.future.dashboard.DashboarddeFragment;
import com.mydiabetesprt.diabetes.future.Entrer.search.EntrerSearchActivity;
import com.mydiabetesprt.diabetes.future.makla.search.maklaSearchActivity;
import com.mydiabetesprt.diabetes.future.log.LogFragment;
import com.mydiabetesprt.diabetes.future.preference.PreferenceActivity;
import com.mydiabetesprt.diabetes.partage.view.activity.BaseActivity;
import com.mydiabetesprt.diabetes.partage.view.coordinatorlayout.SlideOutBehavior;
import com.mydiabetesprt.diabetes.partage.view.fragment.BaseFragment;
import com.mydiabetesprt.diabetes.future.calculatrice.CalculatriceFragment;
import com.mydiabetesprt.diabetes.future.export.ExportFragment;
import com.mydiabetesprt.diabetes.future.statistique.statistiqueFragment;
import com.mydiabetesprt.diabetes.partage.SystemUs;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;

import static com.mydiabetesprt.diabetes.healthysiabiresApplication.getContext;


public class MainActivity extends BaseActivity implements OnFragmentChangeListener {
    private AdView mAdView;

    Boolean y = true;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer)
    NavigationView drawer;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ActionBarDrawerToggle drawerToggle;

    public MainActivity() {
        super(R.layout.activity_main);
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//         mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                finish();
//            }
//
//        });
//
//  }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        showIntewrestial();

        //}
//    @Override
//    public void onBackButtonPressed() {
//        if (y=true) {
//            showIntewrestial();
//        } else {
//            onBackPressed();
//        }

    }
//    public  void showIntewrestial3() {
//        if (mInterstitialAd3.isLoaded()) {
//            mInterstitialAd3.show();
//        }}
//    public  void showIntewrestial4() {
//        if (mInterstitialAd4.isLoaded()) {
//            mInterstitialAd4.show();
//        }}
//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initialize();
        showChangelog();


        startService(new Intent(this, NotificationServiceprt.class));
        startService(new Intent(this, NotificationServiceprt2.class));

//

        }

        @Override
        protected void onPostCreate (Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
            drawerToggle.syncState();
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.action_search:

                    EntrerSearchActivity.show(MainActivity.this, findViewById(R.id.action_search));


                    return true;
                default:
                    return super.onOptionsItemSelected(item);

            }
        }

        @Override
        public void onFragmentChanged (Fragment fragment){
            invalidateLayout();
        }

        private void initialize () {
            drawerToggle = new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    toolbar,
                    R.string.drawer_open,
                    R.string.drawer_close) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
//
//                    MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//                        @Override
//                        public void onInitializationComplete(InitializationStatus initializationStatus) {
//                        }
//                    });
//
//                    mInterstitialAd3 = new InterstitialAd(getContext());
//                    mInterstitialAd3.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//                    mInterstitialAd3.loadAd(new AdRequest.Builder().build());
//                    mInterstitialAd3.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            super.onAdClosed();
//                            finish();
//                        }
//
//                    });
//
//                    MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//                        @Override
//                        public void onInitializationComplete(InitializationStatus initializationStatus) {
//                        }
//                    });
//
//                    mInterstitialAd4 = new InterstitialAd(getContext());
//                    mInterstitialAd4.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//                    mInterstitialAd4.loadAd(new AdRequest.Builder().build());
//                    mInterstitialAd4.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            super.onAdClosed();
//                            finish();
//                        }
//
//                    });

                    invalidateOptionsMenu();

                }





                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);




                    invalidateOptionsMenu();
                }
            };
            drawerLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
            drawer.getMenu().findItem(R.id.nav_calculator);
            drawer.setNavigationItemSelectedListener(menuItem -> {
                drawerLayout.closeDrawers();
                selectMenuItem(menuItem);
                return true;
            });
            drawerToggle.setToolbarNavigationClickListener(v -> {
                if (drawerToggle.isDrawerIndicatorEnabled()) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().popBackStackImmediate();
                }
            });

            getSupportFragmentManager().addOnBackStackChangedListener(() -> {
                drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
                invalidateLayout();
            });

            // Setup start fragment
            int startScreen = PreferenceHelper.getInstance().getStartScreen();
            MenuItem menuItem = drawer.getMenu().getItem(startScreen);
            selectMenuItem(menuItem);
        }

        private void invalidateLayout () {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment != null) {
                invalidateToolbar(fragment instanceof ToolbarBehavior ? (ToolbarBehavior) fragment : null);
                invalidateMainButton(fragment instanceof MainButton ? (MainButton) fragment : null);
                invalidateNavigationDrawer(fragment);
            }
        }

        private void invalidateToolbar (@Nullable ToolbarBehavior toolbarBehavior){
            if (toolbarBehavior != null) {
                setTitle(toolbarBehavior.getTitle());
            }
        }

        private void invalidateMainButton (@Nullable MainButton mainButton){
            MainButtonProperties properties = mainButton != null ? mainButton.getMainButtonProperties() : null;
            fab.setVisibility(properties != null ? View.VISIBLE : View.GONE);
            fab.setImageResource(properties != null ? properties.getIconDrawableResId() : android.R.color.transparent);
            fab.setOnClickListener(properties != null ? properties.getOnClickListener() : null);
            if (properties != null) {
                CoordinatorLayout.Behavior behavior = ViewUs.getBehavior(fab);
                if (behavior instanceof SlideOutBehavior) {
                    ((SlideOutBehavior) behavior).setSlideOut(properties.slideOut());
                }
            }
        }

        private void resetMainButton () {
            if (fab.getTranslationY() != 0) {
                fab.animate().cancel();
                fab.animate().translationY(0).start();

            }
        }

        private void invalidateNavigationDrawer (Fragment fragment){
            MainFragmentType mainFragmentType = MainFragmentType.valueOf(fragment.getClass());
            if (mainFragmentType != null) {
                int position = mainFragmentType.position;
                if (position < drawer.getMenu().size()) {
                    MenuItem menuItem = drawer.getMenu().getItem(position);
                    selectNavigationDrawerMenuItem(menuItem);
                }
            }
        }

        public void showFragment ( @IdRes int itemId){
            MenuItem menuItem = drawer.getMenu().findItem(itemId);
            selectMenuItem(menuItem);
        }


        private void selectMenuItem (MenuItem menuItem){
            if (menuItem != null) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_timeline:
                        showFragment(new tempslineFragment(), menuItem, false);
                        break;
                    case R.id.nav_log:
                        showFragment(new LogFragment(), menuItem, false);
                        break;
                    case R.id.nav_calculator:
                        showFragment(new CalculatriceFragment(), menuItem, true);
                        break;
                    case R.id.nav_makla_database:
                        startActivity(new Intent(this, maklaSearchActivity.class));
                        break;
                    case R.id.nav_statistics:
                        showFragment(new statistiqueFragment(), menuItem, true);
                        break;
                    case R.id.nav_export:
                        showFragment(new ExportFragment(), menuItem, true);
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(this, PreferenceActivity.class));
                        break;
                    default:
                        showFragment(new DashboarddeFragment(), menuItem, false);
                        break;
                }
            }
        }

        public void showFragment (BaseFragment fragment, MenuItem menuItem,boolean addToBackStack){
            Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            boolean isActive = activeFragment != null && activeFragment.getClass() == fragment.getClass();
            if (!isActive) {
                ViewUs.hideKeyboard(this);
                resetMainButton();
                selectNavigationDrawerMenuItem(menuItem);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                String tag = fragment.getClass().getSimpleName();
                if (addToBackStack) {
                    transaction.add(R.id.container, fragment, tag);
                    transaction.addToBackStack(tag);
                } else {
                    transaction.replace(R.id.container, fragment, tag);
                }
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
            }
        }

        private void selectNavigationDrawerMenuItem (MenuItem menuItem){
            if (menuItem != null) {
                // First uncheck all, then check current Fragment
                for (int index = 0; index < drawer.getMenu().size(); index++) {
                    drawer.getMenu().getItem(index).setChecked(false);
                }
                menuItem.setChecked(true);
            }
        }

        private void showChangelog () {
            int oldVersionCode = PreferenceHelper.getInstance().getVersionCode();
            int currentVersionCode = SystemUs.getVersionCode(this);
            if (oldVersionCode > 0) {
                boolean isUpdate = oldVersionCode < currentVersionCode;
                if (isUpdate) {
                    PreferenceHelper.getInstance().setVersionCode(currentVersionCode);
                /* TODO: Re-enable for future update
                ChangelogFragment fragment = new ChangelogFragment();
                String tag = fragment.getClass().getSimpleName();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(tag);
                fragment.show(fragmentTransaction, tag);
                */
                }
            } else {
                // Skip changelog for fresh installs
                PreferenceHelper.getInstance().setVersionCode(currentVersionCode);
            }
        }

        @Override
        public void onBackPressed () {



            if (drawerLayout.isDrawerOpen(GravityCompat.START))
            {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
               super.onBackPressed();
              //  showIntewrestial();
            }

//

        }
    }
