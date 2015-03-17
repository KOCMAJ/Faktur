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

public class Memo2 extends Fragment {

    private Global glob;

    private EditText pib;
    private EditText mbr;
    private EditText del;
    private EditText ziro;

    public Memo2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glob = Api.getInstance().getGlobal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo2, container, false);

        pib  = (EditText) view.findViewById(R.id.pib);
        mbr  = (EditText) view.findViewById(R.id.mbr);
        del  = (EditText) view.findViewById(R.id.del);
        ziro = (EditText) view.findViewById(R.id.ziro);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents();
    }

    void initComponents() {
        pib.setText(glob.getFirmaPib());
        mbr.setText(glob.getFirmaMbr());
        del.setText(glob.getFirmaDel());
        ziro.setText(glob.getFirmaZiro());
    }

    void components2glob () {
        glob.setFirmaPib(pib.getText().toString());
        glob.setFirmaMbr(mbr.getText().toString());
        glob.setFirmaDel(del.getText().toString());
        glob.setFirmaZiro(ziro.getText().toString());
    }

    boolean isModified() {
        if (!glob.getFirmaPib().equals(pib.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaMbr().equals(mbr.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaDel().equals(del.getText().toString())) {
            return true;
        }
        if (!glob.getFirmaZiro().equals(ziro.getText().toString())) {
            return true;
        }
        return false;
    }

}
