package com.ms.fak.entities;

import com.ms.fak.utl.AppUtl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Faktura implements Serializable, Comparable<Faktura> {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String broj;
    private String mesto;
    private Date datum;
    private Date datum_prometa;
    private String napomena_pdv;
    private String nacin_placanja;
    private boolean printed;
    private Integer komitent_id;
    private List<Stavka> stavkaCollection;

    public Faktura() {
        Api api = Api.getInstance();
        // datum bez time!
        datum = AppUtl.getDanas();

        this.setId(null);
        this.setKomitentId(null);
        this.setStavkaCollection(new ArrayList<Stavka>());

        this.setBroj(api.getFakturaNextBroj().toString());
        this.setDatum(datum);
        this.setDatumPrometa(datum);
        this.setMesto(api.getGlobal().getFakturaMesto());
        this.setNapomenaPdv(api.getGlobal().getNapomenaPdv());
        this.setNacinPlacanja(api.getGlobal().getNacinPlacanja());
        this.setPrinted(false);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getDatumPrometa() {
        return datum_prometa;
    }

    public void setDatumPrometa(Date datumPrometa) {
        this.datum_prometa = datumPrometa;
    }

    public String getNapomenaPdv() {
        return napomena_pdv;
    }

    public void setNapomenaPdv(String napomenaPdv) {
        this.napomena_pdv = napomenaPdv;
    }

    public String getNacinPlacanja() {
        return nacin_placanja;
    }

    public void setNacinPlacanja(String nacinPlacanja) {
        this.nacin_placanja = nacinPlacanja;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public Integer getKomitentId() {
        return komitent_id;
    }

    public void setKomitentId(Integer komitentId) {
        this.komitent_id = komitentId;
    }

    public List<Stavka> getStavkaCollection() {
        return stavkaCollection;
    }

    public void setStavkaCollection(List<Stavka> stavkaCollection) {
        this.stavkaCollection = stavkaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Faktura)) {
            return false;
        }
        Faktura other = (Faktura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fakturpersist.Faktura[ id=" + id + " ]";
    }

    public int compareTo(Faktura obj) {
        int objId = obj.getId();
        return this.getId() - objId;
    }

}
