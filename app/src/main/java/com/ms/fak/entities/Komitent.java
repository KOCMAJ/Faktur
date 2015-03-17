package com.ms.fak.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Komitent implements Serializable, Comparable<Komitent> {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String naziv;
    private String mesto;
    private String adresa;
    private String pib;
    private String email;
    private String usluga_opis;
    private boolean usluga_obim;
    private BigDecimal usluga_iznos;
    private boolean usluga_euro;
    private List<Faktura> fakturaCollection;

    public Komitent() {
        setId(null);
        setNaziv("");
        setMesto("");
        setAdresa("");
        setPib("");
        setEmail("");
        Opis opis = Api.getInstance().getOpisAuto();
        if (opis != null) {
            setUslugaOpis(opis.getOpis());
            setUslugaObim(opis.isObim());
            setUslugaIznos(opis.getIznos());
            setUslugaEuro(opis.isEuro());
        } else {
            setUslugaOpis("");
            setUslugaObim(true);
            setUslugaIznos(BigDecimal.ZERO);
            setUslugaEuro(false);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUslugaOpis() {
        return usluga_opis;
    }

    public void setUslugaOpis(String uslugaOpis) {
        this.usluga_opis = uslugaOpis;
    }

    public boolean isUslugaObim() {
        return usluga_obim;
    }

    public void setUslugaObim(boolean uslugaObim) {
        this.usluga_obim = uslugaObim;
    }

    public BigDecimal getUslugaIznos() {
        return usluga_iznos;
    }

    public void setUslugaIznos(BigDecimal uslugaIznos) {
        this.usluga_iznos = uslugaIznos;
    }

    public boolean isUslugaEuro() {
        return usluga_euro;
    }

    public void setUslugaEuro(boolean uslugaEuro) {
        this.usluga_euro = uslugaEuro;
    }

    public List<Faktura> getFakturaCollection() {
        return fakturaCollection;
    }

    public void setFakturaCollection(List<Faktura> fakturaCollection) {
        this.fakturaCollection = fakturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Komitent)) {
            return false;
        }
        Komitent other = (Komitent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getMestoAdresa() {
        return mesto + (adresa=="" ? "" : ", " + adresa);
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int compareTo(Komitent obj) {
        return this.getNaziv().compareTo(obj.naziv);
    }

}
