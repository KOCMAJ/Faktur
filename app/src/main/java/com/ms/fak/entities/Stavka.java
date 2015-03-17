package com.ms.fak.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class Stavka implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String opis;
    private BigDecimal iznos;
    private Integer faktura_id;

    public Stavka() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getIznos() {
        return iznos;
    }

    public void setIznos(BigDecimal iznos) {
        this.iznos = iznos;
    }

    public Integer getFakturaId() {
        return faktura_id;
    }

    public void setFakturaId(Integer fakturaId) {
        this.faktura_id = fakturaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Stavka)) {
            return false;
        }
        Stavka other = (Stavka) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fakturpersist.Stavka[ id=" + id + " ]";
    }
    
}
