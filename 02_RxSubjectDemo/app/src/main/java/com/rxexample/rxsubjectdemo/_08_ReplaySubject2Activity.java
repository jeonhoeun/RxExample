package com.rxexample.rxsubjectdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class _08_ReplaySubject2Activity extends AppCompatActivity {
    private String tag="RXTEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag,this.getClass().getSimpleName()+" start");
        setContentView(R.layout.activity_main);
        replaySubjectDemo2();
    }

    /*
        ReplaySubject는 subscribe시점과 관계없이 모든 데이터를 방출한다. 이는 onComplete이후라도 데이터를 받을 수 있다.
     */
    void replaySubjectDemo2(){
        ReplaySubject<String> replaySubject = ReplaySubject.create();
        replaySubject.subscribe(getFirstObserver());
        replaySubject.onNext("JAVA");
        replaySubject.onNext("KOTLIN");
        replaySubject.onNext("XML");
        replaySubject.subscribe(getSecondObserver());
        replaySubject.onNext("JSON");
        replaySubject.onComplete(); // 이거를 주석처리해도 이벤트는 발생한다.
        replaySubject.subscribe(getThirdObserver());
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
