package com.example.calvin.tosik_toko;

/**
 * Created by PerumjasatirtaII on 9/16/17.
 */

public class Produk {
    private String id;
    private String produkname;
    private String Harga;
    private String deskripsi;
    private String kategori;

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Produk(String id, String produkname, String harga, String deskripsi,String kategori) {
        this.id = id;
        this.produkname = produkname;
        Harga = harga;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getProdukname() {
        return produkname;
    }

    public void setProdukname(String produkname) {
        this.produkname = produkname;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

//    public int getImg() {
//        return img;
//    }
//
//    public void setImg(int img) {
//        this.img = img;
//    }
}
