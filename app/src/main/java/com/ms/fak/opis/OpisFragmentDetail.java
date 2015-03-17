package com.ms.fak.opis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Opis;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.AppUtl;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

import java.math.BigDecimal;
import java.text.ParseException;

public class OpisFragmentDetail extends Fragment implements Updater {
    private FragmentsCallbacks mCallbacks;

    private Opis opis;

    private EditText textOpis;
    private EditText textIznos;
    private CheckBox checkObim;
    private CheckBox checkEuro;
    private CheckBox checkAuto;

    public OpisFragmentDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        opis = (Opis) getArguments().getSerializable("opis");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.opis_detail, container, false);

        textOpis =  (EditText) view.findViewById(R.id.textOpis);
        textIznos = (EditText) view.findViewById(R.id.textIznos);
        checkObim = (CheckBox) view.findViewById(R.id.checkObim);
        checkEuro = (CheckBox) view.findViewById(R.id.checkEuro);
        checkAuto = (CheckBox) view.findViewById(R.id.checkAuto);

        updateData();

        return view;
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
            inflater.inflate(R.menu.save_delete_fresh, menu);
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
            case R.id.action_delete:
                gasiDekor(getActivity());
                delete();
                return true;
            case R.id.action_refresh:
                gasiDekor(getActivity());
            new AppRefreshTask(getActivity(), this).execute();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void delete() {
        if (opis.getId() == null) {
            // delete na new task - samo odustani i zatvori fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            if (fm.getBackStackEntryCount()>0) {
                fm.popBackStack();
            }
        } else {
            // stvarno brisanje
            String title = getActivity().getString(R.string.brisanje_usluge);
            String message = opis.getOpis();
            message = message + getActivity().getString(R.string.n_da_brisem);
            new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getActivity().getString(R.string.brisi), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            new OpisTaskDelete(opis, getActivity()).execute();
                        }
                    })
                    .setNegativeButton(getActivity().getString(R.string.pismani), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean isValid(Context context) {
        if (textOpis.getText().toString().trim().isEmpty()) {
            String message = context.getString(R.string.obavezan_opis);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            textOpis.requestFocus();
            return false;
        }
        try {
            BigDecimal iznos = (BigDecimal) AppUtl.fmtIznos.parse(textIznos.getText().toString());
        } catch (ParseException ex) {
            String message = context.getString(R.string.rdjav_iznos);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            textIznos.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean isModified() {
        BigDecimal iznos = null;
        try {
            iznos = (BigDecimal) AppUtl.fmtIznos.parse(textIznos.getText().toString());
        } catch (ParseException ex) {
            return true;
        }
        if (!opis.getOpis().equals(textOpis.getText().toString())) {
            return true;
        }
        if (iznos == null || opis.getIznos().compareTo(iznos) != 0) {
            return true;
        }
        if (opis.isAuto() != checkAuto.isChecked()) {
            return true;
        }
        if (opis.isEuro() != checkEuro.isChecked()) {
            return true;
        }
        if (opis.isObim() != checkObim.isChecked()) {
            return true;
        }
        return false;
    }

    @Override
    public void updateData() {
        textOpis.setText(opis.getOpis());
        textIznos.setText(AppUtl.formatIznos(opis.getIznos()));
        checkObim.setChecked(opis.isObim());
        checkEuro.setChecked(opis.isEuro());
        checkAuto.setChecked(opis.isAuto());
    }

    @Override
    public void saveData(Context context) {
        BigDecimal iznos = null;
        try {
            iznos = (BigDecimal) AppUtl.fmtIznos.parse(textIznos.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        opis.setOpis(textOpis.getText().toString());
        opis.setIznos(iznos);
        opis.setObim(checkObim.isChecked());
        opis.setEuro(checkEuro.isChecked());
        opis.setAuto(checkAuto.isChecked());
        if (opis.getId() == null) {
            new OpisTaskCreate(opis, context, this).execute();
        } else {
            new OpisTaskUpdate(opis, context, this).execute();
        }
    }

    @Override
    public void gasiDekor(Activity activity) {
        AppUtl.gasiKeyb(activity);
    }

}
