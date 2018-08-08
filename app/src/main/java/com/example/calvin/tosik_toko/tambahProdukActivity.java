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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class tambahProdukActivity extends AppCompatActivity   {
    Spinner spinner;
    private static int RESULT_LOAD_IMAGE = 234;
    Produk produk;


    private StorageReference mStorageRef;
    private Uri filePath;
    String baseImage;
    ProgressDialog progressDialog;
    EditText namaProduk ;
    EditText hargaProduk ;
    EditText deskripsi;

    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_activity_tambahproduk);
        // new progress dialog

         progressDialog = new ProgressDialog(this);
        // flag untuk artist


        //init storage referensi
        mStorageRef = FirebaseStorage.getInstance().getReference();

        spinner = (Spinner) findViewById(R.id.spinner);
         namaProduk = (EditText) findViewById(R.id.namaTambahProduk);
         hargaProduk = (EditText) findViewById(R.id.hargaTambahProduk);
         deskripsi = (EditText) findViewById(R.id.tambahDeskripsiProduk);
         Integer imgid;


        Button gantiGbr = (Button)findViewById(R.id.gantiGambarProduk);
         Button tambahProduk = (Button)findViewById(R.id.tambahProduk);

        //array adapter buat spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        // listener untuk buka gambar
        gantiGbr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        // listener untuk tombol tambah barang
        tambahProduk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                progressDialog.setMessage("Menambah Produk..");
                progressDialog.show();

                tambahProduk();
            }
        });

    }

    // method tambah produk
    public void tambahProduk(){



           SharedPreferences prefs = getSharedPreferences("temp", MODE_PRIVATE);

        String prefIdToko="";
            if (prefs!= null) {

                prefIdToko = prefs.getString("id", "");
            }

        final String nama_produk = namaProduk.getText().toString().trim();
        final String harga_produk = hargaProduk.getText().toString().trim();
        final String deskripsi = this.deskripsi.getText().toString().trim();
        final String idToko = prefIdToko;
        final String kategori = String.valueOf(spinner.getSelectedItem());
        final String gambar = baseImage;
        if (TextUtils.isEmpty(nama_produk)) {
            Toast.makeText(this, "Nama produk kosong ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(harga_produk)) {
            Toast.makeText(this, "harga produk  kosong ", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(deskripsi)) {
            Toast.makeText(this, "deskripsi produk  kosong ", Toast.LENGTH_SHORT).show();
            return;
        }else {


            class RegisterUser extends AsyncTask<Void, Void, String> {

                private ProgressBar progressBar;

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nama_produk", nama_produk);
                    params.put("deskripsi", deskripsi);
                    params.put("harga_produk", harga_produk);
                    params.put("id_toko", idToko);
                    params.put("kategori", kategori);
                    params.put("gambar",gambar);


                    //returing the response

                    return requestHandler.sendPostRequest(URLs.URL_TAMBAHPRODUK, params);

                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();


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


                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }

            //executing the async task
            RegisterUser ru = new RegisterUser();
            ru.execute();


        }

    }

    // method untuk membuka galery ganti gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.gambarTambahProduk);
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


    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images").child(filePath.getLastPathSegment());
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            @SuppressWarnings ("VisibleForTests")
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }

        else {
            Toast.makeText(this, "Gambar Kosong ", Toast.LENGTH_SHORT).show();
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

}
