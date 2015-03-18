package com.ms.fak.faktura;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Faktura;
import com.ms.fak.entities.Global;
import com.ms.fak.entities.Komitent;
import com.ms.fak.pdf.FakturaPdf;
import com.ms.fak.pdf.PdfActivity;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.AppUtl;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Tooltip;
import com.ms.fak.utl.Updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class FakturaFragmentDetail extends Fragment implements Updater {
    private FragmentsCallbacks mCallbacks;

    private Faktura faktura;
    private Komitent komitent;

    private EditText faktura_broj;
    private EditText faktura_mesto;
    private EditText faktura_datum;
    private EditText faktura_promet;
    private EditText placanje;
    private EditText napomena;

    private Spinner komitent_spinner;

    private TextView firma;
    private Tooltip tooltip_firma;
    private TextView komitent_view;
    private Tooltip tooltip_komitent;

    private TableModel tableModel;

    public FakturaFragmentDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        faktura = (Faktura) getArguments().getSerializable("faktura");
        tableModel = new TableModel(getActivity(), faktura.getId(), faktura.getStavkaCollection());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faktura_detail, container, false);

        firma = (TextView) view.findViewById(R.id.firma);

        faktura_broj = (EditText) view.findViewById(R.id.faktura_broj);
        faktura_mesto = (EditText) view.findViewById(R.id.faktura_mesto);
        faktura_datum = (EditText) view.findViewById(R.id.faktura_datum);
        faktura_promet = (EditText) view.findViewById(R.id.faktura_promet);

        komitent_spinner = (Spinner) view.findViewById(R.id.komitent_spinner);
        komitent_view = (TextView) view.findViewById(R.id.komitent_view);

        tableModel.setTableLayout((TableLayout) view.findViewById(R.id.table_stavke));

        placanje = (EditText) view.findViewById(R.id.placanje);
        napomena = (EditText) view.findViewById(R.id.napomena);

        updateData();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentsCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getString(R.string.implement_callbacks));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mCallbacks.isDrawerOpen()) {
            inflater.inflate(R.menu.save_delete_fresh_pdf, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                gasiDekor(getActivity());
                if (isValid(getActivity())) {
                    saveData(getActivity());
                }
                return true;
            case R.id.action_delete:
                gasiDekor(getActivity());
                delete();
                return true;
            case R.id.action_refresh:
                gasiDekor(getActivity());
                new AppRefreshTask(getActivity(), this).execute();
                return true;
            case R.id.action_pdf:
                gasiDekor(getActivity());
                makePdf();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFirma() {
        Global glob = Api.getInstance().getGlobal();
        firma.setText(glob.getFirmaNaziv1() + "\n" + glob.getFirmaNaziv2());

        String tip = glob.getFirmaNaziv1() +
                "\n" + glob.getFirmaNaziv2() +
                "\n" + glob.getFirmaMesto() +
                "\n" + glob.getFirmaAdresa() +
                "\n\ntel. " + glob.getFirmaTel() +
                "\n fax " + glob.getFirmaFax() +
                "\n\nPIB " + glob.getFirmaPib() +
                "\nMatični broj " + glob.getFirmaMbr() +
                "\nŠifra delatnosti " + glob.getFirmaDel() +
                "\nŽiro račun " + glob.getFirmaZiro();

        tooltip_firma = new Tooltip(getActivity());
        tooltip_firma.setText(tip);
        tooltip_firma.setTarget(firma);
        firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tooltip_firma.isShown()) {
                    tooltip_firma.dismiss();
                } else {
                    if (tooltip_komitent.isShown()) tooltip_komitent.dismiss();
                    tooltip_firma.show();
                }
            }
        });
    }

    private void initRacun() {
        faktura_broj.setText(faktura.getBroj());
        faktura_mesto.setText(faktura.getMesto());
        faktura_datum.setText(AppUtl.sdf.format(faktura.getDatum()));
        faktura_promet.setText(AppUtl.sdf.format(faktura.getDatumPrometa()));
    }

    private void setKomitent(Komitent komitent) {
        this.komitent = komitent;
        String tip = komitent.getNaziv() +
                "\n" + komitent.getMesto() +
                "\n" + komitent.getAdresa() +
                "\nPIB " + komitent.getPib() +
                ("".equals(komitent.getEmail()) ? "" : "\n"+komitent.getEmail());
        if (tooltip_komitent == null) {
            tooltip_komitent = new Tooltip(getActivity());
            tooltip_komitent.setTarget(komitent_view);
            komitent_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tooltip_komitent.isShown()) {
                        tooltip_komitent.dismiss();
                    } else {
                        if (tooltip_firma.isShown()) tooltip_firma.dismiss();
                        tooltip_komitent.show();
                    }
                }
            });
        } else {
            if (tooltip_komitent.isShown()) {
                tooltip_komitent.dismiss();
            }
        }
        tooltip_komitent.setText(tip);

    }

    private void initKomitent() {
        final List<Komitent> lista = Api.getInstance().getListKomitentSort();
        ArrayAdapter<Komitent> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        komitent_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setKomitent(lista.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        komitent_spinner.setAdapter(adapter);
        if (!lista.isEmpty()) {
            Integer id = faktura.getKomitentId();
            int position = 0;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(id)) {
                    position = i;
                    break;
                }
            }
            komitent_spinner.setSelection(position);
            // Ne, komitent se postavlja sa setSelectedItem(), side efect
            //komitent = obj.getData().getKomitentId();
        }
    }

    private void initFooter() {
        placanje.setText(faktura.getNacinPlacanja());
        napomena.setText(faktura.getNapomenaPdv());
    }

    private void delete() {
        if (faktura.getId() == null) {
            // delete na new task - samo odustani i zatvori fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }
        } else {
            // stvarno brisanje
            String title = getActivity().getString(R.string.brisanje_fakture);
            String message = getActivity().getString(R.string.racun_br) + faktura.getBroj() + getActivity().getString(R.string.od) + AppUtl.getDatum(faktura.getDatum());
            message = message + getActivity().getString(R.string.n_da_brisem);
            new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getActivity().getString(R.string.brisi), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            new FakturaTaskDelete(faktura, getActivity()).execute();
                        }
                    })
                    .setNegativeButton(getActivity().getString(R.string.pismani), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean isValid(Context context) {
        if (faktura_broj.getText().toString().trim().isEmpty()) {
            String message = context.getString(R.string.obavezan_broj);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            faktura_broj.requestFocus();
            return false;
        }
        if (faktura_mesto.getText().toString().trim().isEmpty()) {
            String message = context.getString(R.string.obavezno_mesto);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            faktura_mesto.requestFocus();
            return false;
        }
        try {
            Date datum = AppUtl.sdf.parse(faktura_datum.getText().toString());
        } catch (ParseException e) {
            String message = context.getString(R.string.rdjav_datum);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            faktura_datum.requestFocus();
            return false;
        }
        try {
            Date promet = AppUtl.sdf.parse(faktura_promet.getText().toString());
        } catch (ParseException e) {
            String message = context.getString(R.string.rdjav_datum);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            faktura_promet.requestFocus();
            return false;
        }
        return true;
        //return tableModel.isValid();
    }

    @Override
    public boolean isModified() {
        Date datum = null;
        Date promet = null;
        try {
            datum = AppUtl.sdf.parse(faktura_datum.getText().toString());
            promet = AppUtl.sdf.parse(faktura_promet.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!faktura.getBroj().equals(faktura_broj.getText().toString())) {
            return true;
        }
        if (!faktura.getMesto().equals(faktura_mesto.getText().toString())) {
            return true;
        }
        if (!faktura.getDatum().equals(datum)) {
            return true;
        }
        if (!faktura.getDatumPrometa().equals(promet)) {
            return true;
        }
        if (!faktura.getNapomenaPdv().equals(napomena.getText().toString())) {
            return true;
        }
        if (!faktura.getNacinPlacanja().equals(placanje.getText().toString())) {
            return true;
        }
        return tableModel.isModified();
    }

    @Override
    public void updateData() {
        initFirma();
        initRacun();
        initKomitent();
        tableModel.initTable();
        initFooter();
    }

    @Override
    public void saveData(Context context) {
        Date datum = null;
        Date promet = null;
        try {
            datum = AppUtl.sdf.parse(faktura_datum.getText().toString());
            promet = AppUtl.sdf.parse(faktura_promet.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        faktura.setKomitentId(komitent.getId());
        faktura.setBroj(faktura_broj.getText().toString());
        faktura.setDatum(datum);
        faktura.setDatumPrometa(promet);
        faktura.setMesto(faktura_mesto.getText().toString());
        faktura.setNapomenaPdv(napomena.getText().toString());
        faktura.setNacinPlacanja(placanje.getText().toString());
        faktura.setPrinted(false);
        tableModel.stavke2original();
        if (faktura.getId() == null) {
            new FakturaTaskCreate(faktura, getActivity(), this).execute();
        } else {
            new FakturaTaskUpdate(faktura, getActivity(), this).execute();
        }
    }


    @Override
    public void gasiDekor(Activity activity) {
        if (tooltip_firma.isShown()) tooltip_firma.dismiss();
        if (tooltip_komitent.isShown()) tooltip_komitent.dismiss();
        AppUtl.gasiKeyb(activity);
    }

    public void makePdf() {
        FileOutputStream outStream;
        File pdfFile;
        String filepath = getString(R.string.pdf_folder);
        String filename = "Faktura "+faktura.getBroj()+".pdf";
        //check if external storage is available so that we can dump our PDF file there
        if (!AppUtl.isExternalStorageAvailable() || AppUtl.isExternalStorageReadOnly()) {
            String message = "External Storage not available or you don't have permission to write";
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }
        //sdcard/Android/data/com.ms.fak/files/fakture/Faktura 114.pdf
        pdfFile = new File(getActivity().getExternalFilesDir(filepath), filename);
        try {
            outStream = new FileOutputStream(pdfFile);
            FakturaPdf fpdf = new FakturaPdf(faktura, outStream);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

        //if (AppUtl.isPdfSupported(getActivity())) {
        //    AppUtl.openPdf(getActivity(), Uri.fromFile(pdfFile));
        //} else {
            Intent intent = new Intent(getActivity(), PdfActivity.class);
            intent.putExtra(PdfActivity.EXTRA_FILE, pdfFile);
            intent.putExtra(PdfActivity.EXTRA_EMAIL, komitent.getEmail());
            getActivity().startActivity(intent);
        //}

    }
}

