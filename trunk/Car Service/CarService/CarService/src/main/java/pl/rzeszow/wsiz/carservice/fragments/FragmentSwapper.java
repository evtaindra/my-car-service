package pl.rzeszow.wsiz.carservice.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import pl.rzeszow.wsiz.carservice.R;

/**
 * służy do odświeżania zawartości widoku poprzez pionowe machnięcie
 */
public class FragmentSwapper {

    private Context mContext;//!< służy do przytrzymania activity
    private FragmentManager mFragmentManager;//!<do interakcji z obiektami fragmentu wewnątrz activity

    /**
     * Konstruktor
     * @param context służy do przytrzymania activity
     * @param fragmentManager służy do interakcji z obiektami fragmentu wewnątrz activity
     */
    public FragmentSwapper(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    /**
     * W zależności od numeru sekcji pokazujemy listę serwisów,
     * dane osobowe lub listę rozmów
     * @param sectionNumber numer sekcji
     * @return aktualny fragment
     */
    public Fragment getFragment(int sectionNumber) {
        Fragment fragment = null;
        switch (sectionNumber) {
            case 0:
                fragment= mFragmentManager.findFragmentByTag(getFragmentTag(0));
                if (fragment == null){
                    fragment = new ServiceListFragment();
                }
                break;
            case 1:
                fragment= mFragmentManager.findFragmentByTag(getFragmentTag(1));
                if (fragment == null){
                    fragment = new PersonalDataFragment();
                }
                break;
            case 2:
                fragment= mFragmentManager.findFragmentByTag(getFragmentTag(2));
                if (fragment == null) {
                    fragment = new ConversationListFragment();
                }
                break;
        }
        return fragment;
    }

    /**
     * Pobieranie fragmentu w zalężności od zakładki
     * @param pos pozycja fragmentu
     * @return pozycję strony
     */
    private String getFragmentTag(int pos){
        return "android:switcher:"+ R.id.pager+":"+pos;
    }

}
