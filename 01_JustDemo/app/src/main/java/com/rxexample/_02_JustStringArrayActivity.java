package com.rxexample;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class _02_JustStringArrayActivity extends AppCompatActivity {
    private String tag="RXTEST";
    private String gretting[] = {"gretting A","gretting B","gretting C"};
    private Observable<String[]> myObservable;
    private DisposableObserver<String[]> myObserver;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //just 는 String[]을 하나씩 보내지않고, 입력받은 String[] 인스턴스를 한번에 보낸다.
        myObservable = Observable.just(gretting);
        compositeDisposable.add(
                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getDisposableObserver())
        );

    }

    private DisposableObserver getDisposableObserver(){
        myObserver = new DisposableObserver<String[]>(){
            @Override
            public void onNext(String[] s) {
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
        return myObserver;
    }
}
