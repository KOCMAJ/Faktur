package com.ms.fak.komitent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ms.fak.R;
import com.ms.fak.entities.Komitent;
import com.ms.fak.utl.HolderText2;

import java.util.List;

public class KomitentAdapter extends ArrayAdapter<Komitent> {
    private final LayoutInflater inflater;
    private final List<Komitent> list;

    public KomitentAdapter(Context context, List<Komitent> items) {
        super(context, R.layout.list_text_2, items);
        list = items;
        inflater = LayoutInflater.from(context);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        Komitent komitent = this.getItem(position);
        holder.text1.setText(komitent.getNaziv());
        holder.text2.setText(komitent.getMestoAdresa());
        return convertView;
    }

}
