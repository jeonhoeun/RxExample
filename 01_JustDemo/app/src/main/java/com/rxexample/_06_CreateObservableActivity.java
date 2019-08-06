package com.rxexample;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.rxexample.res.Student;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class _06_CreateObservableActivity extends AppCompatActivity {
    private String tag="RXTEST";
    private Observable<Student> myObservable;
    private DisposableObserver<Student> myObserver;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obserbable은 obserbable을 정의할 수 있다. 로그 순서를 보면 우리가 생각한거와는 조금 다르게 동작함을 알 수 있다. ( myObserbable의 subscribe가 모두 호출된뒤 myObserver가 호출됨 )
        myObservable = Observable.create(new ObservableOnSubscribe<Student>() {
            @Override
            public void subscribe(ObservableEmitter<Student> emitter) throws Exception {
                Log.i(tag,"subscribe start");
                ArrayList<Student> students = getStudents();
                for(Student student : students){
                    Log.i(tag,"onNext call");
                    emitter.onNext(student);
                    Log.i(tag,"after onNext");
                }
                Log.i(tag,"onComplete call");

                emitter.onComplete();

                Log.i(tag,"after onComplete");
            }
        });
        Log.i(tag,"start compositeDisposable.add(..)");
        //아래 myObservable.subscribe 이용하지않으면(구독안하면)위의 myObservable의 subscribe하 호출되지 않는다.
        compositeDisposable.add(
                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getDisposableObserver())
        );

        // 아래 주석을 풀면 위의 subscribe도 다시 호출된다. 즉, 구독할때마다 subscribe가 호출됨을 알 수 있다.
//        compositeDisposable.add(
//                myObservable
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(getDisposableObserver())
//        );
        SystemClock.sleep(1000);
        Log.i(tag,"return onCreate()");

    }

    private DisposableObserver getDisposableObserver(){
        myObserver = new DisposableObserver<Student>(){
            @Override
            public void onNext(Student s) {
                Log.i(tag,"onNext :"+s.getName());
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


    private ArrayList<Student> getStudents(){
        ArrayList<Student> rtnVal = new ArrayList<>();
        Student s = new Student();
        s.setName("aaa");
        rtnVal.add(s);

        s = new Student();
        s.setName("bbb");
        rtnVal.add(s);
        return rtnVal;
    }
}
