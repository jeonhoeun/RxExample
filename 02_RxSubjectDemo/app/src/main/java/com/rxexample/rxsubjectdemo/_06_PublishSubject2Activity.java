package com.rxexample.rxsubjectdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class _06_PublishSubject2Activity extends AppCompatActivity {
    private String tag="RXTEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag,this.getClass().getSimpleName()+" start");
        setContentView(R.layout.activity_main);
        publishSubjectDemo1();
    }

    /*
        PublishSubject는 subscribe시점부터 후속을 모두 받는다. onComplete가 발생하지 않아도 이벤트는 발생한다.
     */
    void publishSubjectDemo1(){
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.subscribe(getFirstObserver());
        publishSubject.onNext("JAVA");
        publishSubject.onNext("KOTLIN");
        publishSubject.onNext("XML");
        publishSubject.subscribe(getSecondObserver());
        publishSubject.onNext("JSON");
        publishSubject.onComplete(); // 이거를 주석처리해도 이벤트는 발생한다.
        publishSubject.subscribe(getThirdObserver());
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
