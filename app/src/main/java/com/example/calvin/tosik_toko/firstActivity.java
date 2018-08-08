package com.example.calvin.tosik_toko;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by PerumjasatirtaII on 4/19/17.
 */

public class firstActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Button btnmember = (Button) findViewById(R.id.buttonMember);




               btnmember.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                Intent i = new Intent(firstActivity.this,loginActivity.class);
                startActivity(i);
            }
        });






//        btntoko.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View arg0){
//
//                Intent i = new Intent(firstActivity.this,loginTokoActivity.class);
//                startActivity(i);
//            }
//        });



    }

}
