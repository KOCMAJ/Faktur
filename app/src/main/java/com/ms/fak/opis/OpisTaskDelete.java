package com.ms.fak.opis;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Opis;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpisTaskDelete extends AsyncTask<Void, Void, Void> {
    private final Opis opis;
    private final Context context;
    private String message;
    private int affected;

    public OpisTaskDelete(Opis opis, Context context) {
        this.opis = opis;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Api api = Api.getInstance();
        try {
            affected = api.deleteOpis(opis);
            api.refreshOpis();
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(OpisTaskUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message == null) {
            if (affected == 0) {
                message = context.getString(R.string.nista_za_brisanje);
            } else {
                message = context.getString(R.string.uklonjena_usluga);
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
