package com.ms.fak.memorandum;

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
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.AppUtl;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

public class Memorandum extends Fragment implements Updater {
    private FragmentsCallbacks mCallbacks;

    private FragmentTabHost tabHost;
    private Memo1 memo1;
    private Memo2 memo2;
    private Memo3 memo3;

    public Memorandum() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memorandum, container, false);

        tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        FragmentTabHost.TabSpec tab;
        View tabIndicator;
        TextView tabTitle;

        tabIndicator = inflater.inflate(R.layout.tab_indicator_holo, tabHost.getTabWidget(), false);
        tabTitle = (TextView) tabIndicator.findViewById(R.id.title);
        tabTitle.setText(getActivity().getString(R.string.o_firmi_a));
        tab = tabHost.newTabSpec("firma_a");
        tab.setIndicator(tabIndicator);
        tabHost.addTab(tab, Memo1.class, null);

        tabIndicator = inflater.inflate(R.layout.tab_indicator_holo, tabHost.getTabWidget(), false);
        tabTitle = (TextView) tabIndicator.findViewById(R.id.title);
        tabTitle.setText(getActivity().getString(R.string.o_firmi_b));
        tab = tabHost.newTabSpec("firma_b");
        tab.setIndicator(tabIndicator);
        tabHost.addTab(tab, Memo2.class, null);

        tabIndicator = inflater.inflate(R.layout.tab_indicator_holo, tabHost.getTabWidget(), false);
        tabTitle = (TextView) tabIndicator.findViewById(R.id.title);
        tabTitle.setText(getActivity().getString(R.string.o_fakturi));
        tab = tabHost.newTabSpec("fakdata");
        tab.setIndicator(tabIndicator);
        tabHost.addTab(tab, Memo3.class, null);

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
        if (memo1 == null) memo1 = (Memo1) getChildFragmentManager().findFragmentByTag("firma_a");
        if (memo2 == null) memo2 = (Memo2) getChildFragmentManager().findFragmentByTag("firma_b");
        if (memo3 == null) memo3 = (Memo3) getChildFragmentManager().findFragmentByTag("fakdata");
    }

    @Override
    public boolean isValid(Context context) {
        getMemos();
        if (memo3 != null && !memo3.isValid(context)) {
            tabHost.setCurrentTabByTag("fakdata");
            return false;
        }
        return true;
    }

    @Override
    public boolean isModified() {
        getMemos();
        return (memo1 != null && memo1.isModified()) ||
                (memo2 != null && memo2.isModified()) ||
                (memo3 != null && memo3.isModified());
    }

    @Override
    public void updateData() {
        getMemos();
        // ako je null, znači da nije posećen
        if (memo1 != null) memo1.initComponents();
        if (memo2 != null) memo2.initComponents();
        if (memo3 != null) memo3.initComponents();
    }

    @Override
    public void saveData(Context context) {
        getMemos();
        if (memo1 != null) memo1.components2glob();
        if (memo2 != null) memo2.components2glob();
        if (memo3 != null) memo3.components2glob();
        new MemoTaskUpdate(context, this).execute();
    }

    @Override
    public void gasiDekor(Activity activity) {
        AppUtl.gasiKeyb(activity);
    }

}
