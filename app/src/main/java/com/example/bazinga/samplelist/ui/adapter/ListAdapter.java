package com.example.bazinga.samplelist.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.model.AppInfo;
import com.example.bazinga.samplelist.utils.AppUtils;

import java.util.List;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class ListAdapter extends BaseAdapter {

    private List<AppInfo> mAppList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public static final String TAG = "ListAdapter";

    public ListAdapter(Context context) {
        mContext = context;
        mAppList = AppUtils.getLauncherApps(context);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.app_list_item, parent, false);
            holder = buildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindViewData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        TextView appName, appSummary;
        ImageView appIcon;
        CheckBox checkBox;
    }

    private ViewHolder buildHolder(View convertView) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.list_iv_icon);
        viewHolder.appName = (TextView) convertView.findViewById(R.id.list_tx_appname);
        viewHolder.appSummary = (TextView) convertView.findViewById(R.id.list_tx_summary);
        viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.list_iv_switch);

        if (viewHolder.checkBox != null) {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        return viewHolder;
    }

    private void bindViewData(ViewHolder holder, int position) {
        AppInfo appInfo = mAppList.get(position);
        holder.appIcon.setImageDrawable(mAppList.get(position).appIcon);
        holder.appName.setText(appInfo.appName);
        holder.appSummary.setText(appInfo.appSummary);
    }

    public void openApplication(int position) {
        String pkgName = mAppList.get(position).appSummary;
        String activityName = mAppList.get(position).appActivity;

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
}
