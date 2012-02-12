package com.net.rmopenmenu;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An extension of {@link com.example.android.actionbarcompat.ActionBarHelper} that provides Android
 * 4.0-specific functionality for IceCreamSandwich devices. It thus requires API level 14.
 */
public class ActionBarHelperICS extends ActionBarHelperHoneycomb {
    protected ActionBarHelperICS(Activity activity) {
        super(activity);
    }

    @Override
    protected Context getActionBarThemedContext() {
        return mActivity.getActionBar().getThemedContext();
    }
}