package com.example.da08.rxandroidbasic3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private TextView txt;
    private ListView listView;
    List<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    Observable<String> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

//        Calendar calendar = Calendar.getInstance();
//        calendar.get(Calendar.DAY_OF_WEEK);   // 현재 요일 가져오기

        // 캘린더에서 1월~12월 텍스트를 추출
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        observable = Observable.create(emitter ->{
            for(String month : months){
                emitter.onNext(month);
                Thread.sleep(1000);
            }
            emitter.onComplete();
        });

//        observable = Observable.just("월","화","수","목","금","토","일");
    }

    public void doAsync(View v) {
        observable
                .subscribeOn(Schedulers.io())               // 옵저버블의 스레드를 지정
                .observeOn(AndroidSchedulers.mainThread())  // 옵저버의 스레드를 지정
                .subscribe(
                str -> {
                    data.add(str);
                    adapter.notifyDataSetChanged();  // 매번 데이터를 가져올 때 마다 출력하기 위해 설정
                },
                error -> Log.e("Error",error.getMessage()),     // onError
                ()  -> {                                             // onComplete
                    data.add("complete");
                    adapter.notifyDataSetChanged();
                }
        );
    }

    private void initView() {
        txt = (TextView) findViewById(R.id.txt);
        listView = (ListView) findViewById(R.id.listView);
    }
}
