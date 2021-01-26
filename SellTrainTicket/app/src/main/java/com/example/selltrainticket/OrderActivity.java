package com.example.selltrainticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    ImageView homepage_image;
    ImageView my_image;
    LinearLayout jiemian;


    String username;

    SQLiteDatabase db;
    Cursor result;


    String time;
    String trainid;
    String startstationname;
    String leavetime;
    String arrivestationname;
    String arrivetime;
    int carriageid;
    int rowid;
    String position;
    String seattype;
    int price;
    String ordertime;






    //初始化组件
    public void initViews(){
        homepage_image=findViewById(R.id.homepage_image);
        my_image=findViewById(R.id.my_image);
        jiemian=findViewById(R.id.jiemian);

    }


    //设置页面跳转
    public void setPageJump(){
        homepage_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到首页并传递用户名
                Intent intent=new Intent(OrderActivity.this,HomeActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);

            }
        });


        my_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到我的页面并传递用户名
                Intent intent=new Intent(OrderActivity.this,MyActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
    }



    //查询订单
    public void queryOrder(){

        //打开数据库
        db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

        //查询当前用户的所有订单
        String order_sql="select * from orderstable where username='"+username+"'";
        result=db.rawQuery(order_sql,null);

        if(result.getCount()==0){
            //无订单
            Toast.makeText(OrderActivity.this,"未查询到订单！",Toast.LENGTH_SHORT).show();
        }else{
            //有订单
            result.moveToFirst();
            while(result.isAfterLast()==false){

                //读取当前订单项的内容
                time=result.getString(0);
                trainid=result.getString(2);
                startstationname=result.getString(3);
                leavetime=result.getString(4);
                arrivestationname=result.getString(5);
                arrivetime=result.getString(6);
                carriageid=result.getInt(7);
                rowid=result.getInt(8);
                position=result.getString(9);
                seattype=result.getString(10);
                price=result.getInt(11);
                ordertime=result.getString(12);




                //新建订单项并添加到界面
                OrderOptionActivity ooa=new OrderOptionActivity(this,null,username,time,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime,OrderActivity.this);
                ooa.setTop(15);

                //设置点击事件监听器
                try {
                    ooa.setOnClickListener(new View.OnClickListener() {

                        String time2=time;
                        String trainid2=trainid;
                        String startstationname2=startstationname;
                        String leavetime2=leavetime;
                        String arrivestationname2=arrivestationname;
                        String arrivetime2=arrivetime;
                        int carriageid2=carriageid;
                        int rowid2=rowid;
                        String position2=position;
                        String seattype2=seattype;
                        int price2=price;
                        String ordertime2=ordertime;

                        @Override
                        public void onClick(View v) {

                            //点击后进入详情界面并传递数据
                            Intent intent=new Intent(OrderActivity.this,OrderDetailActivity.class);

                            Bundle bundle=new Bundle();
                            bundle.putString("order_time",ordertime2);
                            bundle.putString("start_station_name",startstationname2);
                            bundle.putString("leave_time",leavetime2);
                            bundle.putString("trainid",trainid2);
                            bundle.putString("total_time",calculateTotalTime(leavetime,arrivetime2));
                            bundle.putString("arrive_station_name",arrivestationname2);
                            bundle.putString("arrive_time",arrivetime2);
                            bundle.putString("leave_date",time2);
                            bundle.putString("passenger_name",username);
                            bundle.putInt("price",price2);
                            bundle.putString("seat_type",seattype2);
                            bundle.putInt("carriage_id",carriageid2);
                            bundle.putInt("row_id",rowid2);
                            bundle.putString("position",position2);
                            intent.putExtras(bundle);

                            startActivity(intent);


                        }
                    });
                }catch (Exception e){
                    AlertDialog.Builder adb=new AlertDialog.Builder(OrderActivity.this);
                    adb.setMessage(e.getMessage());
                    adb.show();
                }

                jiemian.addView(ooa);

                //添加分隔线
                TextView divide_line=new TextView(OrderActivity.this);
                divide_line.setHeight(1);
                divide_line.setTop(15);
                divide_line.setBackgroundColor(getResources().getColor(R.color.gray));
                jiemian.addView(divide_line);



                result.moveToNext();




            }


        }





    }





    //根据发车时间和到达时间计算总时间
    public String calculateTotalTime(String leavetime,String arrivetime){

        //找出分隔符":"的位置
        int leavetime_divide=leavetime.indexOf(":");
        int arrivetime_divide=arrivetime.indexOf(":");

        //获取发车时间和到达时间的时和分
        int leave_hour=Integer.parseInt(leavetime.substring(0,leavetime_divide));
        int leave_minute=Integer.parseInt(leavetime.substring(leavetime_divide+1,leavetime.length()));
        int arrive_hour=Integer.parseInt(arrivetime.substring(0,arrivetime_divide));
        int arrive_minute=Integer.parseInt(arrivetime.substring(arrivetime_divide+1,arrivetime.length()));

        int total_minute=arrive_hour*60+arrive_minute-(leave_hour*60+leave_minute);
        int hour=total_minute/60;
        int minute=total_minute%60;

        return hour+"小时"+minute+"分钟";

    }






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);

        //初始化组件
        initViews();

        //接收用户名
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");

        //设置页面跳转
        setPageJump();


        try {
            //开始查询订单
            queryOrder();
        }catch (Exception e){
            AlertDialog.Builder adb=new AlertDialog.Builder(OrderActivity.this);
            adb.setMessage(e.getMessage());
            adb.show();
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
