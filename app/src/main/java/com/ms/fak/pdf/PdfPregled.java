package com.ms.fak.pdf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.utl.AppRefreshTask;
import com.ms.fak.utl.FragmentsCallbacks;
import com.ms.fak.utl.Updater;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PdfPregled extends ListFragment implements Updater {
    private FragmentsCallbacks mCallbacks;
    private PdfAdapter adapter;
    private final ArrayList<File> fileList = new ArrayList<>();

    public PdfPregled() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PdfAdapter(getActivity(), fileList);
        setListAdapter(adapter);

        updateData();
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
            inflater.inflate(R.menu.delete_fresh, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deletePdfs();
                return true;
            case R.id.action_refresh:
                new AppRefreshTask(getActivity(), this).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        File file = (File) l.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), PdfActivity.class);
        intent.putExtra(PdfActivity.EXTRA_FILE, file);
        intent.putExtra(PdfActivity.EXTRA_EMAIL, "");
        getActivity().startActivity(intent);
    }

    @Override
    public boolean isValid(Context context) {
        return true;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void updateData() {
        fileList.clear();
        //getting SDcard path
        String folderName = getActivity().getString(R.string.pdf_folder);
        File folder = getActivity().getExternalFilesDir(folderName);
        assert folder != null;
        String path = folder.getAbsolutePath();
        File dir = new File(path);
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            Collections.addAll(fileList, listFile);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveData(Context context) {
    }

    @Override
    public void gasiDekor(Activity activity) {
    }

    private void deletePdfs() {
        if (fileList.isEmpty()) {
            return;
        }
        String title = getActivity().getString(R.string.brisi_pdfs);
        String message =  getActivity().getString(R.string.n_da_brisem);
        new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getActivity().getString(R.string.brisi), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        int n = 0;
                        for (File pdf : fileList) {
                            if (pdf.delete()) {
                                n++;
                            }
                        }
                        Toast.makeText(getActivity(), getActivity().getString(R.string.pdfa_obrisano)+n, Toast.LENGTH_SHORT).show();
                        updateData();
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
