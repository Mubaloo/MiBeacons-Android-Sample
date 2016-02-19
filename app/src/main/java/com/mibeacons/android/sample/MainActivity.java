package com.mibeacons.android.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView content;
    private View progress;
    private TextView message;
    private Switch scanSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (RecyclerView) findViewById(R.id.content);
        progress = findViewById(R.id.progress);
        message = (TextView) findViewById(R.id.message);

        setSupportActionBar(toolbar);

        showProgress(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);

        scanSwitch = (Switch) menu.findItem(R.id.scan).getActionView().findViewById(R.id.scan_switch);
        scanSwitch.setEnabled(false);
        scanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        return super.onCreateOptionsMenu(menu);
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
}
