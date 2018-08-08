package com.example.calvin.tosik_toko;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by PerumjasatirtaII on 9/14/17.
 */

public class SecondFragment extends Fragment {
    View myView;
    ArrayList<Produk> arr;
    CustomListView custom;
    ListView lst;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arr = new ArrayList<Produk>();
        getActivity().setTitle("Produk");
        loadData();




        FloatingActionButton btnTambah = (FloatingActionButton) getActivity().findViewById(R.id.FAB);
        btnTambah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getActivity(), tambahProdukActivity.class);
                startActivity(i);

            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.second_layout, container, false);
        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_produk);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                custom.getFilter().filter(newText);

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);


    }

    // mengambil data produk yang dimiliki dari database


    public void loadData() {
        class RegisterUser extends AsyncTask<Void, Void, ArrayList<Produk>> {


            @Override
            protected ArrayList<Produk> doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                SharedPreferences prefs = getActivity().getSharedPreferences("temp", MODE_PRIVATE);

                String id_toko = "";
                if (prefs != null) {
                    id_toko = prefs.getString("id", "0");

                }
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id_toko", id_toko);


                //returing the response

                String s = requestHandler.sendPostRequest(URLs.URL_GETPRODUK, params);

                ArrayList<Produk> array = new ArrayList<>();
                try {
                    //converting response to json object
                    JSONArray obj = new JSONArray(s);
                    JSONObject jo = null;
                    for (int i = 0; i < obj.length(); i++) {
                        jo = obj.getJSONObject(i);
                        String id_produk = jo.getString("id_produk");
                        String nama_produk = jo.getString("nama_produk");
                        String deskripsi = jo.getString("deskripsi");
                        String harga_produk = jo.getString("harga_produk");
                        String kategori = jo.getString("kategori ");
                        Produk prd = new Produk(id_produk, nama_produk, harga_produk, deskripsi, kategori);
                        array.add(prd);
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
            protected void onPostExecute(ArrayList<Produk> s) {
                super.onPostExecute(s);
                custom = new CustomListView(getActivity(), s);
                lst = (ListView) getView().findViewById(R.id.listView);
                lst.setAdapter(custom);
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();

    }
}

//
//    @Override
//    public void processFinish(ArrayList<Produk> produk) {
//        arr = produk;
//    }


