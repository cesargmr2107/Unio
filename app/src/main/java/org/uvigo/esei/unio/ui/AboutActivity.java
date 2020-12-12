package org.uvigo.esei.unio.ui;

import android.app.Activity;
import android.os.Bundle;

import org.uvigo.esei.unio.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("About");
    }
}
