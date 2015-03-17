package com.ms.fak.opis;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ms.fak.R;
import com.ms.fak.entities.Api;
import com.ms.fak.entities.Opis;
import com.ms.fak.utl.Updater;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpisTaskCreate extends AsyncTask<Void, Void, Void> {
    private final Opis opis;
    private final Context context;
    private final Updater updater;
    private String message;
    private int newId;


    public OpisTaskCreate(Opis opis, Context context, Updater updater) {
        this.opis = opis;
        this.context = context;
        this.updater = updater;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Api api = Api.getInstance();
        try {
            newId = api.createOpis(opis);
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
            opis.setId(Integer.valueOf(newId));
            message = context.getString(R.string.snimljena_usluga)+newId;
            updater.updateData();
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
