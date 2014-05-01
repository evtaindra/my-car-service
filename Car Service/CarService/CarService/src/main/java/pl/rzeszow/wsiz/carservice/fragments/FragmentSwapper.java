package pl.rzeszow.wsiz.carservice.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import pl.rzeszow.wsiz.carservice.R;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class FragmentSwapper {

    private Context mContext;
    private FragmentManager mFragmentManager;

    public FragmentSwapper(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
    }

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

    private String getFragmentTag(int pos){
        return "android:switcher:"+ R.id.pager+":"+pos;
    }

}
