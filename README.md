## Filter
- 테스트 조건을 만족하는 항목들만 배출
> Observavle 변형할때 사용
- Map :  전달받은 이벤트를 다른 값으로 변경
```java
public void doMap() {
       observable
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .filter( item -> item.equals("May")?false:true)
               .map( item -> ">> "+item+"")
               .subscribe(
                       item -> {
                           data.add(item);
                           adapter.notifyItemInserted(data.size()-1);
                       },
                       error -> Log.e("Error",error.getMessage()),
                       () -> Log.i("Complete","Successfully completed!")
               );
   }
```
- FlatMap : 하나의 Observavle이 발행하는 항목들을 여러개의 Observavle로 변환, 항목들의 배출을 차례차례 줄 세워 하나의 Observavle로 전달
```java
public void doFmap() {
    observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(item -> Observable.fromArray(new String[]{"name:"+months[item], "code:"+item}))
            .subscribe(
                    item -> {
                        data.add(item);
                        adapter.notifyItemInserted(data.size()-1);
                    },
                    error -> Log.e("Error",error.getMessage()),
                    () -> Log.i("Complete","Successfully completed!")
            );

}
```
- Zip :  네트워크 작업으로 사용자의 프로필과 프로필 이미지를 동시에 요청, 그 결과를 합성해서 화면에 표현해준다거나하는 형태의 작업이 필요한 경우 사용
```java
observableZip = Observable.zip(
               Observable.just("daJung","iJune"),
               Observable.just("girl","boy"),
               (item1, item2) -> "name:"+item1+", gender:"+item2
       );

public void doZip() {
    observableZip
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    item -> {
                        data.add(item+"");
                        adapter.notifyItemInserted(data.size()-1);
                    },
                    error -> Log.e("Error",error.getMessage()),
                    () -> Log.i("Complete","Successfully completed!")
            );
}
```
## Subject
-  하나 이상의 Observable을 구독할 수 있으며 동시에 Observable이기도 하기 때문에 항목들을 하나 하나 거치면서 재배출하고 관찰하며 새로운 항목들을 배출

- publish
```java
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
```
- behavior
```java
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
```

- replay
```java
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
```

- async
```java
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
```

## RxBinding
> 사용 시 추가
```java
implementation 'com.jakewharton.rxbinding2:rxbinding-appcompat-v7:2.+'
```

- textChangeEvents
```java
// 텍스트의 변경사항을 체크
      RxTextView.textChangeEvents((TextView) findViewById(R.id.editText))
              .subscribe(
                      item -> Log.i("word", "item" + item.text().toString())  // 텍스트로 변환
              );
```

- clicks
: setOnClickListener를 통해 OnClickListener에 전달될 이벤트를 옵저버블 형태로 래핑 -> 간단한 click event 구현 가능
```java
        RxView.clicks(findViewById(R.id.btnRandom))
                .map(event -> new Random().nextInt())   // 랜덤 값으로 구현
                .subscribe(
                        number -> textView.setText("number" + number)
                );
```
