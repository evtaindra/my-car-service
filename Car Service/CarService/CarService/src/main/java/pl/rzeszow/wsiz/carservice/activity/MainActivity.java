package pl.rzeszow.wsiz.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Locale;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.fragments.FragmentSwapper;
import pl.rzeszow.wsiz.carservice.utils.Singleton;

/**
 * Klasa mainActivity
 * <p>
 * Klasa do usługi menu głównego
 * </p>
 */
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private SectionsPagerAdapter mSectionsPagerAdapter; //!< reprezentuje  każdą stronę jako sekcję do której użytkownik może powrócić

    private ViewPager mViewPager;//!< pozwala na przerzucanie w lewo i prawo przez strony danych.

    private int userID;//!< id użytkownika

    /**
     * Ustawienie treści do widoku i tworzenie nowego adaptera i listenera do niego
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            /**
             *  Metoda zostanie wywołana, gdy nowa strona zostanie wybrana.
             *  Za pomocą Singletona otrzymujemy id użytkownika
             * @param position indeks położenie nowej wybranej strony.
             */
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        userID = getIntent().getExtras().getInt(Constants.USER_ID);
        Singleton.getSingletonInstance().userID = userID;
    }

    /**
     * Wywoływane, gdy zakładka wchodzi w wybrany stan.
     * @param tab Zakładka, która została wybrana
     * @param fragmentTransaction kolejkowanie operacji podczas przełączania zakładek.
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Gdy zakładka wychodzi z wybranego stanu.
     * @param tab Zakładka, która nie została wybrana
     * @param fragmentTransaction kolejkowanie operacji podczas przełączania zakładek.
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * Gdy zakładka, która była już wybrana zostanie wybrana ponownie
     * @param tab zakładka wybrana ponownie
     * @param fragmentTransaction kolejkowanie operacji podczas przełączania zakładek.
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * Klasa do obsługi przechodzenia pomiędzy stronami
     *
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        /**
         * Konstruktor
         * @param fm  interakcja z obiektami wewnątrz activity
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Tworzenie nowego przechodzenia pomiędzy fragmentami
         */
        private FragmentSwapper fragmentSwapper
                = new FragmentSwapper(MainActivity.this, getSupportFragmentManager());

        /**
         * Pobieranie fragmentu związanego z określoną pozycją.
         * @param position pozycja fragmentu
         * @return fragment związany z określoną pozycję.
         */
        @Override
        public Fragment getItem(int position) {
            return fragmentSwapper.getFragment(position);
        }

        /**
         * Zwraca liczbę dosępnych widoków.
         * @return 3 dostępne widoki
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        /**
         *  Może być wywołana przez ViewPager dla opisania określonej strony.
         * @param position pozycja tytułu
         * @return null - brak tytułu dla tej strony.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * Inicjalizacja zawartości menu.
     * <p>
     * Tworzymy wystąpienia XML plików w menu objektach i
     * hierarchię menu z określonego XML zasobu.
     * </p>
     * @param menu menu, w którym można umieścić Opcje.
     * @return true dla wyświetlenia menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }
    /**
     *  Jest wywoływana, kiedy został wybrany element z menu
     * <p>
     *     Pobieramy id tego elementa, jeżeli to jest wylogowanie
     *     za pomocą Singletone usrawiamy id użytkownika na 0 i przechodzimy
     *     na stronę logowania
     * </p>
     * @param item element menu, który został wybrany.
     * @return false aby umożliwić normalne menu dla kontynuacji przetwarzania,
     * true aby je konsumować.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE).edit().clear().commit();
            finish();
            Singleton.getSingletonInstance().userID = 0;
            startActivity(new Intent(MainActivity.this, Login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}