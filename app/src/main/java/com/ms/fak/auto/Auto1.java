package com.ms.fak.auto;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Komitent;

import java.util.ArrayList;
import java.util.List;

public class Auto1 extends ListFragment {

    private final List<KomiCheck> komiChecks = new ArrayList<>();;

    private ArrayAdapter<KomiCheck> adapter;

    private CheckBox checker;

    public Auto1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auto1, container, false);

        checker = (CheckBox) view.findViewById(R.id.checker);
        checker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelection();
            }
        });

        adapter = new KomiCheckAdapter(getActivity(), komiChecks);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    void init() {
        if (adapter != null) adapter.notifyDataSetChanged();
        if (checker != null) {
            checker.setChecked(getSelected().size() == komiChecks.size());
        }
    }

    void initComponents() {
        List<Komitent> komitenti = Api.getInstance().getListKomitent();
        komiChecks.clear();
        for (Komitent komitent : komitenti) {
            KomiCheck kc = new KomiCheck(komitent);
            kc.checked = true;
            komiChecks.add(kc);
        }
        init();
    }

    public boolean isValid(Context context) {
        if (getSelected().size() > 0) {
            return true;
        }
        String message = "Kome, bre!";
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean isModified() {
        return false;
    }

    public List<Komitent> getSelected() {
        ArrayList<Komitent> result = new ArrayList<>();
        for (KomiCheck kc : komiChecks) {
            if (kc.checked) {
                result.add(kc.komitent);
            }
        }
        return result;
    }

    private void makeSelection() {
        boolean checked = checker.isChecked();
        for (KomiCheck kc : komiChecks) {
            kc.checked = checked;
        }
        adapter.notifyDataSetChanged();
    }
}
