package com.ms.fak.komitent;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Komitent;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KomitentTaskDelete extends AsyncTask<Void, Void, Void> {

    private final Komitent komitent;
    private final Context context;
    private String message;
    private int affected;

    public KomitentTaskDelete(Komitent komitent, Context context) {
        this.komitent = komitent;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Api api = Api.getInstance();
        try {
            affected = api.deleteKomitent(komitent);
            api.refreshKomitentAndFaktura();
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(KomitentTaskDelete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message == null) {
            if (affected == 0) {
                message = context.getString(R.string.nista_za_brisanje);
            } else {
                message = context.getString(R.string.uklonjen_komitent);
            }
        } else {
            message = context.getString(R.string.greska) + message;
        }
        // zatvori fragment
        FragmentManager fm =((ActionBarActivity) context).getSupportFragmentManager();
        if (fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
