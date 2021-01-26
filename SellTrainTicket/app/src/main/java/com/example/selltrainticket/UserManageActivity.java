package com.example.selltrainticket;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserManageActivity extends AppCompatActivity {


    RadioGroup operate_user_type;
    EditText operate_username_view;
    Button exec_button;
    Button exit_button;
    ImageView train_manage_image;
    ImageView order_query_image;

    String username;

    SQLiteDatabase db;


    //初始化组件
    public void initViews(){

        operate_user_type=findViewById(R.id.operate_user_type);
        operate_username_view=findViewById(R.id.operate_username_view);
        exec_button=findViewById(R.id.exec_button);
        exit_button=findViewById(R.id.exit_button);
        train_manage_image=findViewById(R.id.train_manage_image);
        order_query_image=findViewById(R.id.order_query_image);

    }


    //设置页面跳转
    public void setPageJump(){
        train_manage_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到车次管理页面并传递用户名
                Intent intent=new Intent(UserManageActivity.this,TrainManageActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);
            }
        });



        order_query_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到订单查询页面并传递用户名
                Intent intent=new Intent(UserManageActivity.this,OrderQueryActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);
            }
        });
    }




    //设置执行
    public void setExec(){

        exec_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击执行按钮后完成对应操作

                //获取用户名
                String un=operate_username_view.getText().toString();

                //判断操作类型
                if(operate_user_type.getCheckedRadioButtonId()==R.id.seal){

                    //封号


                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    //执行封号操作
                    String seal_sql="update usermessege set status=0 where username='"+un+"'";
                    db.execSQL(seal_sql);

                    Toast.makeText(UserManageActivity.this,"封号已完成",Toast.LENGTH_SHORT).show();

                }
                else{

                    //解封


                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    //执行解封操作
                    String unseal_sql="update usermessege set status=1 where username='"+un+"'";
                    db.execSQL(unseal_sql);

                    Toast.makeText(UserManageActivity.this,"解封已完成",Toast.LENGTH_SHORT).show();


                }

            }
        });

    }



    //设置退出
    public void setExit() {

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击退出登录按钮之后跳转到登录界面
                Intent intent=new Intent(UserManageActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);

            }
        });

    }







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manage_page);


        //初始化组件
        initViews();

        //接收用户名
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");

        //设置页面跳转
        setPageJump();



        //设置执行
        setExec();


        //设置退出
        setExit();

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
