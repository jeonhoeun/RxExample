package com.rxexample.rxsubjectdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;

public class _04_BehaviorSubject2Activity extends AppCompatActivity {
    private String tag="RXTEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag,this.getClass().getSimpleName()+" start");
        setContentView(R.layout.activity_main);
        behaviorSubjectDemo2();
    }

    /*
        BehaviorSubject는 subscribe시점에서 바로전에꺼와 이후 받는것들만 이벤트를 발생한다.
        그리고 onComplete이후는 받지 못한다.
     */
    void behaviorSubjectDemo2(){
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.subscribe(getFirstObserver());
        behaviorSubject.onNext("JAVA");
        behaviorSubject.onNext("KOTLIN");
        behaviorSubject.onNext("XML");
        behaviorSubject.subscribe(getSecondObserver());
        behaviorSubject.onNext("JSON");
        behaviorSubject.onComplete();
        behaviorSubject.subscribe(getThirdObserver());
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
