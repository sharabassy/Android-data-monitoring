package com.example.as.trafficdemotwo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Handler mHandler = new Handler();
    private long mStart_wifi_RX = 0;
    private long mStart_wifi_TX = 0;
    private long mStart_data_RX = 0;
    private long mStart_data_TX = 0;
    private long mStartRX = 0;
    private long mStartTX = 0;

    /*EditText my_RX_data;
    EditText my_TX_data;*/



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        mStart_data_RX = TrafficStats.getMobileRxBytes();
        mStart_data_TX = TrafficStats.getMobileTxBytes();
        mStart_wifi_RX=mStartRX-TrafficStats.getMobileRxBytes();
        mStart_wifi_TX = mStartTX - TrafficStats.getMobileTxBytes();

      /*  my_RX_data=(EditText)findViewById(R.id.RX_data);
        my_TX_data=(EditText)findViewById(R.id.TX_data);*/

        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    //
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            TextView RX = (TextView)findViewById(R.id.RX);
            TextView TX = (TextView)findViewById(R.id.TX);
            TextView RX_wifi = (TextView)findViewById(R.id.RX_wifi);//add in xml
            TextView TX_wifi = (TextView)findViewById(R.id.TX_wifi);//add in xml
            TextView RX_data = (TextView)findViewById(R.id.RX_data);//add in xml
            TextView TX_data = (TextView)findViewById(R.id.TX_data);//add in xml

            long rxBytes = TrafficStats.getTotalRxBytes()- mStartRX;
            RX.setText(Long.toString(rxBytes));
            long rxBytes_wifi = TrafficStats.getTotalRxBytes()-TrafficStats.getMobileRxBytes()-mStart_wifi_RX;
            RX_wifi.setText(Long.toString(rxBytes_wifi));
            long rxBytes_data = TrafficStats.getMobileRxBytes()- mStart_data_RX;
            RX_data.setText(Long.toString(rxBytes_data));

            long txBytes = TrafficStats.getTotalTxBytes()- mStartTX;
            TX.setText(Long.toString(txBytes));
            long txBytes_wifi = TrafficStats.getTotalTxBytes()-TrafficStats.getMobileTxBytes()-mStart_wifi_TX;
            TX_wifi.setText(Long.toString(txBytes_wifi));
            long txBytes_data = TrafficStats.getMobileTxBytes()- mStart_data_TX;
            TX_data.setText(Long.toString(txBytes_data));

            try {
                FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                outputWriter.write(RX_data.getText().toString());
                outputWriter.write(TX_data.getText().toString());
                outputWriter.close();

                //display file saved message
                Toast.makeText(getBaseContext(), "File saved successfully!",
                        Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            mHandler.postDelayed(mRunnable, 1000);
        }
    };


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
}
