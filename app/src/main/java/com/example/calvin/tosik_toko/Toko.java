package com.example.calvin.tosik_toko;

/**
 * Created by PerumjasatirtaII on 10/1/17.
 */

public class Toko {

    private String username;
    private String password;
    private String namaToko;
    private String alamatToko;
    private double latitude;
    private double longitude;

    public Toko(String username, String password, String namaToko) {
        this.username = username;
        this.password = password;
        this.namaToko = namaToko;
    }

    private String dirGambar;
    private String nomorTelp;

    public Toko(String username, String password, String namaToko, String alamatToko, double latitude, double longitude, String dirGambar, String nomorTelp) {
        this.username = username;
        this.password = password;
        this.namaToko = namaToko;
        this.alamatToko = alamatToko;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dirGambar = dirGambar;
        this.nomorTelp = nomorTelp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getAlamatToko() {
        return alamatToko;
    }

    public void setAlamatToko(String alamatToko) {
        this.alamatToko = alamatToko;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDirGambar() {
        return dirGambar;
    }

    public void setDirGambar(String dirGambar) {
        this.dirGambar = dirGambar;
    }

    public String getNomorTelp() {
        return nomorTelp;
    }

    public void setNomorTelp(String nomorTelp) {
        this.nomorTelp = nomorTelp;
    }
}
