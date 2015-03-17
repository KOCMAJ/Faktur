package com.ms.fak.opis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ms.fak.R;
import com.ms.fak.entities.Opis;

import java.util.List;

public class OpisAdapter  extends ArrayAdapter<Opis> {

    private final List<Opis> list;
    private final LayoutInflater inflater;

    public OpisAdapter(Context context, List<Opis> list) {
        super(context, R.layout.list_text_1, list);
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpisHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_text_1, null);
            holder = new OpisHolder();
            holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (OpisHolder) convertView.getTag();
        }
        holder.text1.setText(getItem(position).getOpis());
        return convertView;
    }

    public static class OpisHolder {
        public TextView text1;
    }
}
