package com.ms.fak.auto;


import com.ms.fak.entities.Komitent;

public class KomiCheck {
    public Komitent komitent;
    public boolean checked;

    public KomiCheck(Komitent komitent) {
        this.komitent = komitent;
        this.checked = false;
    }
}
