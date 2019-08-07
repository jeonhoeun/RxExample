package com.rxexample;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.rxexample.res.Student;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class _09_ConcatMapOperatorActivity extends AppCompatActivity {
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
                ArrayList<Student> students = getStudents();
                for(Student student : students){
                    emitter.onNext(student);
                }
                emitter.onComplete();
            }
        });
        Log.i(tag,"start compositeDisposable.add(..)");

        /*
            delay를 준 경우 FlatMap의 경우 순서가 보장되지않는다. concatMap은 순서가 보장된다 다만
            concatmap은 하나의 emit이 처리되어야만 다음 emit을 수행한다.
            따라서 속도가 우선인경우는 FlatMap, 순서가 우선인경우는 concatMap을 사용한다.
         */
        compositeDisposable.add(
                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
//                        .flatMap(new Function<Student, ObservableSource<Student>>() {
//                            @Override
//                            public ObservableSource<Student> apply(Student student) throws Exception {
//                                Log.i(tag,"flatMap.apply");
//                                Student student1 = new Student();
//                                student1.setName(student.getName().toUpperCase());
//
//                                Student student2 = new Student();
//                                student2.setName("New Member "+student.getName());
//
//                                return Observable.just(student,student1,student2);
//                            }
//                        })
                        .concatMap(new Function<Student, ObservableSource<Student>>() {
                            @Override
                            public ObservableSource<Student> apply(Student student) throws Exception {
                                Log.i(tag,"concatMap.apply");
                                Student student1 = new Student();
                                student1.setName(student.getName().toUpperCase());

                                Student student2 = new Student();
                                student2.setName("New Member "+student.getName());

                                int delay = new Random().nextInt(1000);

                                return Observable.just(student,student1,student2).delay(delay, TimeUnit.MILLISECONDS);
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

        s = new Student();
        s.setName("ccc");
        rtnVal.add(s);

        s = new Student();
        s.setName("ddd");
        rtnVal.add(s);

        s = new Student();
        s.setName("eee");
        rtnVal.add(s);

        s = new Student();
        s.setName("fff");
        rtnVal.add(s);
        return rtnVal;
    }
}
