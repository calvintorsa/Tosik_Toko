package com.example.calvin.tosik_toko;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by PerumjasatirtaII on 4/19/17.
 */

public class loginActivity extends Activity {

    EditText email;
    EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btndaftar = (Button) findViewById(R.id.daftarbarumember);
        Button masuk = (Button) findViewById(R.id.masuk);

        email = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("register", 0);
        preferences.edit().clear().commit();


        btndaftar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                Intent i = new Intent(loginActivity.this,registerActivity.class);
                startActivity(i);
            }
        });

        masuk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
            userLogin();

            }
        });


    }

    private void userLogin(){
        final String inputEmail = email.getText().toString().trim();
        final String inputPass = password.getText().toString().trim();


        if (TextUtils.isEmpty(inputEmail)) {
            Toast.makeText(this, "Email kosong ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(inputPass)) {
            Toast.makeText(this, "Pass kosong ", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Mencoba Masuk..");
        progressDialog.show();

        //login menggunakan php
        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", inputEmail);
                params.put("password", inputPass);



                //returing the response

                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.dismiss();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");
                        //creating a new user object

                        String id_toko = String.valueOf(userJson.get("id_toko"));
                        String tipeMember = String.valueOf(userJson.get("tipeMember"));
                        SharedPreferences.Editor editor = getSharedPreferences("temp", MODE_PRIVATE).edit();

                        editor.putString("id",id_toko);
                        editor.putString("email",userJson.getString("email"));
                        editor.putString("tipe",tipeMember);

                        editor.apply();


                        Intent i = new Intent(loginActivity.this,Drawer2Activity.class);
                        startActivity(i);


                    } else {
                        Toast.makeText(getApplicationContext(), "Email / Password Salah", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();

    }

}
