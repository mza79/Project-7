package it.familiyparking.app.fragment;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.familiyparking.app.MainActivity;
import it.familiyparking.app.R;
import it.familiyparking.app.listener.NewTabListener;
import it.familiyparking.app.listener.OldTabListener;
import it.familiyparking.app.utility.Tools;


/**
 * Created by francesco on 15/01/15.
 */
public class TabFragment extends Fragment{

    private MainActivity activity;
    private String tab_map;
    private String tab_car;

    private android.support.v7.app.ActionBar actionBarNew;
    private ActionBar actionBarOld;

    private ActionBar.Tab tabMapOld;
    private ActionBar.Tab tabCarOld;

    private android.support.v7.app.ActionBar.Tab tabMapNew;
    private android.support.v7.app.ActionBar.Tab tabCarNew;

    public TabFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        Tools.setUpButtonActionBar((MainActivity) getActivity());

        activity = (MainActivity)getActivity();
        tab_car = activity.getResources().getString(R.string.tab_car);
        tab_map = activity.getResources().getString(R.string.tab_map);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            oldActionBar();
        else
            newActionBar();

        return rootView;
    }

    private void oldActionBar(){
        actionBarOld = activity.getActionBar();
        actionBarOld.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabMapOld = actionBarOld.newTab().setText(tab_map);
        tabCarOld = actionBarOld.newTab().setText(tab_car);

        tabMapOld.setTabListener(new OldTabListener(activity, tab_map));
        tabCarOld.setTabListener(new OldTabListener(activity, tab_car));

        actionBarOld.addTab(tabMapOld);
        actionBarOld.addTab(tabCarOld);
    }

    private void newActionBar(){
        actionBarNew = activity.getSupportActionBar();
        actionBarNew.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabMapNew = actionBarNew.newTab().setText(tab_map);
        tabCarNew = actionBarNew.newTab().setText(tab_car);

        tabMapNew.setTabListener(new NewTabListener(activity, tab_map));
        tabCarNew.setTabListener(new NewTabListener(activity, tab_car));

        actionBarNew.addTab(tabMapNew);
        actionBarNew.addTab(tabCarNew);
    }

    public void selectCarFragment(){
        if(tabCarNew != null)
            tabCarNew.select();
        else if(tabCarOld != null)
            tabCarOld.select();
    }

    public void selectMapFragment(){
        if(tabMapNew != null)
            tabMapNew.select();
        else if(tabMapOld != null)
            tabMapOld.select();
    }

    public void removeTab(){
        if(actionBarNew != null) {
            actionBarNew.removeTab(tabCarNew);
            actionBarNew.removeTab(tabMapNew);
            actionBarNew.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        else if(actionBarOld != null) {
            actionBarOld.removeTab(tabCarOld);
            actionBarOld.removeTab(tabMapOld);
            actionBarOld.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
    }

}

