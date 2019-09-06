package com.example.shortenurl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.megadevs.bitlyzer.Bitlyzer;

public class MainActivity extends AppCompatActivity {
    TextView t;
    Button b1;
    Bitlyzer bitlyzer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=findViewById(R.id.t1);
        b1=findViewById(R.id.b1);

         bitlyzer = new Bitlyzer("7a5ae489c4d92b2428e24de887252972067db067","R_2b0ce081468d4edd9725254a071af4c3");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitlyzer.shorten("http://www.google.com", new Bitlyzer.BitlyzerCallback() {
                    @Override
                    public void onSuccess(String shortUrl) {
                        t.setText(shortUrl);
                        Log.d("success","onSuccess=");
                    }

                    @Override
                    public void onError(String reason) {
                        Log.d("error","onError=");
                    }
                });
            }
        });

    }
}
