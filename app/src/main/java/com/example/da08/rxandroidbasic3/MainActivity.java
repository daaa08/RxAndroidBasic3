package com.example.da08.rxandroidbasic3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
//    ArrayAdapter<String> adapter;
    CustomAdapter adapter;

    Observable<String> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        adapter = new CustomAdapter(this,data);
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
                    adapter.notifyDataSetChanged();  // 매번 데이터를 가져올 때 마다 출력하기 위해 설정 하지만 전체 페이지가 다시 출력 -> 이렇게되면 안 됨(전체 데이터가 변경되었을 경우는 좋음)
//                    adapter.notifyItemChanged(data.size()-1);   // 추가된 데이터만 갱신  -> recyclerView용
                },
                error -> Log.e("Error",error.getMessage()),     // onError
                ()  -> {                                             // onComplete
                    data.add("complete");
                    adapter.notifyDataSetChanged();
//                    adapter.notifyItemChanged( data.size()-1);
                }
        );
    }

    private void initView() {
        txt = (TextView) findViewById(R.id.txt);
        listView = (ListView) findViewById(R.id.listView);
    }
}
class CustomAdapter extends BaseAdapter{

    List<String> data = null;
    LayoutInflater inflater;

    public CustomAdapter(Context context, List<String> data){
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int lastPosition = -1;   // 마지막 위치를 잡아주고

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(position > lastPosition){    // 마지막 위치보다 추가,변경되었을 경우 그 부분만 업데이트
            // 아래 코드 동작..
        }else{
            return view;
        }


        if(view == null)
            view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(data.get(position));
            Log.i("ReFresh", "=====================" + position);
        return view;
    }

//    @Override
//    public boolean hasStableIds() {   // onBindViewHolder를 최적화하여 호출 가능, getItemId(int)를 반드시 구현해야 작동
//        return true;
//    }
}