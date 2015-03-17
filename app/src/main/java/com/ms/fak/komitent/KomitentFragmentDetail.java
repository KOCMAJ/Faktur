package com.ms.fak.komitent;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Komitent;
import com.ms.fak.entities.Opis;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.AppUtl;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public class KomitentFragmentDetail extends Fragment implements Updater {
    private FragmentsCallbacks mCallbacks;

    private Komitent komitent;

    private EditText naziv;
    private EditText mesto;
    private EditText adresa;
    private EditText pib;
    private EditText email;
    private AutoCompleteTextView usluga_opis;
    private CheckBox checkObim;
    private EditText usluga_iznos;
    private CheckBox checkEuro;

    public KomitentFragmentDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        komitent = (Komitent) getArguments().getSerializable("komitent");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.komitent_detail, container, false);

        naziv        = (EditText) view.findViewById(R.id.naziv);
        mesto        = (EditText) view.findViewById(R.id.mesto);
        adresa       = (EditText) view.findViewById(R.id.adresa);
        pib          = (EditText) view.findViewById(R.id.pib);
        email        = (EditText) view.findViewById(R.id.email);
        usluga_opis  = (AutoCompleteTextView) view.findViewById(R.id.usluga_opis);
        checkObim    = (CheckBox) view.findViewById(R.id.checkObim);
        usluga_iznos = (EditText) view.findViewById(R.id.usluga_iznos);
        checkEuro    = (CheckBox) view.findViewById(R.id.checkEuro);

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
        if (komitent.getId() == null) {
            // delete na new task - samo odustani i zatvori fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            if (fm.getBackStackEntryCount()>0) {
                fm.popBackStack();
            }
        } else {
            // stvarno brisanje
            String title = getActivity().getString(R.string.brisanje_komitenta);
            String message = komitent.getNaziv();
            message = message + "\n ima " + komitent.getFakturaCollection().size() +" fakutra";
            message = message + getActivity().getString(R.string.n_da_brisem);
            new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getActivity().getString(R.string.brisi), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            new KomitentTaskDelete(komitent, getActivity()).execute();
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
        if (naziv.getText().toString().trim().isEmpty()) {
            String message = context.getString(R.string.obavezan_naziv);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            naziv.requestFocus();
            return false;
        }
        try {
            BigDecimal iznos = (BigDecimal) AppUtl.fmtIznos.parse(usluga_iznos.getText().toString());
        } catch (ParseException ex) {
            String message = context.getString(R.string.rdjav_iznos);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            usluga_iznos.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean isModified() {
        BigDecimal iznos = null;
        try {
            iznos = (BigDecimal) AppUtl.fmtIznos.parse(usluga_iznos.getText().toString());
        } catch (ParseException ex) {
            return true;
        }
        if (!komitent.getNaziv().equals(naziv.getText().toString())) {
            return true;
        }
        if (!komitent.getMesto().equals(mesto.getText().toString())) {
            return true;
        }
        if (!komitent.getAdresa().equals(adresa.getText().toString())) {
            return true;
        }
        if (!komitent.getPib().equals(pib.getText().toString())) {
            return true;
        }
        if (!komitent.getEmail().equals(email.getText().toString())) {
            return true;
        }
        if (!komitent.getUslugaOpis().equals(usluga_opis.getText().toString())) {
            return true;
        }
        if (komitent.isUslugaObim() != checkObim.isChecked()) {
            return true;
        }
        if (iznos == null || komitent.getUslugaIznos().compareTo(iznos) != 0) {
            return true;
        }
        if (komitent.isUslugaEuro() != checkEuro.isChecked()) {
            return true;
        }
        return false;
    }

    private void setUsluga() {
        usluga_opis.setText(komitent.getUslugaOpis());
        checkObim.setChecked(komitent.isUslugaObim());
        usluga_iznos.setText(AppUtl.formatIznos(komitent.getUslugaIznos()));
        checkEuro.setChecked(komitent.isUslugaEuro());
    }

    @Override
    public void updateData() {
        naziv.setText(komitent.getNaziv());
        mesto.setText(komitent.getMesto());
        adresa.setText(komitent.getAdresa());
        pib.setText(komitent.getPib());
        email.setText(komitent.getEmail());
        setUsluga();
        List<Opis> opisi = Api.getInstance().getListOpisSort();
        usluga_opis.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Opis opis = (Opis) parent.getAdapter().getItem(position);
                        komitent.setUslugaOpis(opis.getOpis());
                        komitent.setUslugaObim(opis.isObim());
                        komitent.setUslugaIznos(opis.getIznos());
                        komitent.setUslugaEuro(opis.isEuro());
                        setUsluga();
                    }
                }
        );
        ArrayAdapter<Opis> adapter = new ArrayAdapter<Opis>(getActivity(), android.R.layout.simple_dropdown_item_1line, opisi);
        usluga_opis.setAdapter(adapter);
    }

    @Override
    public void saveData(Context context) {
        BigDecimal iznos = null;
        try {
            iznos = (BigDecimal) AppUtl.fmtIznos.parse(usluga_iznos.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        komitent.setNaziv(naziv.getText().toString());
        komitent.setMesto(mesto.getText().toString());
        komitent.setAdresa(adresa.getText().toString());
        komitent.setPib(pib.getText().toString());
        komitent.setEmail(email.getText().toString());
        komitent.setUslugaOpis(usluga_opis.getText().toString());
        komitent.setUslugaIznos(iznos);
        komitent.setUslugaObim(checkObim.isChecked());
        komitent.setUslugaEuro(checkEuro.isChecked());
        if (komitent.getId() == null) {
            new KomitentTaskCreate(komitent, getActivity(), this).execute();
        } else {
            new KomitentTaskUpdate(komitent, getActivity(), this).execute();
        }
    }

    @Override
    public void gasiDekor(Activity activity) {
        AppUtl.gasiKeyb(activity);
    }

}

