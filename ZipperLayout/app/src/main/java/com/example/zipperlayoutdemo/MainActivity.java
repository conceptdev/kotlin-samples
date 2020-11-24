package com.example.zipperlayoutdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import androidx.window.DeviceState;
import androidx.window.WindowLayoutInfo;
import androidx.window.WindowManager;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

/**
 * Demonstrate the ZipperLayout custom view. On a single screen some child elements will flow
 * off the bottom of the screen, but on two screens child elements are rendered on the
 * second screen.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}