package com.rxexample.rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class _02_RxBindingActivity extends AppCompatActivity {
    private EditText etNameInputField;
    private TextView tvShowName;
    private Button btClearNameInputField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNameInputField = findViewById(R.id.et_nameInputField);
        tvShowName = findViewById(R.id.tv_showingName);
        btClearNameInputField = findViewById(R.id.btn_clearNameInputField);

        Disposable disposable1 = RxTextView.textChanges(etNameInputField)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        tvShowName.setText(charSequence);
                    }
                });

        Disposable disposable2 = RxView.clicks(btClearNameInputField)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        etNameInputField.setText("");
                        tvShowName.setText("");
                    }
                });
    }
}
