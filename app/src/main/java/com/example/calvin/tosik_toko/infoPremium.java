package com.example.calvin.tosik_toko;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class infoPremium extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_premium);

        Button btnTambah = (Button)findViewById(R.id.daftarPremium);
        btnTambah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(infoPremium.this, daftarPremium.class);
                startActivity(i);

            }
        });

    }


}
