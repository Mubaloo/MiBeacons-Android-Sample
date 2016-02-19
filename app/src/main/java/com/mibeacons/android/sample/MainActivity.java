package com.mibeacons.android.sample;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mibeacons.sdk.AppCallback;
import com.mibeacons.sdk.model.Campaign;
import com.mibeacons.sdk.model.MiBeacon;
import com.mibeacons.sdk.model.Trigger;
import com.mibeacons.sdk.model.WebConfiguration;
import com.mibeacons.sdk.service.SdkBeaconService;
import com.mibeacons.sdk.service.manager.BeaconManagerCallback;
import com.mibeacons.sdk.service.manager.BeaconServiceManager;
import com.mibeacons.sdk.service.manager.MiBeaconServiceManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AppCallback, BeaconManagerCallback, SdkBeaconService.BeaconListCallback {
    private static final int REQUEST_CODE_PERMISSION = 100;

    private BeaconServiceManager beaconServiceManager;

    private RecyclerView content;
    private View progress;
    private TextView message;
    private Switch scanSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (RecyclerView) findViewById(R.id.content);
        progress = findViewById(R.id.progress);
        message = (TextView) findViewById(R.id.message);

        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initiateBeaconServiceManager();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    protected void onDestroy() {
        beaconServiceManager.stop();
        beaconServiceManager.disconnect();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);

        scanSwitch = (Switch) menu.findItem(R.id.scan).getActionView().findViewById(R.id.scan_switch);
        scanSwitch.setEnabled(false);
        scanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    beaconServiceManager.start();
                    beaconServiceManager.getService().getAllBeaconsInRangeOnNextScan(MainActivity.this);
                    if (content.getVisibility() == View.GONE) {
                        showProgress(true);
                    }
                } else {
                    beaconServiceManager.stop();
                    if (content.getVisibility() == View.GONE) {
                        showMessage(getString(R.string.scan_start));
                    }
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateBeaconServiceManager();
            } else {
                showMessage(getString(R.string.error_permissions));
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initiateBeaconServiceManager() {
        showProgress(true);
        beaconServiceManager = new MiBeaconServiceManager.Builder(this)
                .webConfiguration(new WebConfiguration.Builder()
                        .url(BuildConfig.API_URL)
                        .apiKey(BuildConfig.API_KEY)
                        .mode(WebConfiguration.Mode.BEACONS_ONLY)
                        .build())
                .showNotifications(R.drawable.ic_location, MainActivity.class)
                .appCallback(this)
                .managerCallback(this)
                .build();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            beaconServiceManager.connect();
        } else {
            showMessage(getString(R.string.error_bluetooth));
        }
    }

    private void showProgress(boolean isLoading) {
        content.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        message.setVisibility(View.GONE);
    }

    private void showMessage(String messageText) {
        content.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
        message.setText(messageText);
    }

    @Override
    public void onEnterBeacon(MiBeacon miBeacon) {

    }

    @Override
    public void onNearestBeaconProximityChange(MiBeacon miBeacon) {

    }

    @Override
    public void onTriggerFired(@NonNull MiBeacon miBeacon, @NonNull Campaign campaign, @NonNull Trigger trigger, String s) {

    }

    @Override
    public void onExitBeacon(MiBeacon miBeacon) {

    }

    @Override
    public void onBeaconUpdateComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scanSwitch.setEnabled(true);
                showMessage(getString(R.string.scan_start));
            }
        });
    }

    @Override
    public void onError(@NonNull final Exception e, Exception e1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showMessage(e.getMessage());
            }
        });
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onScanComplete(List<MiBeacon> list, List<MiBeacon> list1) {
        beaconServiceManager.getService().getAllBeaconsInRangeOnNextScan(this);
    }
}
