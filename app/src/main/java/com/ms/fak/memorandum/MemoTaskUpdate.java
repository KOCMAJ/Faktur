package com.ms.fak.memorandum;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ms.fak.entities.Api;
import com.ms.fak.utl.Updater;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemoTaskUpdate extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final Updater updater;
    private String message;
    private int affected;

    public MemoTaskUpdate(Context context, Updater updater) {
        this.context = context;
        this.updater = updater;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            affected = Api.getInstance().updateGlobal();
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(MemoTaskUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (message == null) {
            message = "Podataka presnimljeno: "+affected;
            updater.updateData();
        }
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}
