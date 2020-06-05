package com.example.a15711.diarypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.mainactivity.MainActivity;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{
    private SharedPreferences pref;//定义一个SharedPreferences对象
    private SharedPreferences.Editor editor;//调用SharedPreferences对象的edit()方法来获取一个SharedPreferences.Editor对象，用以添加要保存的数据
    private Button login;//登录按钮
    private Button register;//注册按钮
    private EditText adminEdit;//用户名输入框
    private EditText passwordEdit;//密码输入框
    private CheckBox savePassword;//是否保存密码复选框
    private CheckBox showPassword;//显示或隐藏密码复选框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将背景图与状态栏融合到一起，只支持Android5.0以上的版本
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            //布局充满整个屏幕
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //设置状态栏为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        //获取各组件或对象的实例
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        login=(Button)findViewById(R.id.login_button);
        register=(Button)findViewById(R.id.register_button);
        adminEdit=(EditText)findViewById(R.id.admin);
        passwordEdit=(EditText)findViewById(R.id.password);
        savePassword=(CheckBox)findViewById(R.id.save_password);
        showPassword=(CheckBox)findViewById(R.id.show_password);
        //获取当前“是否保存密码”的状态
        final boolean isSave=pref.getBoolean("ischecked",false);
        //当“是否保存密码”勾选时，从SharedPreferences对象中读出保存的内容，并显示出来
        if(isSave){
            String account=pref.getString("default_username","");
            String password=pref.getString("default_password","");
            adminEdit.setText(account);
            passwordEdit.setText(password);
            //把光标移到文本末尾处
            adminEdit.setSelection(adminEdit.getText().length());
            passwordEdit.setSelection(passwordEdit.getText().length());
            savePassword.setChecked(true);
        }

        //用户点击注册时的处理事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String in_usename = adminEdit.getText().toString();
                String in_password = passwordEdit.getText().toString();
                if (in_password.equals("")||in_usename.equals("")) {
                    Toast.makeText(getApplicationContext(), "用户名或者密码未填写", Toast.LENGTH_LONG).show();
                } else if (pref.getString("username"+in_usename, "").equals("")) {
                    editor = pref.edit();
                    editor.putString("username"+in_usename, in_usename);
                    editor.putString("password"+in_usename, in_password);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "用户名已经存在", Toast.LENGTH_LONG).show();
                }
            }
        });

        //用户点击登录时的处理事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String in_usename = adminEdit.getText().toString();
                String in_password = passwordEdit.getText().toString();
//				System.out.println(mysp.getString("password", null)+"\n"+in_password);
                if (in_password.equals(pref.getString("password"+in_usename, null))&&in_usename.equals(pref.getString("username"+in_usename, ""))) {
                     editor = pref.edit();
                    //记住账号密码
                    if (savePassword.isChecked()) {
                        editor.putString("default_username",in_usename );
                        editor.putString("default_password", in_password);
                        editor.putBoolean("ischecked",true);

                    } else {
                        editor.putString("default_username",null );
                        editor.putString("default_password", null);
                        editor.putBoolean("ischecked", false);

                    }
                    editor.commit();
                    Intent intent = new Intent();
                    intent.putExtra("username", in_usename);
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"登录成功！欢迎您，"+in_usename+"!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "用户名或者密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });

        //用户点击'显示密码'复选框
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPassword.isChecked()){
                    showOrHide(passwordEdit,true);
                }else{
                    showOrHide(passwordEdit,false);
                }
            }
        });
    }

    //当用户离开活动时，检测是否勾选记住密码，若勾选则保存用户输入的用户名及密码
    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor=pref.edit();
        String account=adminEdit.getText().toString();
        String password=passwordEdit.getText().toString();
        if(savePassword.isChecked()){
            editor.putBoolean("ischecked",true);
            editor.putString("default_username",account);
            editor.putString("default_password",password);
        }else{
            editor.clear();
        }
        editor.apply();
    }
    //显示或隐藏密码
    private void showOrHide(EditText passwordEdit,boolean isShow){

        //记住光标开始的位置
        int pos = passwordEdit.getSelectionStart();
        if(isShow){
            passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordEdit.setSelection(pos);
    }

}
