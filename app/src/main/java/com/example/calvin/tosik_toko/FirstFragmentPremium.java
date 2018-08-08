package com.example.calvin.tosik_toko;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class FirstFragmentPremium extends Fragment {
    TextView tv1;
    View myView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Beranda");
        tv1 = (TextView) getActivity().findViewById(R.id.tanggalPremium);

        new loadTanggal().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_first_fragment_premium,container,false);
        return myView;

    }



    class loadTanggal extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();



            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            SharedPreferences prefs = getActivity().getSharedPreferences("temp", MODE_PRIVATE);

            String id_toko = "";
            if (prefs != null) {
                id_toko = prefs.getString("id", "0");

            }
            params.put("id_toko", id_toko);

            String s = requestHandler.sendPostRequest(URLs.URL_TANGGAL,params);




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

                JSONObject userJson = jo.getJSONObject("tanggal_habis");
                //creating a new user object

                String nomor_rekening = String.valueOf(userJson.get("tanggal_habis"));

                tv1.setText(nomor_rekening);

            } catch (JSONException e) {
                e.printStackTrace();
            }
           // progressDialog.dismiss();
        }


    }

}
