package com.ms.fak.faktura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ms.fak.R;
import com.ms.fak.entities.Faktura;
import com.ms.fak.utl.AppUtl;
import com.ms.fak.utl.HolderText2;

import java.util.List;

public class FakturaAdapter extends ArrayAdapter<Faktura> {
    private final LayoutInflater inflater;
    private final List<Faktura> list;

    public FakturaAdapter(Context context, List<Faktura> items) {
        super(context, R.layout.list_text_2, items);
        list = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HolderText2 holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_text_2, null);
            holder = new HolderText2();
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (HolderText2) convertView.getTag();
        }
        Faktura faktura = getItem(position);
        holder.text1.setText(getContext().getString(R.string.racun_br) + faktura.getBroj());
        holder.text2.setText("od " + AppUtl.getDatum(faktura.getDatum()));
        return convertView;
    }

}

