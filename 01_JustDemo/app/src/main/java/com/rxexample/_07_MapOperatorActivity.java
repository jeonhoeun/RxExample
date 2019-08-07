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
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class _07_MapOperatorActivity extends AppCompatActivity {
    private String tag="RXTEST";
    private Observable<Student> myObservable;
    private DisposableObserver<Student> myObserver;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /*
            아래와같이 map을 이용하면 emit되는 데이터 변형이 가능하다.
            map 첫번째 파라미터는 입력, 두번째 파라미터는 출력이다.
            이를 위해서는 apply를 override해야된다.
            아래의 appply 호출 시점은 로그를보면 좋으며, 각 emit별 myObserver의 onNext가 호출되기직전에 호출된다.
         */
        compositeDisposable.add(
                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Student, Student>() {
                            @Override
                            public Student apply(Student student) throws Exception {
                                Log.i(tag,"map.apply");
                                student.setName(student.getName().toUpperCase());
                                return student;
                            }
                        })
                        .subscribeWith(getDisposableObserver())
        );

        Log.i(tag,"return onCreate()");

    }

    private DisposableObserver getDisposableObserver(){
        myObserver = new DisposableObserver<Student>(){
            @Override
            public void onNext(Student s) {
                Log.i(tag,"myObserver.onNext :"+s.getName());
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
        Log.i(tag,"getStudents() invoked");
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
