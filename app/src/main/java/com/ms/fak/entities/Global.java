package com.ms.fak.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Global implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String firma_naziv1;
    private String firma_naziv2;
    private String firma_logo;
    private String firma_mesto;
    private String firma_adresa;
    private String firma_pib;
    private String firma_mbr;
    private String firma_del;
    private String firma_fax;
    private String firma_tel;
    private String firma_ziro;
    private String faktura_mesto;
    private Date faktura_datum;
    private Date datum_prometa;
    private String napomena_pdv;
    private String nacin_placanja;
    private BigDecimal kurs_euro;
    private String usluga_obim;

    public Global() {
    }

    public Global(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirmaNaziv1() {
        return firma_naziv1;
    }

    public void setFirmaNaziv1(String firmaNaziv1) {
        this.firma_naziv1 = firmaNaziv1;
    }

    public String getFirmaNaziv2() {
        return firma_naziv2;
    }

    public void setFirmaNaziv2(String firmaNaziv2) {
        this.firma_naziv2 = firmaNaziv2;
    }

    public String getFirmaLogo() {
        return firma_logo;
    }

    public void setFirmaLogo(String firmaLogo) {
        this.firma_logo = firmaLogo;
    }

    public String getFirmaMesto() {
        return firma_mesto;
    }

    public void setFirmaMesto(String firmaMesto) {
        this.firma_mesto = firmaMesto;
    }

    public String getFirmaAdresa() {
        return firma_adresa;
    }

    public void setFirmaAdresa(String firmaAdresa) {
        this.firma_adresa = firmaAdresa;
    }

    public String getFirmaPib() {
        return firma_pib;
    }

    public void setFirmaPib(String firmaPib) {
        this.firma_pib = firmaPib;
    }

    public String getFirmaMbr() {
        return firma_mbr;
    }

    public void setFirmaMbr(String firmaMbr) {
        this.firma_mbr = firmaMbr;
    }

    public String getFirmaDel() {
        return firma_del;
    }

    public void setFirmaDel(String firmaDel) {
        this.firma_del = firmaDel;
    }

    public String getFirmaFax() {
        return firma_fax;
    }

    public void setFirmaFax(String firmaFax) {
        this.firma_fax = firmaFax;
    }

    public String getFirmaTel() {
        return firma_tel;
    }

    public void setFirmaTel(String firmaTel) {
        this.firma_tel = firmaTel;
    }

    public String getFirmaZiro() {
        return firma_ziro;
    }

    public void setFirmaZiro(String firmaZiro) {
        this.firma_ziro = firmaZiro;
    }

    public String getFakturaMesto() {
        return faktura_mesto;
    }

    public void setFakturaMesto(String fakturaMesto) {
        this.faktura_mesto = fakturaMesto;
    }

    public Date getFakturaDatum() {
        return faktura_datum;
    }

    public void setFakturaDatum(Date fakturaDatum) {
        this.faktura_datum = fakturaDatum;
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

    public BigDecimal getKursEuro() {
        return kurs_euro;
    }

    public void setKursEuro(BigDecimal kursEuro) {
        this.kurs_euro = kursEuro;
    }

    public String getUslugaObim() {
        return usluga_obim;
    }

    public void setUslugaObim(String uslugaObim) {
        this.usluga_obim = uslugaObim;
    }

    public String getFirmaFmtTel() {
        return "tel. " + firma_tel;
    }
    public String getFirmaFmtFax() {
        return "fax " + firma_fax;
    }
    public String getFirmaFmtPib() {
        return "PIB " +firma_pib;
    }
    public String getFirmaFmtMbr() {
        return "Matični broj " + firma_mbr;
    }
    public String getFirmaFmtDel() {
        return "Šifra delatnosti " + firma_del;
    }
    public String getFirmaFmtZiro() {
        return "Žiro račun " + firma_ziro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Global)) {
            return false;
        }
        Global other = (Global) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fakturpersist.Global[ id=" + id + " ]";
    }
    
}
