package com.ms.fak.faktura;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Opis;
import com.ms.fak.entities.Stavka;
import com.ms.fak.utl.AppUtl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TableModel {
    private final Activity activity;
    private final Integer fakturaId;
    private final List<Stavka> original;
    private final List<Stav> stavke;

    private BigDecimal suma;
    private TextView suma_view;

    private TableLayout table_stavke;

    private ClickDel clickDel;
    private ClickAdd clickAdd;
    private ClickUpd clickUpd;

    private TableRow.LayoutParams lpw10;
    private TableRow.LayoutParams lpw00;
    private TableRow.LayoutParams lpico;
    private TableRow.LayoutParams lptro;

    public TableModel(Activity activity, Integer fakturaId, List<Stavka> data) {
        this.activity = activity;
        this.fakturaId = fakturaId;
        this.original = data;
        this.stavke = new ArrayList<>();

        clickDel = new ClickDel();
        clickAdd = new ClickAdd();
        clickUpd = new ClickUpd();

        lptro = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        lpw10 = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                10.0f
        );
        lpw10.gravity = Gravity.BOTTOM;

        lpw00 = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0.0f
        );
        lpw00.gravity = Gravity.BOTTOM;

        lpico = new TableRow.LayoutParams(
                36,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0.0f
        );
        lpico.gravity = Gravity.CENTER_VERTICAL;
    }

    public void setTableLayout(TableLayout table_stavke) {
        this.table_stavke = table_stavke;
    }

    private TableRow createDataRow(final int n, final Stav sta) {
        String rb = " ";
        String opisStr = "";
        String iznosStr = "";
        int clickIcon = R.drawable.ic_add_circle_grey600_24dp;
        View.OnClickListener onClick = clickAdd;
        if (n > 0) {
            // data row
            rb = String.valueOf(n);
            opisStr = sta.opis;
            iznosStr = AppUtl.formatIznos(sta.iznos);
            clickIcon = R.drawable.ic_remove_circle_grey600_24dp;
            onClick = clickDel;
        }

        final TextView cellRb = new TextView(activity);
        final AutoCompleteTextView cellOpis = new AutoCompleteTextView(activity);
        final EditText cellIznos = new EditText(activity);
        final ImageView celDelete = new ImageView(activity);

        cellRb.setBackgroundColor(activity.getResources().getColor(R.color.tooltip_color));
        cellRb.setTextColor(Color.WHITE);
        cellRb.setText(rb);

        List<Opis> opisi = Api.getInstance().getListOpisSort();
        ArrayAdapter<Opis> adapter = new ArrayAdapter<Opis>(activity, android.R.layout.simple_dropdown_item_1line, opisi);

        cellOpis.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        cellOpis.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        cellOpis.setText(opisStr);
        cellOpis.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Opis opis = (Opis) parent.getAdapter().getItem(position);
                        BigDecimal iznos = opis.getIznos();
                        if (opis.isEuro()) {
                            BigDecimal kurs = Api.getInstance().getGlobal().getKursEuro();
                            iznos = iznos.multiply(kurs);
                        }
                        cellIznos.setText(AppUtl.formatIznos(iznos));
                        cellIznos.setError(null);
                    }
                }
        );
        cellOpis.setAdapter(adapter);

        cellIznos.setInputType(InputType.TYPE_CLASS_NUMBER);
        cellIznos.setKeyListener(DigitsKeyListener.getInstance("0123456789,."));
        if (n > 0) {
            // data row
            cellIznos.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            cellIznos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        clickUpd.onClick(v);
                    }
                    return false;
                }
            });
        } else {
            // append row
            cellIznos.setImeOptions(EditorInfo.IME_ACTION_DONE);
            cellIznos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        clickAdd.onClick(v);
                        return true;
                    }
                    return false;
                }
            });
        }
        cellIznos.setGravity(Gravity.RIGHT);
        cellIznos.setText(iznosStr);

        celDelete.setOnClickListener(onClick);
        celDelete.setImageResource(clickIcon);

        TableRow tableRow = new TableRow(activity);
        tableRow.addView(cellRb, lpw00);
        tableRow.addView(cellOpis, lpw10);
        tableRow.addView(cellIznos, lpw00);
        tableRow.addView(celDelete, lpico);

        return tableRow;
    }

    public void initTable() {

        stavke.clear();
        for (Stavka s : original) {
            stavke.add(new Stav(s.getOpis(), s.getIznos()));
        }

        // remove stavke, ostavi nulti red - heder
        int k = table_stavke.getChildCount();
        for (int i=k-1; i>0; i--) {
            View child = table_stavke.getChildAt(i);
            table_stavke.removeViewAt(i);
        }

        int n = 1;
        suma = BigDecimal.ZERO;
        for (Stav sta : stavke) {
            suma = suma.add(sta.iznos);
            TableRow tr = createDataRow(n, sta);
            table_stavke.addView(tr, lptro);
            n++;
        }

        TableRow appendRow = createDataRow(0, null);
        table_stavke.addView(appendRow, lptro);

        addSumaRow();
    }

    private void addSumaRow() {
        TextView tc1 = new TextView(activity);
        TextView tc2 = new TextView(activity);
        suma_view = new TextView(activity);
        TextView tc4 = new TextView(activity);

        tc1.setText("");

        tc2.setText("Ukupno:");
        tc2.setTextColor(Color.WHITE);
        tc2.setGravity(Gravity.RIGHT);
        tc2.setPadding(0, 0, 4, 0);

        suma_view.setText(AppUtl.formatIznos(suma));
        suma_view.setTextColor(Color.WHITE);
        suma_view.setGravity(Gravity.RIGHT);
        suma_view.setPadding(0, 0, 4, 0);

        tc4.setText(" ");

        TableRow tr = new TableRow(activity);
        tr.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.gradient_background));

        tr.addView(tc1, lpw00);
        tr.addView(tc2, lpw10);
        tr.addView(suma_view, lpw00);
        tr.addView(tc4, lpico);

        table_stavke.addView(tr, lptro);
    }

    private class ClickAdd implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TableRow tr = (TableRow) v.getParent();
            EditText et1 = (EditText) tr.getChildAt(1);
            EditText et2 = (EditText) tr.getChildAt(2);
            String opisStr = et1.getText().toString();
            String iznosStr = et2.getText().toString();
            if ("".equals(opisStr) && "".equals(iznosStr)) {
                // ubi tastaturu
                View view = activity.getCurrentFocus();
                if (view != null) {
                    view.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return;
            }
            BigDecimal iznos;
            try {
                iznos = (BigDecimal) AppUtl.fmtIznos.parse(iznosStr);
                et2.setText(AppUtl.formatIznos(iznos));
                et2.setError(null);
            } catch (ParseException ex) {
                et2.setError("rđav iznos");
                et2.requestFocus();
                return;
            }

            Stav sta = new Stav(opisStr, iznos);
            stavke.add(sta);
            int n = stavke.size();

            TableRow tableRow = createDataRow(n, sta);
            table_stavke.addView(tableRow, n, lptro);

            suma = suma.add(iznos);
            suma_view.setText(AppUtl.formatIznos(suma));

            et1.setText("");
            et2.setText("");

            et1.requestFocus();

        }
    }

    private class ClickDel implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TableRow tr = (TableRow) v.getParent();
            TextView tv = (TextView) tr.getChildAt(0);
            int row = Integer.valueOf(tv.getText().toString());

            suma = suma.subtract(stavke.get(row - 1).iznos);
            stavke.remove(row - 1);

            table_stavke.removeViewAt(row);
            for (int k = row; k <= stavke.size(); k++) {
                tr = (TableRow) table_stavke.getChildAt(k);
                tv = (TextView) tr.getChildAt(0);
                tv.setText(String.valueOf(k));
            }
            suma_view.setText(AppUtl.formatIznos(suma));
        }
    }

    private class ClickUpd implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TableRow tr = (TableRow) v.getParent();
            EditText et2 = (EditText) tr.getChildAt(2);
            String iznosStr = et2.getText().toString();
            BigDecimal iznos;
            try {
                iznos = (BigDecimal) AppUtl.fmtIznos.parse(iznosStr);
                et2.setText(AppUtl.formatIznos(iznos));
                et2.setError(null);
            } catch (ParseException ex) {
                et2.setError("rđav iznos");
                et2.requestFocus();
                return;
            }
            TextView tv = (TextView) tr.getChildAt(0);
            int row = Integer.valueOf(tv.getText().toString());
            suma = suma.subtract(stavke.get(row - 1).iznos);
            stavke.get(row - 1).iznos = iznos;
            suma = suma.add(iznos);
            suma_view.setText(AppUtl.formatIznos(suma));
        }
    }

    public boolean isModified() {
        if (original.size() != stavke.size()) {
            return true;
        }
        for (int i=0; i<stavke.size(); i++) {
            if (!stavke.get(i).opis.equals(original.get(i).getOpis())) {
                return true;
            }
            if (stavke.get(i).iznos.compareTo(original.get(i).getIznos()) != 0) {
                return true;
            }
        }
        return false;
    }

    public void stavke2original() {
        original.clear();
        int n = 1;
        for (Stav sta : stavke) {
            Stavka st = new Stavka();
            st.setId(n);
            st.setOpis(sta.opis);
            st.setIznos(sta.iznos);
            st.setFakturaId(fakturaId);
            original.add(st);
        }
    }
    private class Stav {

        public String opis;
        public BigDecimal iznos;

        public Stav(String opis, BigDecimal iznos) {
            this.opis = opis;
            this.iznos = iznos;
        }
    }

}

