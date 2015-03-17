package com.ms.fak.komitent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Komitent;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

import java.util.List;

public class KomitentFragment extends ListFragment implements Updater {
    private FragmentsCallbacks mCallbacks;
    private KomitentAdapter adapter;

    public KomitentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Komitent> list = Api.getInstance().getListKomitent();
        adapter = new KomitentAdapter(getActivity(), list);
        setListAdapter(adapter);
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
                showDetail(new Komitent());
                return true;
            case R.id.action_refresh:
                new AppRefreshTask(getActivity(), this).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // action_edit
        Komitent komitent = (Komitent) l.getItemAtPosition(position);
        showDetail(komitent);
    }

    private void showDetail(Komitent komitent) {
        if (null != mCallbacks) {
            mCallbacks.onCallForDetail(komitent);
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
