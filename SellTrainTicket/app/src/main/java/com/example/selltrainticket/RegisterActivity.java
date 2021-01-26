package com.example.selltrainticket;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {

    ImageView back_button;
    EditText username_field;
    EditText password_field;
    EditText confirmpassword_field;
    Button register_button;

    SQLiteDatabase db;


    String username;
    String password;
    String confirmpassword;

    public void initviews(){
        back_button=findViewById(R.id.back);
        username_field=findViewById(R.id.username);
        password_field=findViewById(R.id.password);
        confirmpassword_field=findViewById(R.id.confirm_password);
        register_button=findViewById(R.id.register);
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        initviews();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到登录界面并关闭原来的界面
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);

            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取用户输入
                username=username_field.getText().toString();
                password=password_field.getText().toString();
                confirmpassword=confirmpassword_field.getText().toString();


                //判断用户两次输入的密码是否一致
                if(password.equals(confirmpassword)==true){

                    //两次密码输入一致，将注册信息写入数据库中

                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    //向数据库中插入数据
                    String sql="insert into usermessege values('"+username+"','"+password+"',1,10000)";
                    db.execSQL(sql);

                    AlertDialog.Builder adb=new AlertDialog.Builder(RegisterActivity.this);
                    adb.setTitle("提示");
                    adb.setMessage("注册成功!");
                    adb.show();

                    //跳转到登录界面并关闭原来的界面
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intent);

                }else{

                    //两次密码不一致，提醒用户并清空密码框
                    AlertDialog.Builder adb=new AlertDialog.Builder(RegisterActivity.this);
                    adb.setTitle("提示");
                    adb.setMessage("您两次输入的密码不一致，请重新输入!");
                    adb.show();

                    password_field.setText("");
                    confirmpassword_field.setText("");
                }

            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
