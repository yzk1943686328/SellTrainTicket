package com.example.selltrainticket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {

    TextView username_view;
    TextView money_view;
    Button exit_button;
    ImageView homepage_image;
    ImageView order_image;



    String username;
    SQLiteDatabase db;


    //初始化组件
    public void initViews(){

        username_view=findViewById(R.id.username_view);
        money_view=findViewById(R.id.money_view);
        exit_button=findViewById(R.id.exit_button);
        homepage_image=findViewById(R.id.homepage_image);
        order_image=findViewById(R.id.order_image);


    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);


        //初始化组件
        initViews();



        //接收用户名
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");

        //设置用户名
        username_view.setText(username);


        //设置余额

        //获取用户当前余额
        db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);
        String money_sql="select * from usermessege where username='"+username+"'";
        Cursor money_result=db.rawQuery(money_sql,null);
        money_result.moveToFirst();
        int current_money=money_result.getInt(3);
        money_view.setText("¥"+current_money);


        //设置页面跳转
        homepage_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到首页并传递用户名
                Intent intent=new Intent(MyActivity.this,HomeActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });


        order_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到订单页面并传递用户名
                Intent intent=new Intent(MyActivity.this,OrderActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });


        //设置退出登录
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击之后退出登录(跳转到登录界面)
                Intent intent=new Intent(MyActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);


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
