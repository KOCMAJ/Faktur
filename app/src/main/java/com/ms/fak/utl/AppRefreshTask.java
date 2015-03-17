package com.ms.fak.utl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.ms.fak.R;
import com.ms.fak.entities.Api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppRefreshTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final Updater updater;
    private ProgressDialog progress;
    private String message = null;

    public AppRefreshTask(Context context, Updater updater) {
        this.context = context;
        this.updater = updater;
    }

    protected Void doInBackground(Void... arg0) {
        try {
            Api.getInstance().refreshAllData();
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(AppRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(context, context.getString(R.string.osvezavam), context.getString(R.string.strpljenje), true, false);
    }

    @Override
    protected void onPostExecute(Void result) {
        if (updater != null) {
            updater.updateData();
        }
        progress.dismiss();
        if (message != null) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.greska))
                    .setMessage(message)
                    .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

}
