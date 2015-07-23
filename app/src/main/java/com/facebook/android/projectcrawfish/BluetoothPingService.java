// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.android.projectcrawfish;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BluetoothPingService extends Service {


    public static final int ON_CYCLE_LENGTH = 5000;
    public static final int OFF_CYCLE_LENGTH = 100;

    public static final String ATTENDANCE_ID = "ATTENDANCE_ID";

    private Set<String> mDeviceAddresses;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private String mAttendanceID;

    public static Intent newIntent(Context context, Attendance attendance){
        Intent intent = new Intent(context, BluetoothPingService.class);
        intent.putExtra(ATTENDANCE_ID, attendance.getObjectId());
        return intent;
    }

    @Override
    public void onCreate() {
        mDeviceAddresses = new HashSet<>();

        mHandler = new Handler();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mAttendanceID = intent.getStringExtra(ATTENDANCE_ID);
        if (mAttendanceID == null) throw new IllegalStateException("No Attendance ID");
        mHandler.post(start);
        return START_STICKY;
    }

    Runnable start = new Runnable() {
        @Override

        public void run() {
            if (mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
            mHandler.postDelayed(stop, ON_CYCLE_LENGTH);
        }

    };

    Runnable stop = new Runnable() {
        @Override

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            if (mDeviceAddresses.size() != 0) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("MACs", mDeviceAddresses);
                params.put("attendanceID", mAttendanceID);
                try {
                    ParseCloud.callFunction("recordPings", params);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            mDeviceAddresses.clear();
            mHandler.postDelayed(start, OFF_CYCLE_LENGTH);
        }

    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i("BluetoothTest", "Found device @ " + device.getAddress());
                    mDeviceAddresses.add(device.getAddress());
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
