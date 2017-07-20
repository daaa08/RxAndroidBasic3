package com.example.da08.rxandroidbasic3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Random;

public class RxAndroidActivity6 extends AppCompatActivity {

    private TextView textView;
    private Button btnRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android6);

        initView();

        // 텍스트의 변경사항을 체크
        RxTextView.textChangeEvents((TextView) findViewById(R.id.editText))
                .subscribe(
                        item -> Log.i("word", "item" + item.text().toString())  // 텍스트로 변환
                );

        // setOnClickListener를 통해 OnClickListener에 전달될 이벤트를 옵저버블 형태로 래핑 -> 간단한 click event 구현 가능
        RxView.clicks(findViewById(R.id.btnRandom))
                .map(event -> new Random().nextInt())   // 랜덤 값으로 구현
                .subscribe(
                        number -> textView.setText("number" + number)
                );

    }

    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
        btnRandom = (Button) findViewById(R.id.btnRandom);
    }
}
