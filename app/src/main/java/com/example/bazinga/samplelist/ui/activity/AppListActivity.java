package com.example.bazinga.samplelist.ui.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.model.AppListData;
import com.example.bazinga.samplelist.ui.adapter.ListAdapter;
import com.example.bazinga.samplelist.utils.AppUtils;

import java.util.List;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class AppListActivity extends ListActivity {
    private ListAdapter mAppListAdapter;
    private ListView mListView;
    private ProgressDialog mProgressDialog;
    private List<AppListData> mAppList;
    private AppListTask mAppTask;
    private LayoutInflater mLayoutInflater;
    private View mHeaderView;
    private TextView mHeaderViewTitle;
    private TextView mHeaderViewSummary;
    private Switch mHideSystemAppSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_activity_layout);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderView = findViewById(R.id.custom_preference_layout_content_id);
        mHeaderViewTitle = (TextView) mHeaderView.findViewById(R.id.custom_preference_title);
        mHeaderViewSummary = (TextView) mHeaderView.findViewById(R.id.custom_preference_status);
        mHideSystemAppSwitch = (Switch) mHeaderView.findViewById(R.id.custom_preference_switch);

        mHeaderViewTitle.setText("Launcher Apps");
        if (mHideSystemAppSwitch.isChecked()) {
            mHeaderViewSummary.setText("System apps are hidden.");
        } else {
            mHeaderViewSummary.setText("System apps are not hidden.");
        }


        mListView = getListView();
        mAppListAdapter = new ListAdapter(this);
        mListView.setFastScrollEnabled(true);
        mListView.setDivider(null);
        loadAppList(false);
        registerForContextMenu(mListView);

        mHideSystemAppSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loadAppList(isChecked);
                if (isChecked) {
                    mHeaderViewSummary.setText("System apps are hidden.");
                } else {
                    mHeaderViewSummary.setText("System apps are not hidden.");
                }
            }
        });
    }

    private void loadAppList(boolean hideSystemApp) {
        mAppTask = new AppListTask();
        mAppTask.execute(hideSystemApp);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 50);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int headerViewCount = l.getHeaderViewsCount();
        int footerViewCount = l.getFooterViewsCount();
        int listCount = l.getCount();
        boolean isValidPos = position >= headerViewCount && (position < (listCount + 1) - footerViewCount) ? true : false;
        if (mAppListAdapter.getItemViewType(position) == 1) {
            v.showContextMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Context Menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menu.setHeaderTitle("Select Action");
        menuInflater.inflate(R.menu.menu_context_list_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_open:
                mAppListAdapter.openApplication(itemInfo.position);
                return true;
            case R.id.action_view_details:
                mAppListAdapter.viewDetail(itemInfo.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    class AppListTask extends AsyncTask<Boolean, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Void doInBackground(Boolean... bool) {
            boolean hideSystemApps = bool[0].booleanValue();
            Log.d("BUZZ", "doInBackground : hideSystemApps : " + hideSystemApps);
            mAppList = AppUtils.getLauncherApps(AppListActivity.this, hideSystemApps);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAppListAdapter.setList(mAppList);
            setListAdapter(mAppListAdapter);
            mAppListAdapter.notifyDataSetChanged();
            hideProgressDialog();
        }
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(AppListActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
