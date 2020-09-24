package com.example.ioclearn;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annotations.BindView;
import com.example.annotations.OnClick;
import com.example.injectknife.Injectknife;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ioc注解学习
 * 编译注解方式
 */
public class APTActivity extends AppCompatActivity {
    @BindView(R.id.text_ioc_test)
    TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_p_t);
        Injectknife.bind(this);

        tv_test.setText("666");
    }

    @OnClick({R.id.btn_ioc_test, R.id.btn_ioc_test2, R.id.btn_ioc_test3})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.btn_ioc_test:
                Toast.makeText(this, "Click1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ioc_test2:
                Toast.makeText(this, "Click2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ioc_test3:
                Toast.makeText(this, "Click3", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}