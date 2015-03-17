package com.ms.fak.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Api {

    private static Api INSTANCE;

    private final Global global = new Global();
    private final List<Komitent> listKomitent = new ArrayList<Komitent>();
    private final List<Faktura> listFaktura = new ArrayList<Faktura>();
    private final List<Opis> listOpis = new ArrayList<Opis>();

    private final Gson gson;

    private Api() {
        BooleanSerializer seri = new BooleanSerializer();
        BooleanDeserializer deseri = new BooleanDeserializer();
        gson = new GsonBuilder()
                .registerTypeAdapter(boolean.class, seri)
                .registerTypeAdapter(boolean.class, deseri)
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public static Api getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Api();
        }
        return INSTANCE;
    }

    // na osnovu refreshAllData()
    public Global getGlobal() {
        return global;
    }

    public List<Komitent> getListKomitent() {
        return listKomitent;
    }
    public List<Komitent> getListKomitentSort() {
        List<Komitent> sorted = new ArrayList<Komitent>();
        sorted.addAll(getListKomitent());
        Collections.sort(sorted);
        return sorted;
    }

    public List<Faktura> getListFaktura() {
        return listFaktura;
    }

    public List<Opis> getListOpis() {
        return listOpis;
    }

    public List<Opis> getListOpisSort() {
        List<Opis> sorted = new ArrayList<Opis>();
        sorted.addAll(getListOpis());
        Collections.sort(sorted);
        return sorted;
    }

    public Opis getOpis(int id) {
        return listOpis.get(id);
    }

    public Opis getOpisAuto() {
        for (Opis opis : getListOpis()) {
            if (opis.isAuto()) {
                return opis;
            }
        }
        return null;
    }

    public Komitent getKomitent(int id) {
        return listKomitent.get(id);
    }
    public Komitent getKomitentById(Integer id) {
        for (Komitent komitent : listKomitent) {
            if (id.compareTo(komitent.getId())==0) {
                return komitent;
            }
        }
        return null;
    }
    public Faktura getFaktura(int id) {
        return listFaktura.get(id);
    }

    public void refreshAllData() throws IOException {
        refreshGlobal();
        refreshOpis();
        refreshKomitentAndFaktura();
    }

    public void refreshKomitentAndFaktura() throws IOException {
        Type listType = new TypeToken<List<Komitent>>() {
        }.getType();
        String json = fakturService("komitenti", "GET", null);
        List<Komitent> data = gson.fromJson(json, listType);
        listKomitent.clear();
        listKomitent.addAll(data);

        List<Faktura> faks = new ArrayList<Faktura>();
        for (Komitent kom : data) {
            faks.addAll(kom.getFakturaCollection());
        }
        Collections.sort(faks, Collections.reverseOrder());
        listFaktura.clear();
        listFaktura.addAll(faks);
    }

    public void refreshOpis() throws IOException {
        Type listType = new TypeToken<List<Opis>>() {
        }.getType();
        String json = fakturService("opisi", "GET", null);
        List<Opis> data = gson.fromJson(json, listType);
        listOpis.clear();
        listOpis.addAll(data);
    }

    public void refreshGlobal() throws IOException {
        String json = fakturService("global", "GET", null);
        Global data = gson.fromJson(json, Global.class);

        global.setId(data.getId());
        global.setFirmaNaziv1(data.getFirmaNaziv1());
        global.setFirmaNaziv2(data.getFirmaNaziv2());
        global.setFirmaLogo(data.getFirmaLogo());
        global.setFirmaMesto(data.getFirmaMesto());
        global.setFirmaAdresa(data.getFirmaAdresa());
        global.setFirmaPib(data.getFirmaPib());
        global.setFirmaMbr(data.getFirmaMbr());
        global.setFirmaDel(data.getFirmaDel());
        global.setFirmaFax(data.getFirmaFax());
        global.setFirmaTel(data.getFirmaTel());
        global.setFirmaZiro(data.getFirmaZiro());
        global.setFakturaMesto(data.getFakturaMesto());
        global.setFakturaDatum(data.getFakturaDatum());
        global.setDatumPrometa(data.getDatumPrometa());
        global.setNapomenaPdv(data.getNapomenaPdv());
        global.setNacinPlacanja(data.getNacinPlacanja());
        global.setKursEuro(data.getKursEuro());
        global.setUslugaObim(data.getUslugaObim());

    }

    // global
    public Integer updateGlobal() throws IOException {
        String json = gson.toJson(global, Global.class);
        String affected = fakturService("global", "PUT", json);
        return Integer.valueOf(affected);
    }

    // Opis
    public Integer createOpis(Opis data) throws IOException {
        String json = gson.toJson(data, Opis.class);
        String newId = fakturService("opisi", "POST", json);
        return Integer.valueOf(newId);
    }

    public Integer updateOpis(Opis data) throws IOException {
        String json = gson.toJson(data, Opis.class);
        String affected = fakturService("opisi/" + data.getId(), "PUT", json);
        return Integer.valueOf(affected);
    }

    public Integer deleteOpis(Opis data) throws IOException {
        String affected = fakturService("opisi/" + data.getId(), "DELETE", null);
        return Integer.valueOf(affected);
    }

    // Komitent
    public Integer createKomitent(Komitent data) throws IOException {
        String json = gson.toJson(data, Komitent.class);
        String newId = fakturService("komitenti", "POST", json);
        return Integer.valueOf(newId);
    }

    public Integer updateKomitent(Komitent data) throws IOException {
        String json = gson.toJson(data, Komitent.class);
        String affected = fakturService("komitenti/" + data.getId(), "PUT", json);
        return Integer.valueOf(affected);
    }

    public Integer deleteKomitent(Komitent data) throws IOException {
        String affected = fakturService("komitenti/" + data.getId(), "DELETE", null);
        return Integer.valueOf(affected);
    }

    public Integer deleteKomitents(List<Komitent> list) throws IOException {
        Integer result = 0;
        for (Komitent komitent : list) {
            result = result + deleteKomitent(komitent);
        }
        return result;
    }

    // Faktura
    public Integer createFaktura(Faktura data) throws IOException {
        String json = gson.toJson(data, Faktura.class);
        String newId = fakturService("fakture", "POST", json);
        return Integer.valueOf(newId);
    }

    public Integer updateFaktura(Faktura data) throws IOException {
        String json = gson.toJson(data, Faktura.class);
        String affected = fakturService("fakture/" + data.getId(), "PUT", json);
        return Integer.valueOf(affected);
    }

    public Integer deleteFaktura(Faktura data) throws IOException {
        String affected = fakturService("fakture/" + data.getId(), "DELETE", null);
        return Integer.valueOf(affected);
    }

    public Integer getFakturaNextBroj() {
        Integer result = Integer.valueOf(1);
        if (listFaktura.size()>0) {
            // reverse order!
            String sbroj = listFaktura.get(0).getBroj();
            Integer ibroj = Integer.valueOf(sbroj);
            result = ibroj + 1;
        }
        return result;
    }

    private final String URL_BASE = "http://bobo.in.rs/FakturService/";
    private final String OPT_NAME = "X-Powered-By";
    private final String OPT_VALUE = "KOCMAJ 2014";

    // GET, DELETE, POST i PUT front
    private String fakturService(String path, String method, String jsonData)
            throws MalformedURLException, UnsupportedEncodingException, IOException {
        StringBuilder output = new StringBuilder();
        URL url = new URL(URL_BASE + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty(OPT_NAME, OPT_VALUE);

        if (jsonData != null) {
            // Send post request
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write(jsonData);
            writer.flush();
            writer.close();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code : " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            output.append(line);
        }
        br.close();

        conn.disconnect();
        conn = null;
        return output.toString();
    }

}
