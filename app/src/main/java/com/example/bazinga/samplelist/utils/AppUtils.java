package com.example.bazinga.samplelist.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.example.bazinga.samplelist.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class AppUtils {

    public static List<AppInfo> getLauncherApps(Context context) {
        List<AppInfo> launcherApps = new ArrayList<>();
        PackageManager mPM = context.getPackageManager();
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = mPM.queryIntentActivities(launcherIntent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            AppInfo appInfo = new AppInfo();
            appInfo.appName = activityInfo.loadLabel(mPM).toString();
            appInfo.appSummary = activityInfo.packageName;
            appInfo.appIcon = activityInfo.loadIcon(mPM);
            appInfo.appActivity = activityInfo.name;
            launcherApps.add(appInfo);
        }
        return launcherApps;
    }
}
