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
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by PerumjasatirtaII on 4/19/17.
 */

public class registerActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    EditText email;
    EditText password;
    EditText namaToko;
    EditText alamatToko;
    EditText namaPemilik;
    EditText deskripsi;
    EditText noTelp;

    Uri filePath;
    String baseImage;

    DatabaseReference databaseRef;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        Button btndaftar = (Button) findViewById(R.id.memberregister);
        Button btnpeta = (Button) findViewById(R.id.setLokasi);
        Button btnGanti = (Button) findViewById(R.id.gantiGambar);

//        // flag untuk  toko
//        databaseRef = FirebaseDatabase.getInstance().getReference("toko");
//
//        //init storage referensi
//        mStorageRef = FirebaseStorage.getInstance().getReference();

        // bounding edit teks dengan atribut
        namaToko= (EditText) findViewById(R.id.your_namatoko);
        alamatToko =(EditText) findViewById(R.id.your_alamat);
         email = (EditText) findViewById(R.id.your_email);
         password = (EditText) findViewById(R.id.your_password);
        noTelp= (EditText) findViewById(R.id.your_noTelpToko);
        namaPemilik= (EditText) findViewById(R.id.your_namapemilik);
        deskripsi = (EditText) findViewById(R.id.your_deskripsiToko);




        //mendapatkan nilai yang sudah disimpan
        SharedPreferences prefs = getSharedPreferences("register", 0);


        String prefEmail="";
        String prefPassword="";
        String prefNamaToko="";
        String prefAlamatToko="";
        String prefNamaPemilik="";
        String prefNoTelpon="";
        if (prefs!= null) {

            prefEmail = prefs.getString("email","");
            prefPassword = prefs.getString("password","");
            prefNamaToko = prefs.getString("namatoko","");
            prefAlamatToko  = prefs.getString("alamat","");

            prefNamaPemilik  = prefs.getString("namaPemilikToko","");
            prefNoTelpon= prefs.getString("noTelpon","");
        }

        email.setText(prefEmail);
        password.setText(prefPassword);
        namaToko.setText(prefNamaToko);
        alamatToko.setText(prefAlamatToko);
        namaPemilik.setText(prefNamaPemilik);
        noTelp.setText(prefNoTelpon);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();



        //seting teks tombol jika koordinat sudah diset dari activity peta
        if (getIntent().getDoubleExtra("longitude", 0) != 0 && getIntent().getDoubleExtra("latitude", 0) != 0) {


            btnpeta.setText("lokasi Koordinat anda sudah diset");

        }


        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                registerUser();


            }
        });

        btnpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = getSharedPreferences("register", MODE_PRIVATE).edit();

                editor.putString("email",email.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.putString("namatoko", namaToko.getText().toString());
                editor.putString("alamat",alamatToko.getText().toString());
                editor.putString("namaPemilikToko",namaPemilik.getText().toString());
                editor.putString("noTelpon",noTelp.getText().toString());


                editor.apply();

                Intent i = new Intent(registerActivity.this, CobaPetaActivity.class);


                startActivity(i);
            }
        });

        btnGanti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance

    }

    // berguna untuk menyimpan data ketika pindah activity
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.


        savedInstanceState.putString("email", email.getText().toString().trim());
        savedInstanceState.putString("password", password.getText().toString().trim());
        savedInstanceState.putString("namaToko", namaToko.getText().toString().trim());
        savedInstanceState.putString("namaPemilikToko", namaPemilik.getText().toString().trim());
        savedInstanceState.putString("noTelpon", noTelp.getText().toString().trim());
        savedInstanceState.putString("alamat", alamatToko.getText().toString().trim());

        super.onSaveInstanceState(savedInstanceState);
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




            ImageView imageView = (ImageView) findViewById(R.id.gambarToko);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            filePath = selectedImage;

             InputStream imageStream=null;
            try {
                 imageStream = getContentResolver().openInputStream(selectedImage);
            }catch(FileNotFoundException e){
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

    private void registerUser() {
        final String inputEmail = email.getText().toString().trim();
        final String inputPass = password.getText().toString().trim();
        final String inputNamaToko = namaToko.getText().toString().trim();
        final String inputJalanToko = alamatToko.getText().toString().trim();
        final String inputNamaPemilik = namaPemilik.getText().toString().trim();
        final String inputNoTelp = noTelp.getText().toString().trim();
        final String deskrip = deskripsi.getText().toString().trim();
        final double lg = getIntent().getDoubleExtra("longitude", 0);
        final double lt = getIntent().getDoubleExtra("latitude", 0);
        final String lng = String.valueOf(lg);
        final String lot = String.valueOf(lt);
        final String gambar = baseImage;

        final Toko user = new Toko(inputEmail,inputPass,inputNamaToko,inputJalanToko,
                lg,lt,"",inputNoTelp);

        if (TextUtils.isEmpty(inputEmail)) {
            Toast.makeText(this, "Email kosong ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(inputPass)) {
            Toast.makeText(this, "Pass kosong ", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Mendaftar..");
        progressDialog.show();


        class RegisterUser extends AsyncTask<Void, Void, String> {



            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", inputEmail);
                params.put("password", inputPass);
                params.put("nama_toko", inputNamaToko);
                params.put("nama_pemilik", inputNamaPemilik);
                params.put("alamat", inputJalanToko);
                params.put("noTelpon", inputNoTelp);
                params.put("longitude", lng);
                params.put("latitude", lot);
                params.put("deskripsi", deskrip);
                params.put("gambar", gambar);



                //returing the response

                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {




                progressDialog.dismiss();

                super.onPostExecute(s);
                //hiding the progressbar after completion


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), "Berhasil Dibuat Tunggu Maks.24 Jam", Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object


                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


        RegisterUser ru = new RegisterUser();
        ru.execute();



        //executing the async task
//        firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPass).
//                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//
//                            progressDialog.dismiss();
//
//
//
//
//
//
//
//                        }else {
//                            progressDialog.dismiss();
//
//                            Toast.makeText(registerActivity.this,"Gagal Mendaftar ",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//






    }








//    private void uploadFile() {
//        //if there is a file to upload
//        if (filePath != null) {
//            //displaying a progress dialog while upload is going on
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//
//            StorageReference riversRef = mStorageRef.child("images").child(filePath.getLastPathSegment());
//            riversRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //if the upload is successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying a success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            //if the upload is not successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying error message
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            //calculating progress percentage
//                            @SuppressWarnings("VisibleForTests")
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                            //displaying percentage in progress dialog
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
//                        }
//                    });
//        }
//    }
}
