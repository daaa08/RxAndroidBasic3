package com.example.da08.rxandroidbasic3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.da08.rxandroidbasic3.domain.Data;
import com.example.da08.rxandroidbasic3.domain.Row;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RxBasic8Activity extends AppCompatActivity {

    // http://openAPI.seoul.go.kr:8088/(인증키)/xml/RealtimeWeatherStation/1/5/중구
    // 48557578576461613130315141764666

    public static final String SERVER = "http://openAPI.seoul.go.kr:8088/";
    public static final String SERVER_KEY = "48557578576461613130315141764666";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_basic8);

        // 1 retofit 생성
        Retrofit client = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 2 서비스 생성
        Iweather service = client.create(Iweather.class);

        // 3 Observable 생성 - RxJava2CallAdapterFactory을 사용했기때문에 Observable로 생성 가능
        Observable<Data> observable = service.getData(SERVER_KEY, 1, 10, "서초");

        // 4 발행시작
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                // 구독시작
                .subscribe(
                        data -> {
                            Row rows[] = data.getRealtimeWeatherStation().getRow();
                            for (Row row : rows) {
                                Log.i("Weather", "지역명 :" + row.getSTN_NM());
                                Log.i("Weather", "온도 :" + row.getSAWS_TA_AVG() + "도");
                                Log.i("Weather", "습도 :" + row.getSAWS_HD() + "%");
                            }
                        }
                );
    }
}

interface Iweather {
    //    @GET('인증키/xml/서비스명/시작인덱스/가져올개수/구이름')
    @GET("{key}/json/RealtimeWeatherStation/{start}/{count}/{name}")
    Observable<Data> getData(@Path("key") String server_key     // Path안의 이름으로 래핑이 됨 (Path는 주소 안에 들어감)
            , @Path("start") int begin_index
            , @Path("count") int offset
            , @Path("name") String gu);
}

