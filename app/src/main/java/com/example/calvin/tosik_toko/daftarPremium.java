package com.example.calvin.tosik_toko;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class daftarPremium extends AppCompatActivity {

    Spinner spin ;
    EditText teksNamaRekening;
    EditText namaBank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_premium);
        spin = (Spinner) findViewById(R.id.spinnerBank);
        new loadBank().execute();

        Button btnmember = (Button) findViewById(R.id.daftarPremium1);

        teksNamaRekening = (EditText) findViewById(R.id.namaPemilikRekening);
        namaBank = (EditText) findViewById(R.id.pilihanBank);


        btnmember.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                Intent i = new Intent(daftarPremium.this,daftarPremium2.class);
                String namaPemilik = teksNamaRekening.getText().toString();
                String bank = spin.getSelectedItem().toString();

                i.putExtra("namaPemilik", namaPemilik);
                i.putExtra("bank", bank);
                startActivity(i);
            }

        });

    }


        class loadBank extends AsyncTask<Void, Void, ArrayList> {


            @Override
            protected ArrayList doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();




                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id_toko", "0");
                String s = requestHandler.sendPostRequest(URLs.URL_GETALLBANK,params);

               ArrayList array = new ArrayList();

                try {
                    //converting response to json object
                    JSONArray obj = new JSONArray(s);
                    JSONObject jo = null;
                    for (int i = 0; i < obj.length(); i++) {
                        jo = obj.getJSONObject(i);
                        String nama_bank = jo.getString("nama_bank");

                        array.add(nama_bank);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return array;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(ArrayList s) {
                super.onPostExecute(s);

                ArrayAdapter adapter = new ArrayAdapter(daftarPremium.this,android.R.layout.simple_spinner_item,s);

                spin.setAdapter(adapter);
            }
        }



}
