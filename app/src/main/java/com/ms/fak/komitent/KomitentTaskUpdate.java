package com.ms.fak.komitent;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Komitent;
import com.ms.fak.utl.Updater;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KomitentTaskUpdate extends AsyncTask<Void, Void, Void> {
    private final Komitent komitent;
    private final Context context;
    private final Updater updater;
    private String message;
    private int affected;

    public KomitentTaskUpdate(Komitent komitent, Context context, Updater updater) {
        this.komitent = komitent;
        this.context = context;
        this.updater = updater;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            affected = Api.getInstance().updateKomitent(komitent);
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(KomitentTaskUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message == null) {
            if (affected == 0) {
                message = context.getString(R.string.nema_izmena);
            } else {
                message = context.getString(R.string.presnimljen_komitent);
            }
            updater.updateData();
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}

