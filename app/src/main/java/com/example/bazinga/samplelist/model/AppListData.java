package com.example.bazinga.samplelist.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class AppListData {
    public String appName;
    public String appSummary;
    public Drawable appIcon;
    public String appActivity;
    private boolean isSectionHeader;
    private boolean isSystemApp;
    public String installLocation;

    public void setIsSectionHeader(boolean isSectionHeader) {
        this.isSectionHeader = isSectionHeader;
    }

    public boolean getIsSectionHeader() {
        return isSectionHeader;
    }

    public boolean getIsSystemApp() {
        return isSystemApp;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }
}
