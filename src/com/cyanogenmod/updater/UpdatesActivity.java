/*
 * Copyright (C) 2017 The LineageOS Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */
package com.cyanogenmod.updater;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cyanogenmod.updater.utils.Utils;

public class UpdatesActivity extends AppCompatActivity {

    private UpdatesSettings mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_updater);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mHeaderCm = (TextView) findViewById(R.id.header_version);
        TextView mHeaderInfo = (TextView) findViewById(R.id.header_info);

        mSettingsFragment = new UpdatesSettings();

        String[] mInstalled = Utils.getInstalledVersion().split("-");

        mHeaderCm.setText(String.format(getString(R.string.header_os), mInstalled[0]));
        String mApi = String.valueOf(Utils.getInstalledApiLevel());
        switch (mApi) {
            case "25":
                mApi = "7.1.1";
                break;
            default:
                mApi = "API " + mApi;
        }
        mHeaderInfo.setText(String.format(getString(R.string.header_summary),
                Utils.getInstalledBuildDateLocalized(this, mInstalled[1]), mInstalled[2], mApi));

        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_logo);
        setSupportActionBar(mToolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mSettingsFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSettingsFragment != null) {
            mSettingsFragment.checkForUpdates();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if we need to refresh the screen to show new updates
        if (intent.getBooleanExtra(UpdatesSettings.EXTRA_UPDATE_LIST_UPDATED, false)) {
            mSettingsFragment.updateLayout();
        }

        mSettingsFragment.checkForDownloadCompleted(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu mMenu) {
        getMenuInflater().inflate(R.menu.menu, mMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mItem) {
        switch (mItem.getItemId()) {
            case R.id.menu_refresh:
                if (mSettingsFragment != null) {
                    mSettingsFragment.checkForUpdates();
                }
                break;
        }

        return super.onOptionsItemSelected(mItem);
    }

}
