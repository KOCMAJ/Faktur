package com.ms.fak.utl;

import android.view.View;

public interface FragmentsCallbacks {
    void onMenuItemClick(View view, int position);
    void onCallForDetail(Object obj);
    boolean isDrawerOpen();
    boolean isFirstTime();
    void setFirstTime(boolean value);
}
