package com.ms.fak.faktura;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Faktura;
import com.ms.fak.utl.Updater;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FakturaTaskUpdate extends AsyncTask<Void, Void, Void> {
    private final Faktura faktura;
    private final Context context;
    private final Updater updater;
    private String message;
    private int affected;

    public FakturaTaskUpdate(Faktura faktura, Context context, Updater updater) {
        this.faktura = faktura;
        this.context = context;
        this.updater = updater;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            affected = Api.getInstance().updateFaktura(faktura);
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(FakturaTaskUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message == null) {
            if (affected == 0) {
                message = context.getString(R.string.nema_izmena);
            } else {
                message = context.getString(R.string.presnimljena_faktura);
            }
            updater.updateData();
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}

