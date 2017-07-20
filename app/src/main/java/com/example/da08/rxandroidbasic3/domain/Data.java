package com.example.da08.rxandroidbasic3.domain;

/**
 * Created by Da08 on 2017. 7. 20..
 */

public class Data {
    private RealtimeWeatherStation RealtimeWeatherStation;

    public RealtimeWeatherStation getRealtimeWeatherStation()
    {
        return RealtimeWeatherStation;
    }

    public void setRealtimeWeatherStation(RealtimeWeatherStation RealtimeWeatherStation) {
        this.RealtimeWeatherStation = RealtimeWeatherStation;
    }

    @Override
    public String toString() {
        return "ClassPojo [RealtimeWeatherStation = " + RealtimeWeatherStation + "]";
    }
}
