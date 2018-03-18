package com.example.bazinga.samplelist.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.icu.text.AlphabeticIndex;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.model.AppListData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class ListAdapter extends BaseAdapter implements SectionIndexer {

    public static int TYPE_HEADER = 0;
    public static int TYPE_DATA = 1;

    private static final SectionInfo[] EMPTY_SECTIONS = new SectionInfo[0];

    private List<AppListData> mAppList;
    private List<AppListData> mAppAndSectionList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private AlphabeticIndex.ImmutableIndex mIndex;
    private int[] mPositionToSectionIndex;
    private SectionInfo[] mSections = EMPTY_SECTIONS;
    private ArrayList<Integer> mPositionToSectionIndexList;
    private boolean mShowSystemApps;

    public static final String TAG = "ListAdapter";

    public ListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAppAndSectionList = new ArrayList<>();
        mPositionToSectionIndexList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mAppAndSectionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppAndSectionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int mType = getItemViewType(position);

        DataViewHolder dataViewHolder = null;
        HeaderViewHolder headerViewHolder = null;
        if (convertView == null) {
            if (mType == TYPE_DATA) {
                convertView = mLayoutInflater.inflate(R.layout.app_list_item, parent, false);
                dataViewHolder = buildDataHolder(convertView);
                convertView.setTag(dataViewHolder);
            } else {
                convertView = mLayoutInflater.inflate(R.layout.app_list_header_item, parent, false);
                headerViewHolder = buildHeaderHolder(convertView);
                convertView.setTag(headerViewHolder);
            }
        } else {
            if (mType == TYPE_DATA) {
                dataViewHolder = (DataViewHolder) convertView.getTag();
            } else {
                headerViewHolder = (HeaderViewHolder) convertView.getTag();
            }
        }

        if (mType == TYPE_DATA) {
            bindViewData(dataViewHolder, position);
        } else {
            bindViewHeader(headerViewHolder, position);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        //Log.d(TAG, "getPositionForSection : sectionIndex : " + sectionIndex + " : getPositionForSection : " + mSections[sectionIndex].position);
        return mSections[sectionIndex].position;
    }

    @Override
    public int getSectionForPosition(int position) {
        //Log.d(TAG, "getSectionForPosition : position " + position + " :: " + mAppAndSectionList.get(position).appName + " :: " + mPositionToSectionIndexList.get(position));
        // If constant value is returned the fast scroller will behave weirdly like going beyond the screen
        // or moving haphazardly while scrolling the list.
        return mPositionToSectionIndexList.get(position);
    }

    @Override
    public boolean isEnabled(int position) {
        return mAppAndSectionList.get(position).getIsSectionHeader() ? false : true;
    }

    @Override
    public int getItemViewType(int position) {
        return mAppAndSectionList.get(position).getIsSectionHeader() ? TYPE_HEADER : TYPE_DATA;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class DataViewHolder {
        TextView appName, appSummary, installLocation;
        ImageView appIcon;
        CheckBox checkBox;
    }

    private class HeaderViewHolder {
        TextView headerLabel;
    }

    private DataViewHolder buildDataHolder(View convertView) {
        DataViewHolder dataViewHolder = new DataViewHolder();
        dataViewHolder.appIcon = (ImageView) convertView.findViewById(R.id.list_iv_icon);
        dataViewHolder.appName = (TextView) convertView.findViewById(R.id.list_tx_appname);
        dataViewHolder.appSummary = (TextView) convertView.findViewById(R.id.list_tx_summary);
        dataViewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.list_iv_switch);
        dataViewHolder.installLocation = (TextView) convertView.findViewById(R.id.list_install_location);

        if (dataViewHolder.checkBox != null) {
            dataViewHolder.checkBox.setVisibility(View.GONE);
        }
        return dataViewHolder;
    }

    private HeaderViewHolder buildHeaderHolder(View convertView) {
        HeaderViewHolder headerViewHolder = new HeaderViewHolder();
        headerViewHolder.headerLabel = (TextView) convertView.findViewById(R.id.app_list_header_label);

        return headerViewHolder;
    }

    private void bindViewData(DataViewHolder holder, int position) {
        AppListData appInfo = mAppAndSectionList.get(position);
        holder.appIcon.setImageDrawable(mAppAndSectionList.get(position).appIcon);
        holder.appName.setText(appInfo.appName);
        holder.appSummary.setText(appInfo.appSummary);
        holder.installLocation.setText(appInfo.installLocation);
    }

    private void bindViewHeader(HeaderViewHolder headerViewHolder, int position) {
        AppListData appInfo = mAppAndSectionList.get(position);
        headerViewHolder.headerLabel.setText(appInfo.appName);
    }

    public void setList(List<AppListData> mAppList) {
        this.mAppList = mAppList;
        buildSections();
    }

    public List<AppListData> getList() {
        return mAppList;
    }

    private void buildSections() {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            mAppAndSectionList.clear();
            LocaleList localeList = null;
            localeList = mContext.getResources().getConfiguration().getLocales();
            if (localeList.size() == 0) {
                localeList = new LocaleList(Locale.ENGLISH);
            }

            AlphabeticIndex index = new AlphabeticIndex<>(localeList.get(0));
            int localeCount = localeList.size();
            for (int i = 1; i < localeCount; i++) {
                index.addLabels(localeList.get(i));
            }

            // Ensure we always have some base English locale buckets
            index.addLabels(Locale.ENGLISH);
            mIndex = index.buildImmutableIndex();

            ArrayList<SectionInfo> sections = new ArrayList<>();
            int lastSecId = -1;
            int totalEntries = mAppList.size();
            //mPositionToSectionIndex = new int[totalEntries];

            for (int pos = 0; pos < totalEntries; pos++) {
                AppListData appListData = mAppList.get(pos);
                if (!mShowSystemApps && appListData.getIsSystemApp()) {
                    continue;
                }
                String label = appListData.appName;
                int secId = mIndex.getBucketIndex(TextUtils.isEmpty(label) ? "" : label);
//                Log.d(TAG, "[" + pos + "]sId = " + secId
//                        + ", sLabel = " + mIndex.getBucket(secId).getLabel() + ", app = " + label);
                if (secId != lastSecId) {
                    lastSecId = secId;
                    AppListData sectionData = new AppListData();
                    sectionData.appName = mIndex.getBucket(secId).getLabel();
                    sectionData.setIsSectionHeader(true);
                    mAppAndSectionList.add(sectionData);
                    sections.add(new SectionInfo(mIndex.getBucket(secId).getLabel(), mAppAndSectionList.size() - 1));
                    mPositionToSectionIndexList.add(sections.size() - 1);
                }
                mAppAndSectionList.add(appListData);
                mPositionToSectionIndexList.add(sections.size() - 1);
                //mPositionToSectionIndex[pos] = sections.size() - 1;
            }
            mSections = sections.toArray(EMPTY_SECTIONS);
//            for (int i = 0; i < mSections.length; i++) {
//                Log.d(TAG, "(" + i + ")pos = " + mSections[i].position
//                        + ", sLabel = " + mSections[i].label);
//            }
            for (int i = 0; i < mAppAndSectionList.size(); i++) {
                Log.d(TAG, "Data : " + mAppAndSectionList.get(i).appName + " - isSectionHeader - " + mAppAndSectionList.get(i).getIsSectionHeader());
            }
        }
    }

    private static class SectionInfo {
        final String label;
        final int position;

        public SectionInfo(String label, int position) {
            this.label = label;
            this.position = position;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public void openApplication(int position) {

        if (mAppAndSectionList.get(position).getIsSectionHeader())
            return;
        String pkgName = mAppAndSectionList.get(position).appSummary;
        String activityName = mAppAndSectionList.get(position).appActivity;

        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (activityName != null) {
            intent.setClassName(pkgName, activityName);
        } else {
            intent.setPackage(pkgName);
            ResolveInfo info = mContext.getPackageManager().resolveActivity(intent, 0);
            if (info != null) {
                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            }
        }

        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "ActivityNotFoundException, pkg=" + pkgName);
            e.printStackTrace();
        }
    }

    public void viewDetail(int position) {
        if (mAppAndSectionList.get(position).getIsSectionHeader())
            return;

        String pkgName = mAppAndSectionList.get(position).appSummary;
        PackageManager pm = mContext.getPackageManager();

        String installLocation = "internal only";


        try {
            PackageInfo packageInfo = pm.getPackageInfo(pkgName, 0);
            switch (packageInfo.installLocation) {
                case PackageInfo.INSTALL_LOCATION_AUTO:
                    installLocation = "auto";
                    break;
                case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
                    installLocation = "internal only";
                    break;
                case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
                    installLocation = "prefer external";
                    break;
            }
            Toast.makeText(mContext, "Install Location : " + installLocation, Toast.LENGTH_LONG).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setSystemAppsVisibility(boolean showSystemApps) {
        mShowSystemApps = showSystemApps;
    }
}
