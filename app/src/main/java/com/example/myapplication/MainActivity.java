package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String suhu_public;
    String humidity_public;

    Handler handler = new Handler();

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.i(data.toString(),"Sensor");
                    String suhu;
                    String humidity;
                    try {
                        suhu = data.getString("suhu");
                        humidity = data.getString("humidity");
                        suhu_public = suhu;
                        humidity_public = humidity;
                        Log.e(suhu,"suhu");
                        Log.e(humidity,"humidity");
                        TextView mSuhu = findViewById(R.id.suhu_txt);
                        TextView mHumidity = findViewById(R.id.humidity_txt);
                        suhu = "Suhu:" + suhu_public;
                        humidity = "Humidity: " + humidity_public;
                        mSuhu.setText(suhu);
                        mHumidity.setText(humidity);

                    } catch (JSONException e) {
                        return ;
                    }
                }
            });
        }
    };

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://167.71.214.196:7500/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mSocket.on("iot_push", onNewMessage);
        mSocket.connect();
    }

}
