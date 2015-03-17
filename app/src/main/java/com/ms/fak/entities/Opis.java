package com.ms.fak.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class Opis implements Serializable, Comparable<Opis> {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String opis;
    private boolean obim;
    private BigDecimal iznos;
    private boolean euro;
    private boolean auto;

    public Opis() {
        id = null;
        opis = "";
        iznos = BigDecimal.ZERO;
        euro = false;
        obim = true;
        auto = false;
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

    public boolean isObim() {
        return obim;
    }

    public void setObim(boolean obim) {
        this.obim = obim;
    }

    public BigDecimal getIznos() {
        return iznos;
    }

    public void setIznos(BigDecimal iznos) {
        this.iznos = iznos;
    }

    public boolean isEuro() {
        return euro;
    }

    public void setEuro(boolean euro) {
        this.euro = euro;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    @Override
    public String toString() {
        return opis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Opis)) {
            return false;
        }
        Opis other = (Opis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Opis obj) {
        return this.getOpis().compareTo(obj.getOpis());
    }

}
