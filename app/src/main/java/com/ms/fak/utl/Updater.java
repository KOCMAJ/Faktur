package com.ms.fak.utl;

import android.app.Activity;
import android.content.Context;

public interface Updater {
    public boolean isValid(Context context);
    public boolean isModified();
    public void saveData(Context context);
    public void updateData();
    public void gasiDekor(Activity activity);
}
