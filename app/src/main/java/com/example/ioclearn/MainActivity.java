package com.example.ioclearn;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ioclearn.bean.bind.FindView;
import com.example.ioclearn.bean.bind.OnClick;
import com.example.ioclearn.utils.ViewInject;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ioc注解学习
 * 反射方式
 */
public class MainActivity extends AppCompatActivity {
    @FindView(R.id.text_ioc_test)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInject.bind(this);
        textView.setText("测试");
    }

    @OnClick(id = {R.id.btn_ioc_test, R.id.btn_ioc_test2, R.id.btn_ioc_test3})
    public void onClick(View view) {
        switch (view.getId()) {
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