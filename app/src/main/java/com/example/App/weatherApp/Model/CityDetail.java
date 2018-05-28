package com.example.App.weatherApp.Model;

import org.json.JSONObject;

import java.io.Serializable;


public class CityDetail implements Serializable {
    public String getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }

    public String getWindSp() {
        return windSp;
    }

    public void setWindSp(String windSp) {
        this.windSp = windSp;
    }

    String name, currentTemp, minTemp, maxTemp, humidity, preasure, windDeg, windSp, weatherDes;

    public CityDetail(String name, String currentTemp, String minTemp, String maxTemp, String preasure, String windSp, String windDeg, String humidity, String weatherDes) {
        this.name = name;
        this.currentTemp = currentTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.preasure = preasure;
        this.windDeg = windDeg;
        this.windSp = windSp;
        this.weatherDes = weatherDes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPreasure() {
        return preasure;
    }

    public void setPreasure(String preasure) {
        this.preasure = preasure;
    }


    public String getWeatherDes() {
        return weatherDes;
    }

    public void setWeatherDes(String weatherDes) {
        this.weatherDes = weatherDes;
    }
}
