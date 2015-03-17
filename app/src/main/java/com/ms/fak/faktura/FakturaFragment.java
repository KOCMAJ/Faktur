package com.ms.fak.faktura;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ms.fak.MainActivity;
import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Faktura;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

import java.util.List;

public class FakturaFragment extends ListFragment implements Updater {
    private FragmentsCallbacks mCallbacks;
    private FakturaAdapter adapter;

    public FakturaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Faktura> list = Api.getInstance().getListFaktura();
        adapter = new FakturaAdapter(getActivity(), list);
        setListAdapter(adapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity.isFirstTime()) {
            mainActivity.setFirstTime(false);
            new AppRefreshTask(getActivity(), this).execute();
        }

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
            inflater.inflate(R.menu.new_frash, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                showDetail(new Faktura());
                return true;
            case R.id.action_refresh:
                new AppRefreshTask(getActivity(), this).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // action_edit
        Faktura faktura = (Faktura) l.getItemAtPosition(position);
        showDetail(faktura);
    }

    private void showDetail(Faktura faktura) {
        if (null != mCallbacks) {
            mCallbacks.onCallForDetail(faktura);
        }
    }

    @Override
    public boolean isValid(Context context) {
        return true;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void updateData() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveData(Context context) {
    }

    @Override
    public void gasiDekor(Activity activity) {
    }

}
