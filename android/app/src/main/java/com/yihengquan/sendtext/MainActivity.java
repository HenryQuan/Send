package com.yihengquan.sendtext;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private final int port = 9587;
    private WebServer server = new WebServer(port, "");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            // Start server
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Focus inputBox
        TextView inputBox = findViewById(R.id.inputText);
        inputBox.requestFocus();

        // Check if wifi is connected
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected()) {
            // Get device's IP address
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = intToInetAddress(wm.getConnectionInfo().getIpAddress()).toString();
            ip = ip.replace("/", "") + ':' + port;

            TextView ipLabel = findViewById(R.id.idLabel);
            ipLabel.setText(ip);
        } else {
            // Hotspot might work but I don't know
            new AlertDialog.Builder(this)
            .setTitle("WiFi is not connected")
            .setMessage("Please connect to the same network first")
            .setCancelable(false)
            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                finish();
                }
            })
            .show();
        }
    }

    public void sendText(View v) {
        final EditText input = findViewById(R.id.inputText);
        String msg = input.getText().toString();
        System.out.println(msg);
        this.server.setMessage(msg);
    }

    /**
     * Convert ip address to a readable string
     * From https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code/54417079#54417079
     * @param hostAddress
     * @return
     */
    private InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = { (byte)(0xff & hostAddress),
                (byte)(0xff & (hostAddress >> 8)),
                (byte)(0xff & (hostAddress >> 16)),
                (byte)(0xff & (hostAddress >> 24)) };
        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.server.stop();
    }

    public void checkLocalhost(MenuItem item) {
        Intent localhost = new Intent(Intent.ACTION_VIEW, Uri.parse("http://127.0.0.1:" + port));
        startActivity(localhost);
    }
}
