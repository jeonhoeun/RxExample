package com.rxexample.rxsubjectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;

public class _01_AsyncSubjectActivity extends AppCompatActivity {
    private String tag="RXTEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asyncSubjectDemo1();
    }

    /*
        AsyncSubject는 onComplete기준으로 마지막 onNext에 들어온값만 emit한다. 아래 예제에서는 JSON만 emit한다.
     */

    void asyncSubjectDemo1(){
        Observable<String> observable = Observable.just("JAVA","KOTLIN","XML","JSON");
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        observable.subscribe(asyncSubject);

        asyncSubject.subscribe(getFirstObserver());
        asyncSubject.subscribe(getSecondObserver());
        asyncSubject.subscribe(getThirdObserver());
    }

    private Observer<String> getFirstObserver(){
        Observer<String> observer1 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(tag, "First observer onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i(tag, "First observer onNext :" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(tag, "First observer onError :");
            }

            @Override
            public void onComplete() {
                Log.i(tag, " First observer onComplete");
            }
        };
        return observer1;
    }

    private Observer<String> getSecondObserver(){
        Observer<String> observer2 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(tag, "Second observer onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i(tag, "Second observer onNext :" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(tag, "Second observer onError :");
            }

            @Override
            public void onComplete() {
                Log.i(tag, " Second observer onComplete");
            }
        };
        return observer2;
    }

    private Observer<String> getThirdObserver(){
        Observer<String> observer3 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(tag, "Third observer onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i(tag, "Third observer onNext :" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(tag, "Third observer onError :");
            }

            @Override
            public void onComplete() {
                Log.i(tag, " Third observer onComplete");
            }
        };
        return observer3;
    }
}
