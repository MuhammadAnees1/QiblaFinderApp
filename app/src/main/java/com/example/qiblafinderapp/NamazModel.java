package com.example.qiblafinderapp;
public class NamazModel {

    private String fajr;
    private String sunrise;
    private String dhuhr;
    private String asr;
    private String sunset;
    private String maghrib;
    private String isha;
    private String imsak;
    private String midnight;
    private String firstThird;
    private String lastThird;
    private String date;

    public NamazModel(String fajr, String sunrise, String dhuhr, String asr, String sunset, String maghrib, String isha, String imsak, String midnight, String firstThird, String lastThird, String date) {
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.sunset = sunset;
        this.maghrib = maghrib;
        this.isha = isha;
        this.imsak = imsak;
        this.midnight = midnight;
        this.firstThird = firstThird;
        this.lastThird = lastThird;
        this.date = date;
    }

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(String dhuhr) {
        this.dhuhr = dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public String getImsak() {
        return imsak;
    }

    public void setImsak(String imsak) {
        this.imsak = imsak;
    }

    public String getMidnight() {
        return midnight;
    }

    public void setMidnight(String midnight) {
        this.midnight = midnight;
    }

    public String getFirstThird() {
        return firstThird;
    }

    public void setFirstThird(String firstThird) {
        this.firstThird = firstThird;
    }

    public String getLastThird() {
        return lastThird;
    }

    public void setLastThird(String lastThird) {
        this.lastThird = lastThird;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

