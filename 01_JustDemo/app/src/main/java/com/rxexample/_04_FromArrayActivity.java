package com.rxexample;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class _04_FromArrayActivity extends AppCompatActivity {
    private String tag="RXTEST";
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String[] greetings={"gretting A","gretting B","gretting C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fromArray를 이용하면 just에 "aa","bb" 를 넣은것같은 효과를 낼 수 있다. just에 콤마로 넣는것은 최대 10개까지만 되자만, fromArray는 이런 제약이 없다.
        myObservable = Observable.fromArray(greetings);
        Log.i(tag,"start compositeDisposable.add(..)");
        compositeDisposable.add(
                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getDisposableObserver())
        );

        SystemClock.sleep(1000);
        Log.i(tag,"return onCreate()");

    }

    private DisposableObserver getDisposableObserver(){
        myObserver = new DisposableObserver<String>(){
            @Override
            public void onNext(String s) {
                Log.i(tag,"onNext :"+s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(tag,"onError");
            }

            @Override
            public void onComplete() {
                Log.i(tag,"onComplete");
            }
        };
        Log.i(tag,"return myObserver");
        return myObserver;
    }
}
