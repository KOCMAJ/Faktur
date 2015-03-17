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

public class KomitentTaskCreate extends AsyncTask<Void, Void, Void> {
    private final Komitent komitent;
    private final Context context;
    private final Updater updater;
    private String message;
    private int newId;

    public KomitentTaskCreate(Komitent komitent, Context context, Updater updater) {
        this.komitent = komitent;
        this.context = context;
        this.updater = updater;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            newId = Api.getInstance().createKomitent(komitent);
            Api.getInstance().refreshKomitentAndFaktura();
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(KomitentTaskCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message == null) {
            komitent.setId(Integer.valueOf(newId));
            message = context.getString(R.string.snimljen_komitent)+newId;
            updater.updateData();
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}

