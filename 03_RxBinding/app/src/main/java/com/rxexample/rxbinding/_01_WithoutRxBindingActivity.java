package com.rxexample.rxbinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class _01_WithoutRxBindingActivity extends AppCompatActivity {
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

        etNameInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvShowName.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btClearNameInputField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNameInputField.setText("");
                tvShowName.setText("");
            }
        });
    }
}
