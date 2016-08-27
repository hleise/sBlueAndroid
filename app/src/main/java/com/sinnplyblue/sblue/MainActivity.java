package com.sinnplyblue.sblue;

/**
 * Created by dani on 8/5/16.
 */
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView txtView;
    private NotificationReceiver nReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        NLService nlservice = new NLService();
        filter.addAction("com.sinnplyblue.sblue.NOTIFICATION_LISTENER");
        filter.addAction("com.sinnplyblue.sblue.CLEAR_ALL");
        filter.addAction("com.sinnplyblue.sblue.LIST_ALL");
        registerReceiver(nReceiver,filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }


    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("My Notification");
            ncomp.setContentText("Notification Listener Service");
            ncomp.setTicker("Sinnply Manual Notification");
            ncomp.setSmallIcon(R.drawable.ic_launcher);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnClearNotify){
            Intent i = new Intent("com.sinnplyblue.sblue.CLEAR_ALL");
            i.putExtra("command","clearall");
            sendBroadcast(i);
            //NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //nManager.cancelAll();

        }
        else if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent("com.sinnplyblue.sblue.LIST_ALL");
            i.putExtra("command","list");
            sendBroadcast(i);
        }
        // Opens Scan Devices and Bluetooth
        else if(v.getId() == R.id.btnScanDevices){
            Intent intent = new Intent(this, DeviceScanActivity.class);
            startActivity(intent);
        }
    }

    class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
            txtView.setText(temp);
        }
    }
}