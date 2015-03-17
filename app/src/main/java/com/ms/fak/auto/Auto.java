package com.ms.fak.auto;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.ms.fak.R;
import com.ms.fak.entities.Komitent;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.AppUtl;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

import java.util.List;

public class Auto extends Fragment implements Updater {
    private FragmentsCallbacks mCallbacks;

    private FragmentTabHost tabHost;
    private Auto1 auto1;
    private Auto2 auto2;

    public Auto() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auto, container, false);

        tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        FragmentTabHost.TabSpec tab;
        View tabIndicator;
        TextView tabTitle;

        tabIndicator = inflater.inflate(R.layout.tab_indicator_holo, tabHost.getTabWidget(), false);
        tabTitle = (TextView) tabIndicator.findViewById(R.id.title);
        tabTitle.setText(getActivity().getString(R.string.kome));
        tab = tabHost.newTabSpec("auto1");
        tab.setIndicator(tabIndicator);
        tabHost.addTab(tab, Auto1.class, null);

        tabIndicator = inflater.inflate(R.layout.tab_indicator_holo, tabHost.getTabWidget(), false);
        tabTitle = (TextView) tabIndicator.findViewById(R.id.title);
        tabTitle.setText(getActivity().getString(R.string.kako));
        tab = tabHost.newTabSpec("auto2");
        tab.setIndicator(tabIndicator);
        tabHost.addTab(tab, Auto2.class, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                gasiDekor(getActivity());
            }
        });

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_UP) {
                        //String currentTabTag = (String) tabHost.getCurrentTabTag();
                        //String clickedTabTag = (String) v.getTag();
                        if (!isValid(getActivity())) {
                            return true;    // doesnt allow tab change
                        }
                    }
                    return false;           // allows tab change
                }
            });
        }

        return tabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tabHost = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentsCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getString(R.string.implement_callbacks));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mCallbacks.isDrawerOpen()) {
            inflater.inflate(R.menu.save_frash, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                gasiDekor(getActivity());
                if (isValid(getActivity())) {
                    saveData(getActivity());
                }
                return true;
            case R.id.action_refresh:
                gasiDekor(getActivity());
                new AppRefreshTask(getActivity(), this).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMemos() {
        // ulovi prvo pojavljivanje, ako je null, nije ni korišćen taj tab
        if (auto1 == null) auto1 = (Auto1) getChildFragmentManager().findFragmentByTag("auto1");
        if (auto2 == null) auto2 = (Auto2) getChildFragmentManager().findFragmentByTag("auto2");
    }

    @Override
    public boolean isValid(Context context) {
        getMemos();
        if (auto1 != null && !auto1.isValid(context)) {
            tabHost.setCurrentTabByTag("auto1");
            return false;
        }
        if (auto2 != null && !auto2.isValid(context)) {
            tabHost.setCurrentTabByTag("auto2");
            return false;
        }
        return true;
    }

    @Override
    public boolean isModified() {
        getMemos();
        return auto2 != null && auto2.isModified();
    }

    @Override
    public void updateData() {
        getMemos();
        // ako je null, znači da nije posećen
        if (auto1 != null) auto1.initComponents();
        if (auto2 != null) auto2.initComponents();
    }

    @Override
    public void saveData(Context context) {
        getMemos();
        if (auto2 != null) auto2.components2glob();

        List<Komitent> komitenti = auto1.getSelected();

        new AutoTaskGenerate(context, this, komitenti).execute();
    }

    @Override
    public void gasiDekor(Activity activity) {
        AppUtl.gasiKeyb(activity);
    }

}
