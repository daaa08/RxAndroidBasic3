package com.example.da08.rxandroidbasic3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.regex.Pattern;

import io.reactivex.Observable;

/**
 * A login screen that offers login via email/password.
 */
public class RxBasic7LoginActivity extends AppCompatActivity {


    private EditText editEmail;
    private EditText editPw;
    private Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_basic7_login);

        initView();
        btnSign.setEnabled(false);   // setEnabled -> button 비활성

        // 발행자 emitter
        Observable<TextViewTextChangeEvent> idEmitter =  RxTextView.textChangeEvents(editEmail);
        Observable<TextViewTextChangeEvent> pwEmitter = RxTextView.textChangeEvents(editPw);

        // zip()과 같음
        Observable.combineLatest(idEmitter,pwEmitter,
                (idEvent,pwEvent) -> {
                    boolean checkId = idEvent.text().length() >= 5;   // 5글자가 넘어가는 순간 checkId가 true가 됨
                    boolean checkPw = pwEvent.text().length() >=8;    // 8글자가 넘어가는 순간 checkPw가 true가 됨

                    return  checkId && checkPw;   // 둘 다 true면 최종적으로 true가 넘어가게 됨
                }
        ).subscribe(
                flag -> btnSign.setEnabled(flag)    // id,pw가 몇글자인지에 따라서 버튼이 활성/비활성이 됨   -> 반응형
        );
    }

    private void initView() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPw = (EditText) findViewById(R.id.editPw);
        btnSign = (Button) findViewById(R.id.btnSign);
    }
}

