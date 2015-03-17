package com.ms.fak.auto;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Global;
import com.ms.fak.utl.AppUtl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

public class Auto2  extends Fragment {

    private Global glob;

    private EditText mesto;
    private EditText datum;
    private EditText promet;
    private EditText obim;
    private EditText kurs;
    private EditText nacin_placanja;
    private EditText napomena;

    public Auto2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        glob = Api.getInstance().getGlobal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auto2, container, false);

        mesto          = (EditText) view.findViewById(R.id.mesto);
        datum          = (EditText) view.findViewById(R.id.datum);
        promet         = (EditText) view.findViewById(R.id.promet);
        obim           = (EditText) view.findViewById(R.id.obim);
        kurs           = (EditText) view.findViewById(R.id.kurs);
        nacin_placanja = (EditText) view.findViewById(R.id.nacin_placanja);
        napomena       = (EditText) view.findViewById(R.id.napomena);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents();
    }

    void initComponents() {
        mesto.setText(glob.getFakturaMesto());
        datum.setText(AppUtl.sdf.format(glob.getFakturaDatum()));
        promet.setText(AppUtl.sdf.format(glob.getDatumPrometa()));
        napomena.setText(glob.getNapomenaPdv());
        nacin_placanja.setText(glob.getNacinPlacanja());
        kurs.setText(AppUtl.formatIznos(glob.getKursEuro()));
        obim.setText(glob.getUslugaObim());
    }


    void components2glob () {
        Date datum1 = null;
        Date promet1 = null;
        BigDecimal kurs1 = null;
        try {
            datum1 = AppUtl.sdf.parse(datum.getText().toString());
            promet1 = AppUtl.sdf.parse(promet.getText().toString());
            kurs1 = (BigDecimal) AppUtl.fmtIznos.parse(kurs.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        glob.setFakturaMesto(mesto.getText().toString());
        glob.setFakturaDatum(datum1);
        glob.setDatumPrometa(promet1);
        glob.setNapomenaPdv(napomena.getText().toString());
        glob.setNacinPlacanja(nacin_placanja.getText().toString());
        glob.setKursEuro(kurs1);
        glob.setUslugaObim(obim.getText().toString());
    }

    boolean isModified() {
        Date datum1 = null;
        Date promet1 = null;
        BigDecimal kurs1 = null;
        try {
            datum1 = AppUtl.sdf.parse(datum.getText().toString());
            promet1 = AppUtl.sdf.parse(promet.getText().toString());
            kurs1 = (BigDecimal) AppUtl.fmtIznos.parse(kurs.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!glob.getFakturaMesto().equals(mesto.getText().toString())) {
            return true;
        }
        if (!glob.getFakturaDatum().equals(datum1)) {
            return true;
        }
        if (!glob.getDatumPrometa().equals(promet1)) {
            return true;
        }
        if (!glob.getNapomenaPdv().equals(napomena.getText().toString())) {
            return true;
        }
        if (!glob.getNacinPlacanja().equals(nacin_placanja.getText().toString())) {
            return true;
        }
        if (kurs1 == null || glob.getKursEuro().compareTo(kurs1) != 0) {
            return true;
        }
        if (!glob.getUslugaObim().equals(obim.getText().toString())) {
            return true;
        }
        return false;
    }

    boolean isValid(Context context) {
        try {
            Date datum1 = AppUtl.sdf.parse(datum.getText().toString());
        } catch (ParseException e) {
            String message = context.getString(R.string.rdjav_datum);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            datum.requestFocus();
            return false;
        }
        try {
            Date promet1 = AppUtl.sdf.parse(promet.getText().toString());
        } catch (ParseException e) {
            String message = context.getString(R.string.rdjav_datum);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            promet.requestFocus();
            return false;
        }
        try {
            BigDecimal kurs1 = (BigDecimal) AppUtl.fmtIznos.parse(kurs.getText().toString());
        } catch (ParseException ex) {
            String message = context.getString(R.string.rdjav_iznos);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            kurs.requestFocus();
            return false;
        }
        return true;
    }

}
