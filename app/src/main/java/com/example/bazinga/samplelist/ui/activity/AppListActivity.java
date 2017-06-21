package com.example.bazinga.samplelist.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.ui.adapter.ListAdapter;

/**
 * Created by Bazinga on 5/1/2017.
 */

public class AppListActivity extends ListActivity {
    private ListAdapter mAppListAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_activity_layout);
        mListView = getListView();
        mAppListAdapter = new ListAdapter(this);
        setListAdapter(mAppListAdapter);

        registerForContextMenu(mListView);
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
        super.onListItemClick(l, v, position, id);
//        mAppListAdapter.openApplication(position);
        v.showContextMenu();
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
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
