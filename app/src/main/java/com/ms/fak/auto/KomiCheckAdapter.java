package com.ms.fak.auto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ms.fak.R;

import java.util.List;

public class KomiCheckAdapter extends ArrayAdapter<KomiCheck> {
    private final LayoutInflater inflater;

    public KomiCheckAdapter(Context context, List<KomiCheck> items) {
        super(context, R.layout.chacked_list, R.id.text1, items);
        inflater = LayoutInflater.from(context);
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chacked_list, null);
            holder = new Holder();
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(R.id.text2);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    KomiCheck kc = (KomiCheck) cb.getTag();
                    kc.checked = cb.isChecked();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        KomiCheck komiCheck = this.getItem(position);

        holder.text1.setText(komiCheck.komitent.getNaziv());
        holder.text2.setText(komiCheck.komitent.getMestoAdresa());
        holder.checkBox.setChecked(komiCheck.checked);

        holder.checkBox.setTag(komiCheck);

        return convertView;
    }

    private class Holder {
        CheckBox checkBox;
        TextView text1;
        TextView text2;
    }
}
