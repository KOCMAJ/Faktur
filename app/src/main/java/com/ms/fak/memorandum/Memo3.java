package com.ms.fak.memorandum;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class Memo3 extends Fragment {

    private Global glob;

    private EditText mesto_izdavanja;
    private EditText datum_izdavanja;
    private EditText datum_prometa;
    private EditText napomena;
    private EditText nacin_placanja;
    private EditText kurs_eura;
    private EditText obim;

    public Memo3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glob = Api.getInstance().getGlobal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo3, container, false);
        mesto_izdavanja  = (EditText) view.findViewById(R.id.mesto_izdavanja);
        datum_izdavanja  = (EditText) view.findViewById(R.id.datum_izdavanja);
        datum_prometa    = (EditText) view.findViewById(R.id.datum_prometa);
        napomena         = (EditText) view.findViewById(R.id.napomena);
        nacin_placanja   = (EditText) view.findViewById(R.id.nacin_placanja);
        kurs_eura        = (EditText) view.findViewById(R.id.kurs);
        obim             = (EditText) view.findViewById(R.id.obim);

return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents();
    }

    void initComponents() {
        mesto_izdavanja.setText(glob.getFakturaMesto());
        datum_izdavanja.setText(AppUtl.sdf.format(glob.getFakturaDatum()));
        datum_prometa.setText(AppUtl.sdf.format(glob.getDatumPrometa()));
        napomena.setText(glob.getNapomenaPdv());
        nacin_placanja.setText(glob.getNacinPlacanja());
        kurs_eura.setText(AppUtl.formatIznos(glob.getKursEuro()));
        obim.setText(glob.getUslugaObim());
    }

    void components2glob () {
        Date datum = null;
        Date promet = null;
        BigDecimal kurs = null;
        try {
            datum = AppUtl.sdf.parse(datum_izdavanja.getText().toString());
            promet = AppUtl.sdf.parse(datum_prometa.getText().toString());
            kurs = (BigDecimal) AppUtl.fmtIznos.parse(kurs_eura.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        glob.setFakturaMesto(mesto_izdavanja.getText().toString());
        glob.setFakturaDatum(datum);
        glob.setDatumPrometa(promet);
        glob.setNapomenaPdv(napomena.getText().toString());
        glob.setNacinPlacanja(nacin_placanja.getText().toString());
        glob.setKursEuro(kurs);
        glob.setUslugaObim(obim.getText().toString());
    }

    boolean isModified() {
        Date datum = null;
        Date promet = null;
        BigDecimal kurs = null;
        try {
            datum = AppUtl.sdf.parse(datum_izdavanja.getText().toString());
            promet = AppUtl.sdf.parse(datum_prometa.getText().toString());
            kurs = (BigDecimal) AppUtl.fmtIznos.parse(kurs_eura.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!glob.getFakturaMesto().equals(mesto_izdavanja.getText().toString())) {
            return true;
        }
        if (!glob.getFakturaDatum().equals(datum)) {
            return true;
        }
        if (!glob.getDatumPrometa().equals(promet)) {
            return true;
        }
        if (!glob.getNapomenaPdv().equals(napomena.getText().toString())) {
            return true;
        }
        if (!glob.getNacinPlacanja().equals(nacin_placanja.getText().toString())) {
            return true;
        }
        if (kurs == null || glob.getKursEuro().compareTo(kurs) != 0) {
            return true;
        }
        if (!glob.getUslugaObim().equals(obim.getText().toString())) {
            return true;
        }
        return false;
    }

    boolean isValid(Context context) {
        try {
            Date datum = AppUtl.sdf.parse(datum_izdavanja.getText().toString());
        } catch (ParseException e) {
            String message = context.getString(R.string.rdjav_datum);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            datum_izdavanja.requestFocus();
            return false;
        }
        try {
            Date promet = AppUtl.sdf.parse(datum_prometa.getText().toString());
        } catch (ParseException e) {
            String message = context.getString(R.string.rdjav_datum);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            datum_prometa.requestFocus();
            return false;
        }
        try {
            BigDecimal kurs = (BigDecimal) AppUtl.fmtIznos.parse(kurs_eura.getText().toString());
        } catch (ParseException ex) {
            String message = context.getString(R.string.rdjav_iznos);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            kurs_eura.requestFocus();
            return false;
        }
        return true;
    }
}
