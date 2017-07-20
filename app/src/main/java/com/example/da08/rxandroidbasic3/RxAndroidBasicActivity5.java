package com.example.da08.rxandroidbasic3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class RxAndroidBasicActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android_basic);
    }

    /*
    구독 시점부터 발행한 데이터 읽을 수 있음
     */
    PublishSubject<String> publishSubject = PublishSubject.create();  // 발행 준비
    public void doPublish(View view){
        new Thread(){
            public void run(){
                for(int i = 0; i<10; i++) {
                    publishSubject.onNext("a" + i);  // 발행
                    Log.i("publish", "a" + i);
                    try {
                        Thread.sleep(1000);   // 1초씩 텀을두고 발행
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getPublish (View view){
        //구독
        publishSubject.subscribe(
                item -> Log.i("publish","item="+item)
        );
    }

    /*
    가장 마지막에 발행한 데이터부터 읽을 수 있음 (바로 이전에 읽은거까지)
     */
    BehaviorSubject<String> behaviorSubject = BehaviorSubject.create(); // 발행 준비
    public void doBehavior(View view){
        new Thread(){
            public void run(){
                for(int i = 0; i<10; i++) {
                    behaviorSubject.onNext("b" + i);  // 발행
                    Log.i("behavior", "b" + i);
                    try {
                        Thread.sleep(1000);   // 1초씩 텀을두고 발행
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getBehavior(View view){
        //구독
        behaviorSubject.subscribe(
                item -> Log.i("behavior","item="+item)
        );
    }

    /*
    처음 발행한 데이터부터 읽을 수 있음
     */
    ReplaySubject<String> replaySubject = ReplaySubject.create(); // 발행 준비
    public void doReplay(View view){
        new Thread(){
            public void run(){
                for(int i = 0; i<10; i++) {
                    replaySubject.onNext("c" + i);  // 발행
                    Log.i("replay", "c" + i);
                    try {
                        Thread.sleep(1000);   // 1초씩 텀을두고 발행
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getReplay(View view){
        //구독
        replaySubject.subscribe(
                item -> Log.i("replay","item="+item)
        );
    }

    /*
    완료된 시점에서 마지막 데이터만 읽을 수 있음
     */
    AsyncSubject<String> asyncSubject = AsyncSubject.create(); // 발행 준비
    public void doAsync(View view){
        new Thread(){
            public void run() {
                for (int i = 0; i < 10; i++) {
                    asyncSubject.onNext("d" + i);  // 발행
                    Log.i("async", "d" + i);
                    try {
                        Thread.sleep(1000);   // 1초씩 텀을두고 발행
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                asyncSubject.onComplete();
            }
        }.start();
    }

    public void getAsync(View view){
        //구독
        asyncSubject.subscribe(
                item -> Log.i("async","item="+item)
        );
    }

}
