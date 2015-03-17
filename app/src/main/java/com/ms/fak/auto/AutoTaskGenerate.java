package com.ms.fak.auto;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ms.fak.entities.Api;
import com.ms.fak.entities.Faktura;
import com.ms.fak.entities.Global;
import com.ms.fak.entities.Komitent;
import com.ms.fak.entities.Stavka;
import com.ms.fak.utl.Updater;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoTaskGenerate extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final Updater updater;
    private String message;
    private int affected;
    private int newId;

    private final List<Komitent> komitenti;

    public AutoTaskGenerate(Context context, Updater updater, List<Komitent> komitenti) {
        this.context = context;
        this.updater = updater;
        this.komitenti = komitenti;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // prvo update Global
        try {
            affected = Api.getInstance().updateGlobal();
        } catch (IOException ex) {
            message = ex.getMessage();
            Logger.getLogger(AutoTaskGenerate.class.getName()).log(Level.SEVERE, null, ex);
        }
        // a sada fakture
        for (Komitent komitent : komitenti) {
            Faktura faktura = generateFak(komitent);
            try {
                newId = Api.getInstance().createFaktura(faktura);
                faktura.setId(Integer.valueOf(newId));
                Api.getInstance().refreshKomitentAndFaktura();
                affected++;
            } catch (IOException ex) {
                message = ex.getMessage();
                Logger.getLogger(AutoTaskGenerate.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private Faktura generateFak(Komitent komitent) {
        Api api = Api.getInstance();
        Global glob = api.getGlobal();
        Faktura faktura = new Faktura();
        faktura.setBroj(api.getFakturaNextBroj().toString());
        faktura.setKomitentId(komitent.getId());
        faktura.setDatum(glob.getFakturaDatum());
        faktura.setDatumPrometa(glob.getDatumPrometa());
        faktura.setMesto(glob.getFakturaMesto());
        faktura.setNacinPlacanja(glob.getNacinPlacanja());
        faktura.setNapomenaPdv(glob.getNapomenaPdv());
        faktura.setPrinted(false);
        // stavka
        String opis = komitent.getUslugaOpis();
        if (komitent.isUslugaObim()) {
            opis = opis + " " + glob.getUslugaObim();
        }
        BigDecimal iznos = komitent.getUslugaIznos();
        if (komitent.isUslugaEuro()) {
            iznos = iznos.multiply(glob.getKursEuro());
        }

        Stavka sta = new Stavka();
        sta.setFakturaId(faktura.getId());
        sta.setOpis(opis);
        sta.setIznos(iznos);

        faktura.setStavkaCollection(new ArrayList<Stavka>());
        faktura.getStavkaCollection().add(sta);

        return faktura;
    }

}
