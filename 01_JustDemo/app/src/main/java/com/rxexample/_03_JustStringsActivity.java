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

public class _03_JustStringsActivity extends AppCompatActivity {
    private String tag="RXTEST";
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //just 에 String들을 ,(콤마)로 여러개 보내면 한개씩 처리된다. 이와같이 ,(콤마)로 넣는방식은 최대 10개까지 넣을수 있다.
        myObservable = Observable.just("gretting A","gretting B","gretting C");
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
