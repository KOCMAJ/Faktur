package com.ms.fak.memorandum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Global;

public class Memo1 extends Fragment {

    private Global glob;

    private EditText naziv1;
    private EditText naziv2;
    private EditText mesto;
    private EditText adresa;
    private EditText telefon;
    private EditText fax;

    public Memo1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glob = Api.getInstance().getGlobal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo1, container, false);

        naziv1  = (EditText) view.findViewById(R.id.naziv1);
        naziv2  = (EditText) view.findViewById(R.id.naziv2);
        mesto   = (EditText) view.findViewById(R.id.mesto);
        adresa  = (EditText) view.findViewById(R.id.adresa);
        telefon = (EditText) view.findViewById(R.id.telefon);
        fax     = (EditText) view.findViewById(R.id.fax);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents();
    }

    void initComponents() {
        naziv1.setText(glob.getFirmaNaziv1());
        naziv2.setText(glob.getFirmaNaziv2());
        mesto.setText(glob.getFirmaMesto());
        adresa.setText(glob.getFirmaAdresa());
        telefon.setText(glob.getFirmaTel());
        fax.setText(glob.getFirmaFax());
    }

    void components2glob () {
        glob.setFirmaNaziv1(naziv1.getText().toString());
        glob.setFirmaNaziv2(naziv2.getText().toString());
        glob.setFirmaMesto(mesto.getText().toString());
        glob.setFirmaAdresa(adresa.getText().toString());
        glob.setFirmaTel(telefon.getText().toString());
        glob.setFirmaFax(fax.getText().toString());
    }

    boolean isModified() {
        if (!glob.getFirmaNaziv1().equals(naziv1.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaNaziv2().equals(naziv2.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaMesto().equals(mesto.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaAdresa().equals(adresa.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaTel().equals(telefon.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaFax().equals(fax.getText().toString())) {
            return true;
        }
        return false;
    }

}
