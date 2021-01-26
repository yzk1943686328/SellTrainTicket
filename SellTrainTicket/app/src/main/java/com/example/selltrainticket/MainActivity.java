package com.example.selltrainticket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.app.Activity.*;
import static com.example.selltrainticket.R.*;

public class MainActivity extends AppCompatActivity {

    EditText username_field;
    EditText password_field;
    CheckBox rememberpassword;
    TextView register;
    Button login_button;
    TextView user_login;
    TextView manager_login;

    SQLiteDatabase db;
    Cursor result;


    String username;
    String password;
    String real_password;

    String login_mode="USER";


    Workbook TrainMessege_book;
    Workbook TrainPassStation_book;
    Workbook TrainSeat_book;
    Workbook TrainTicketPrice_book;

    Sheet TrainMessege_sheet;
    Sheet TrainPassStation_sheet;
    Sheet TrainSeat_sheet;
    Sheet TrainTicketPrice_sheet;


    public void initviews(){
        username_field=findViewById(id.username);
        password_field=findViewById(id.password);
        rememberpassword=findViewById(id.remember_password);
        register=findViewById(id.register);
        login_button=findViewById(id.login_button);
        user_login=findViewById(id.user_login);
        manager_login=findViewById(id.manager_login);
    }

    //初始化Sqlite数据库
    public void initDatabase(){
        //建立数据库
        db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);


        //建立用户表
        db.execSQL("CREATE TABLE IF NOT EXISTS usermessege(username varchar(100),password varchar(20),status int,money int)");



        //建立管理员表
        db.execSQL("CREATE TABLE IF NOT EXISTS managermessege(username varchar(100),password varchar(20))");
        //向管理员表中插入原始数据
        Cursor c=db.rawQuery("select * from managermessege",null);
        if(c.getCount()==0){
            //第一次打开时插入数据
            db.execSQL("insert into managermessege values('袁仲锴','123456')");
        }




        //建立火车车次表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainmessege(time varchar(20),trainid varchar(20),trainname varchar(20),traintype varchar(20),startstation varchar(100),arrivestation varchar(100),carriagenumber int,seatnumber int)");
        //向火车车次表中插入数据
        Cursor c2=db.rawQuery("select * from trainmessege",null);
        if(c2.getCount()==0){
            //第一次打开时插入数据

            Toast.makeText(MainActivity.this,"1",Toast.LENGTH_SHORT).show();

            //打开Excel文件
            try {
                TrainMessege_book = Workbook.getWorkbook(getAssets().open("TrainMessege.xls"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }


            TrainMessege_sheet = TrainMessege_book.getSheet(0);



            //插入数据
            for(int i=1;i<TrainMessege_sheet.getRows();i++){

                String time=TrainMessege_sheet.getCell(0,i).getContents();
                String trainid=TrainMessege_sheet.getCell(1,i).getContents();
                String trainname=TrainMessege_sheet.getCell(2,i).getContents();
                String traintype=TrainMessege_sheet.getCell(3,i).getContents();
                String start_station_name=TrainMessege_sheet.getCell(4,i).getContents();
                String arrive_station_name=TrainMessege_sheet.getCell(5,i).getContents();
                int carriagenumber=Integer.parseInt(TrainMessege_sheet.getCell(6,i).getContents());
                int seatnumber=Integer.parseInt(TrainMessege_sheet.getCell(7,i).getContents());


                String sql="insert into trainmessege values('"+time+"','"+trainid+"','"+trainname+"','"+traintype+"','"+start_station_name+"','"+arrive_station_name+"',"+carriagenumber+","+seatnumber+")";
                db.execSQL(sql);


            }



        }




        //建立火车经停站表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainpassstation(time varchar(20),trainid varchar(20),cityname varchar(20),stationname varchar(20),stationid int,arrivetime varchar(20),staytime int,leavetime varchar(20))");
        //向火车经停站表中插入数据
        Cursor c3=db.rawQuery("select * from trainpassstation",null);
        if(c3.getCount()==0){
            //第一次打开时插入数据

            //打开Excel文件
            try {
                TrainPassStation_book = Workbook.getWorkbook(getAssets().open("TrainPassStation.xls"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }

            TrainPassStation_sheet = TrainPassStation_book.getSheet(0);

            //插入数据
            for(int i=1;i<TrainPassStation_sheet.getRows();i++){

                String time=TrainPassStation_sheet.getCell(0,i).getContents();
                String trainid=TrainPassStation_sheet.getCell(1,i).getContents();
                String cityname=TrainPassStation_sheet.getCell(2,i).getContents();
                String stationname=TrainPassStation_sheet.getCell(3,i).getContents();
                int stationid=Integer.parseInt(TrainPassStation_sheet.getCell(4,i).getContents());
                String arrivetime=TrainPassStation_sheet.getCell(5,i).getContents();
                int staytime=Integer.parseInt(TrainPassStation_sheet.getCell(6,i).getContents());
                String leavetime=TrainPassStation_sheet.getCell(7,i).getContents();

                String sql="insert into trainpassstation values('"+time+"','"+trainid+"','"+cityname+"','"+stationname+"',"+stationid+",'"+arrivetime+"',"+staytime+",'"+leavetime+"')";
                db.execSQL(sql);

            }

        }




        //建立火车票座次表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainseat(time varchar(20),trainid varchar(20),carriageid int,rowid int,position varchar(20),seattype varchar(20),fromstation int,tostation int)");
        //向火车票座次表中插入数据
        Cursor c4=db.rawQuery("select * from trainseat",null);
        if(c4.getCount()==0){
            //第一次打开时插入数据

            //打开Excel文件
            try {
                TrainSeat_book = Workbook.getWorkbook(getAssets().open("TrainSeat.xls"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }

            TrainSeat_sheet = TrainSeat_book.getSheet(0);

            //插入数据
            for(int i=1;i<TrainSeat_sheet.getRows();i++){

                String time=TrainSeat_sheet.getCell(0,i).getContents();
                String trainid=TrainSeat_sheet.getCell(1,i).getContents();
                int carriageid=Integer.parseInt(TrainSeat_sheet.getCell(2,i).getContents());
                int rowid=Integer.parseInt(TrainSeat_sheet.getCell(3,i).getContents());
                String position=TrainSeat_sheet.getCell(4,i).getContents();
                String seattype=TrainSeat_sheet.getCell(5,i).getContents();
                int fromstation=Integer.parseInt(TrainSeat_sheet.getCell(6,i).getContents());
                int tostation=Integer.parseInt(TrainSeat_sheet.getCell(7,i).getContents());

                String sql="insert into trainseat values('"+time+"','"+trainid+"',"+carriageid+","+rowid+",'"+position+"','"+seattype+"',"+fromstation+","+tostation+")";
                db.execSQL(sql);

            }

        }





        //建立火车票价格表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainticketprice(trainid varchar(20),fromstationname varchar(20),tostationname varchar(20),fromstationid int,tostationid int,seattype varchar(20),price int)");
        //向火车票价格表中插入数据
        Cursor c5=db.rawQuery("select * from trainticketprice",null);
        if(c5.getCount()==0){
            //第一次打开时插入数据

            //打开Excel文件
            try {
                TrainTicketPrice_book = Workbook.getWorkbook(getAssets().open("TrainTicketPrice.xls"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }

            TrainTicketPrice_sheet = TrainTicketPrice_book.getSheet(0);


            //插入数据
            for(int i=1;i<TrainTicketPrice_sheet.getRows();i++){

                String trainid=TrainTicketPrice_sheet.getCell(0,i).getContents();
                String fromstationname=TrainTicketPrice_sheet.getCell(1,i).getContents();
                String tostationname=TrainTicketPrice_sheet.getCell(2,i).getContents();
                int fromstationid=Integer.parseInt(TrainTicketPrice_sheet.getCell(3,i).getContents());
                int tostationid=Integer.parseInt(TrainTicketPrice_sheet.getCell(4,i).getContents());
                String seattype=TrainTicketPrice_sheet.getCell(5,i).getContents();
                int price=Integer.parseInt(TrainTicketPrice_sheet.getCell(6,i).getContents());

                String sql="insert into trainticketprice values('"+trainid+"','"+fromstationname+"','"+tostationname+"',"+fromstationid+","+tostationid+",'"+seattype+"',"+price+")";
                db.execSQL(sql);

            }



        }



        //建立订单表
        db.execSQL("CREATE TABLE IF NOT EXISTS orderstable(time varchar(20),username varchar(20),trainid varchar(20),startstationname varchar(20),leavetime varchar(20),arrivestationname varchar(20),arrivetime varchar(20),carriageid int,rowid int,position varchar(10),seattype varchar(20),price int,ordertime varchar(20))");





    }


    //判断是否保存了密码，若保存了密码则显示已保存的密码
    public void RememberOrNot(){

        SharedPreferences sp=getSharedPreferences("zhmm",MODE_PRIVATE);
        Boolean rp=sp.getBoolean("REMEMBER_PASSWORD",false);

        if(rp){
            //如果保存了密码就将密码显示出来
            String saved_username=sp.getString("USERNAME","");
            String saved_password=AES.decode("4166b0828a80cc1b37e5e3731b176f50",sp.getString("PASSWORD",""));
            username_field.setText(saved_username);
            password_field.setText(saved_password);
            rememberpassword.setChecked(true);
        }

    }


    //设置注册跳转
    public void setRegister(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到注册页面并关闭原来的界面
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);

            }
        });
    }

    //设置模式转换
    public void setModuleChange(){
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //将模式改为用户模式并改变颜色
                login_mode="USER";

                user_login.setTextColor(getResources().getColor(color.blue));
                manager_login.setTextColor(getResources().getColor(color.gray));

                username_field.setHint("用户名/邮箱/手机号码");
                register.setVisibility(View.VISIBLE);//显示注册按钮
            }
        });


        manager_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将模式改为管理员模式并改变颜色
                login_mode="MANAGER";

                manager_login.setTextColor(getResources().getColor(color.blue));
                user_login.setTextColor(getResources().getColor(color.gray));

                username_field.setHint("管理员名");//将默认文字改为“管理员名”
                register.setVisibility(View.INVISIBLE);//隐藏注册按钮


            }
        });

    }


    //设置登录
    public void setLoginIn(){
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取用户输入
                username=username_field.getText().toString();
                password=password_field.getText().toString();

                //判断是用户登录还是管理员登录
                if(login_mode.equals("USER")){
                    //用户登录

                    //验证账号密码是否正确
                    String sql="select * from usermessege where username='"+username+"'";
                    result=db.rawQuery(sql,null);

                    if(result.getCount()==0){
                        //用户不存在
                        Tips("用户不存在!");

                        username_field.setText("");
                        password_field.setText("");
                    }
                    else{

                        //用户存在，检查是否被封号
                        result.moveToFirst();
                        int status=result.getInt(2);
                        if(status==0){

                            //已被封号
                            Tips("您的账号已被封号！");

                        }
                        else{

                            //未被封号，检查密码是否正确

                            result.moveToFirst();
                            real_password=result.getString(1);

                            if(password.equals(real_password)){


                                //密码正确

                                //判断是否勾选记住密码
                                CheckOrNot();

                                //密码正确,跳转到用户首页并传递用户名
                                Intent intent=new Intent(MainActivity.this,HomeActivity.class);

                                Bundle bundle=new Bundle();
                                bundle.putString("username",username);
                                intent.putExtras(bundle);


                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                                startActivity(intent);

                            }
                            else{
                                //密码错误
                                Tips("密码错误!");
                                password_field.setText("");
                            }


                        }


                    }

                }
                else{
                    //管理员登录

                    //验证账号密码是否正确
                    String sql="select * from managermessege where username='"+username+"'";
                    result=db.rawQuery(sql,null);

                    if(result.getCount()==0){
                        //用户不存在
                        Tips("用户不存在!");

                        username_field.setText("");
                        password_field.setText("");
                    }else{

                        //用户存在，检查密码是否正确
                        result.moveToFirst();
                        real_password=result.getString(1);

                        if(password.equals(real_password)){
                            //密码正确

                            //判断是否勾选记住密码
                            CheckOrNot();


                            //密码正确,跳转到管理员界面并传递管理员名
                            Intent intent=new Intent(MainActivity.this,TrainManageActivity.class);

                            Bundle bundle=new Bundle();
                            bundle.putString("username",username);
                            intent.putExtras(bundle);


                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                            startActivity(intent);





                        }else{
                            //密码错误
                            Tips("密码错误!");
                            password_field.setText("");
                        }

                    }

                }


            }
        });
    }


    public void Tips(String messege){
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("提示");
        adb.setMessage(messege);
        adb.show();
    }


    //判断是否勾选记住密码,若勾选则将密码存到SharedPreferences中
    public void CheckOrNot()     {

        SharedPreferences sp=getSharedPreferences("zhmm", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        //判断是否勾选了记住密码
        if(rememberpassword.isChecked()){
            //若勾选了记住密码，就将账号密码存到SharedPreferences中

            editor.putBoolean("REMEMBER_PASSWORD",true);
            editor.putString("USERNAME",username);
            editor.putString("PASSWORD",AES.encode("4166b0828a80cc1b37e5e3731b176f50",password));//加密保存
            editor.commit();

        }else{
            editor.putBoolean("REMEMBER_PASSWORD",false);
            editor.commit();
        }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);


        initviews();

        //初始化数据库
        initDatabase();

        //判断是否保存了密码，若保存了密码则显示已保存的密码
        RememberOrNot();

        //设置注册跳转
        setRegister();

        //设置模式转换
        setModuleChange();

        //设置登录
        setLoginIn();




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


