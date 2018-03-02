package com.example.bazinga.samplelist.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;

import com.example.bazinga.samplelist.model.AppListData;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class AppUtils {

    public static List<AppListData> getLauncherApps(Context context, boolean hideSystemApps) {
        List<AppListData> launcherApps = new ArrayList<>();
        PackageManager mPM = context.getPackageManager();
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = mPM.queryIntentActivities(launcherIntent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = mPM.getApplicationInfo(activityInfo.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            AppListData appInfo = new AppListData();
            appInfo.appName = activityInfo.loadLabel(mPM).toString();
            appInfo.appSummary = activityInfo.packageName;
            appInfo.appIcon = activityInfo.loadIcon(mPM);
            appInfo.appActivity = activityInfo.name;
            // default install location
            appInfo.installLocation = "INSTALL_LOCATION_INTERNAL_ONLY";

            PackageInfo packageInfo = getPackageInfo(context, activityInfo.packageName);

            if (null != packageInfo) {
                switch (packageInfo.installLocation) {
                    case PackageInfo.INSTALL_LOCATION_AUTO:
                        appInfo.installLocation = "INSTALL_LOCATION_AUTO";
                        break;
                    case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
                        appInfo.installLocation = "INSTALL_LOCATION_INTERNAL_ONLY";
                        break;
                    case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
                        appInfo.installLocation = "INSTALL_LOCATION_PREFER_EXTERNAL";
                        break;

                }
            }
            if (hideSystemApps && applicationInfo != null) {
                if ((ApplicationInfo.FLAG_SYSTEM & applicationInfo.flags) == 0) {
                    // add only if not system app.
                    launcherApps.add(appInfo);
                }
            } else {
                launcherApps.add(appInfo);
            }

        }
        sortList(launcherApps);
        return launcherApps;
    }

    private static void sortList(List<AppListData> mAppList) {
        final Collator collator = Collator.getInstance(Locale.getDefault());
        Comparator<AppListData> comparator = new Comparator<AppListData>() {
            @Override
            public int compare(AppListData lhs, AppListData rhs) {
                int result = 0;
                int ret;
                ret = collator.compare(lhs.appName, rhs.appName);
                if (ret != 0) {
                    result = ret > 0 ? 1 : -1;
                }
                return result;
            }
        };
        Collections.sort(mAppList, comparator);
    }

    public static void setComponentState(Context context, String className, boolean state) {
        PackageManager pm = context.getPackageManager();
        String pkg = context.getPackageName();
        ComponentName componentName = ComponentName.createRelative(pkg, className);
        pm.setComponentEnabledSetting(componentName,
                state ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    public static boolean isComponentEnabled(Context context, String pkgName, String clsName) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = ComponentName.createRelative(pkgName, clsName);
        int componentEnabledSetting = pm.getComponentEnabledSetting(componentName);

        switch (componentEnabledSetting) {
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
                return false;
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
                return true;
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
            default:
                // We need to get the application info to get the component's default state
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES
                            | PackageManager.GET_RECEIVERS
                            | PackageManager.GET_SERVICES
                            | PackageManager.GET_PROVIDERS
                            | PackageManager.MATCH_DISABLED_COMPONENTS);


                    List<ComponentInfo> components = new ArrayList<>();
                    if (packageInfo.activities != null)
                        Collections.addAll(components, packageInfo.activities);
                    if (packageInfo.services != null)
                        Collections.addAll(components, packageInfo.services);
                    if (packageInfo.providers != null)
                        Collections.addAll(components, packageInfo.providers);

                    for (ComponentInfo componentInfo : components) {
                        if (componentInfo.name.equals(clsName)) {
                            return componentInfo.enabled;
                        }
                    }

                    // the component is not declared in the AndroidManifest
                    return false;
                } catch (PackageManager.NameNotFoundException e) {
                    // the package isn't installed on the device
                    return false;
                }
        }
    }

    public static boolean isRTL(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        PackageInfo packageInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo;
    }
}
