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

public class _05_RangeActivity extends AppCompatActivity {
    private String tag="RXTEST";
    private Observable<Integer> myObservable;
    private DisposableObserver<Integer> myObserver;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //range는 파라미터로 입력된 숫자를 start부터 count갯수만큼 1씩 증가 및 emit(방출) 예를들어 start가 11이고 count가 3이면 11,12,13을 emit(방출)한다
        myObservable = Observable.range(100,20);
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
        myObserver = new DisposableObserver<Integer>(){
            @Override
            public void onNext(Integer s) {
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
