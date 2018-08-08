package com.example.calvin.tosik_toko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class daftarPremium2 extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    TextView namaBank;
    TextView namaPemilik;
    TextView nomorRekening;
    String ekstraNamaBank;

    Uri filePath;
    String baseImage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daftar_premium2);
        namaPemilik = (TextView) findViewById(R.id.premium2_namaPemilik);
        namaBank = (TextView) findViewById(R.id.premium2_namaBank);
        nomorRekening = (TextView) findViewById(R.id.premium2_nomorRek);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ekstraNamaBank= null;
            } else {
                ekstraNamaBank= extras.getString("bank");
            }
        } else {
            ekstraNamaBank= (String) savedInstanceState.getSerializable("bank");
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        new loadDataBank().execute();


        Button btnUpload = (Button) findViewById(R.id.buttonUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Button btnKonfirmasi = (Button) findViewById(R.id.buttonKonfirmasi);
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                progressDialog.setMessage("Loading..");
                progressDialog.show();
                new uploadBukti().execute();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};


            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            ImageView imageView = (ImageView) findViewById(R.id.gambarBukti);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            filePath = selectedImage;

            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap bitmapImg = BitmapFactory.decodeStream(imageStream);

            String encodedImage = encodeImage(bitmapImg);
            baseImage = encodedImage;


        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    class loadDataBank extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();



            //creating request parameters
            HashMap<String, String> params = new HashMap<>();

            params.put("nama_bank", ekstraNamaBank);

            String s = requestHandler.sendPostRequest(URLs.URL_GETBANK,params);




            return s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //converting response to json object
                JSONObject jo = new JSONObject(s);

                JSONObject userJson = jo.getJSONObject("bank");
                //creating a new user object

                String nomor_rekening = String.valueOf(userJson.get("nomor_rekening"));
                String nama_pemilik = String.valueOf(userJson.get("nama_pemilik"));
                String nama_bank = String.valueOf(userJson.get("nama_bank"));

                namaPemilik.setText(nama_pemilik);
                namaBank.setText(nama_bank);
                nomorRekening.setText(nomor_rekening);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }


    }


    class uploadBukti extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();
            progressDialog.show();


            //creating request parameters

            SharedPreferences prefs = getSharedPreferences("temp", MODE_PRIVATE);

            String id_toko = "";
            if (prefs != null) {
                id_toko = prefs.getString("id", "0");

            }
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_toko", id_toko);
            params.put("buktiPembayaran", baseImage);
            String s = requestHandler.sendPostRequest(URLs.URL_UPGRADEPREMIUM,params);




            return s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Tunggu Konfirmasi dalam 1x24 Jam", Toast.LENGTH_SHORT).show();

        }


    }

}
