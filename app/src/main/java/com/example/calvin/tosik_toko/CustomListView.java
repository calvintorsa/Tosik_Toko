package com.example.calvin.tosik_toko;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PerumjasatirtaII on 9/16/17.
 */

public class CustomListView extends ArrayAdapter<Produk> implements Filterable {

    private ArrayList<Produk> arrayProduk;
    private CustomFilter cf;
    private ArrayList<Produk> filterProduk;


    private Context contxt;

    public CustomListView(@NonNull Context context,ArrayList<Produk> arrayProd) {
       // super(context, R.layout.produk_listview_layoutproduk,produkname);
        super(context, R.layout.produk_listview_layoutproduk);
        arrayProduk = arrayProd;


        // mengkopi array produk ke filter
        this.filterProduk = arrayProduk;

        this.contxt = context;


    }
    @Override
    public int getCount() {
        return arrayProduk.size();
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r =convertView;
        ViewHolder viewHolder=null;

        if(r==null){
            LayoutInflater layoutInflater = LayoutInflater.from(contxt);
            r = layoutInflater.inflate(R.layout.produk_listview_layoutproduk,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) r.getTag();

        }


        // setting adapter ke view
           // viewHolder.ivw.setImageResource(arrayProduk.get(position).getImg());
            viewHolder.tvw1.setText(arrayProduk.get(position).getProdukname());
            viewHolder.tvw2.setText(arrayProduk.get(position).getHarga());


        return r;
    }

    class ViewHolder{
        TextView tvw1;
        TextView tvw2;
        ImageView ivw;

        ViewHolder(View v)
        {
            tvw1 = (TextView) v.findViewById(R.id.teksNamaProduk);
            tvw2=(TextView) v.findViewById(R.id.teksHargaProduk);
            ivw = (ImageView) v.findViewById(R.id.gambarProduk);

        }

    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(cf==null){
            cf = new CustomFilter();

        }
        return cf;
    }

    class CustomFilter extends Filter{


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint!=null&& constraint.length()>0) {
                constraint = constraint.toString().toLowerCase();

                ArrayList<Produk> filters = new ArrayList<Produk>();
                for (int i = 0; i < filterProduk.size(); i++) {

                    if (filterProduk.get(i).getProdukname().toLowerCase().contains(constraint)) {
                        Produk p = new Produk(filterProduk.get(i).getId(),filterProduk.get(i).getProdukname(), filterProduk.get(i).getHarga(),filterProduk.get(i).getDeskripsi(),filterProduk.get(i).getKategori());

                        filters.add(p);
                    }

                }
                results.count =filters.size();
                results.values = filters;
            }else{
                results.count = filterProduk.size();
                results.values = filterProduk;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            arrayProduk = (ArrayList<Produk>) results.values;

            notifyDataSetChanged();

        }
    }


}
