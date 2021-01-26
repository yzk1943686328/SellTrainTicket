package com.example.selltrainticket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class QueryActivity extends AppCompatActivity {


    SQLiteDatabase db;
    String username;

    Bundle bundle;
    String start_city_name;
    String arrive_city_name;
    String time;
    Boolean only;


    LinearLayout query_page;
    TextView start_arrive_city_view;



    public void initViews(){
        query_page=findViewById(R.id.query_page);
        start_arrive_city_view=findViewById(R.id.start_arrive_city_view);
    }


    //查询满足条件的火车
    public void Query(){

        //打开数据库
        db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);


        //查询符合要求的车次
        String sql="select distinct trainid from trainpassstation as A where cityname='"+start_city_name+"' and exists(select trainid from trainpassstation as B  where B.trainid=A.trainid and B.cityname='"+arrive_city_name+"' and B.stationid>A.stationid)";
        Cursor c=db.rawQuery(sql,null);

        //判断查询结果是否为空
        if(c.getCount()==0){
            //没有满足条件的车次

            Toast.makeText(QueryActivity.this,"未查询到满足条件的车次!",Toast.LENGTH_SHORT).show();

        }
        else{
            //有满足条件的车次

            c.moveToFirst();

            while(c.isAfterLast()==false){
                String trainid=c.getString(0);


                //查询发车信息与到达信息

                //查询发车信息
                String StartSQL="select * from trainpassstation where trainid='"+trainid+"' and cityname='"+start_city_name+"'";
                Cursor c2=db.rawQuery(StartSQL,null);
                c2.moveToFirst();
                String start_station_name=c2.getString(3);
                int start_station_id=c2.getInt(4);
                String leavetime=c2.getString(7);

                //查询到达信息
                String ArriveSQL="select * from trainpassstation where trainid='"+trainid+"' and cityname='"+arrive_city_name+"'";
                Cursor c3=db.rawQuery(ArriveSQL,null);
                c3.moveToFirst();
                String arrive_station_name=c3.getString(3);
                int arrive_station_id=c3.getInt(4);
                String arrivetime=c3.getString(5);





                //查询余票数

                //查询一等票余票数
                String FirstSeatSql="select * from trainseat where trainid='"+trainid+"' and seattype='一等座' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                Cursor c4=db.rawQuery(FirstSeatSql,null);
                int FirstSeatNumber=c4.getCount();

                //查询二等票余票数
                String SecondSeatSql="select * from trainseat where trainid='"+trainid+"' and seattype='二等座' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                Cursor c5=db.rawQuery(SecondSeatSql,null);
                int SecondSeatNumber=c5.getCount();





                //查询票价

                //查询一等座的票价
                String FirstPriceSql="select * from trainticketprice where trainid='"+trainid+"' and seattype='一等座' and fromstationid>="+start_station_id+" and tostationid<="+arrive_station_id;
                Cursor c6=db.rawQuery(FirstPriceSql,null);
                c6.moveToFirst();

                int FirstSeatPrice=0;

                while(c6.isAfterLast()==false){
                    FirstSeatPrice+=c6.getInt(6);
                    c6.moveToNext();
                }
                c6.close();

                //查询二等座的票价
                String SecondPriceSql="select * from trainticketprice where trainid='"+trainid+"' and seattype='二等座' and fromstationid>="+start_station_id+" and tostationid<="+arrive_station_id;
                Cursor c7=db.rawQuery(SecondPriceSql,null);
                c7.moveToFirst();

                int SecondSeatPrice=0;

                while(c7.isAfterLast()==false){
                    SecondSeatPrice+=c7.getInt(6);
                    c7.moveToNext();
                }
                c7.close();





                String messege="满足条件的车次为:"+trainid+"发车时间为:"+leavetime+"到达时间为:"+arrivetime+"一等座余票为:"+FirstSeatNumber+"一等座票价为:"+FirstSeatPrice;
                Toast.makeText(QueryActivity.this,messege,Toast.LENGTH_SHORT).show();



                //将信息显示在查询界面
                TrainMessegeView tmv=new TrainMessegeView(this,null,time,trainid,start_station_name,start_station_id,arrive_station_name,arrive_station_id,leavetime,arrivetime,FirstSeatNumber,SecondSeatNumber,FirstSeatPrice,SecondSeatPrice,username,db);
                query_page.addView(tmv);

                //添加分隔线
                TextView divide_line=new TextView(QueryActivity.this);
                divide_line.setHeight(20);
                divide_line.setBackgroundColor(getResources().getColor(R.color.graywhite));
                query_page.addView(divide_line);



                c.moveToNext();

            }

            c.close();


        }




    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_page);

        initViews();


        //获取传来的信息
        bundle=this.getIntent().getExtras();
        username=bundle.getString("username");
        start_city_name=bundle.getString("start_city_name");
        arrive_city_name=bundle.getString("arrive_city_name");
        time=bundle.getString("time");
        only=bundle.getBoolean("only");


        start_arrive_city_view.setText(start_city_name+"一"+arrive_city_name);


        try {
            //开始查询
            Query();
        }catch (Exception e){
            Toast.makeText(QueryActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }



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
