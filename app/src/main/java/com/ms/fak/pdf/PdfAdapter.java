package com.ms.fak.pdf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ms.fak.R;

import java.io.File;
import java.util.List;

public class PdfAdapter  extends ArrayAdapter<File> {

    private final List<File> list;
    private final LayoutInflater inflater;

    public PdfAdapter(Context context, List<File> list) {
        super(context, R.layout.list_text_1, list);
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_text_1, null);
            holder = new ItemHolder();
            holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }
        holder.text1.setText(getItem(position).getName());
        return convertView;
    }

    public static class ItemHolder {
        public TextView text1;
    }
}
