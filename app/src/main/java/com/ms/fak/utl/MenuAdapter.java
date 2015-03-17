package com.ms.fak.utl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.fak.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private final String[] menuItems;
    private final FragmentsCallbacks fListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public ViewHolder(TextView view) {
            super(view);
            textView = view;
        }
    }

    public MenuAdapter(String[] menuItems, FragmentsCallbacks listener) {
        this.menuItems = menuItems;
        this.fListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu_item, parent, false);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(menuItems[position]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fListener.onMenuItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.length;
    }
}
