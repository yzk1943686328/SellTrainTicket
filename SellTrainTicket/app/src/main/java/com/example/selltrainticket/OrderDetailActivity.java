package com.example.selltrainticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yzq.zxinglibrary.encode.CodeCreator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailActivity extends AppCompatActivity {



    TextView order_time_view;
    TextView start_station_view;
    TextView leave_time_view;
    TextView trainid_view;
    TextView total_time_view;
    TextView arrive_station_view;
    TextView arrive_time_view;
    TextView leave_date_view;
    TextView passenger_name_view;
    TextView price_view;
    TextView seat_messege_view;
    TextView change_ticket_view;
    TextView refund_ticket_view;
    ImageView back_button;
    Button show_button;



    String order_time;
    String start_station_name;
    String leave_time;
    String trainid;
    String total_time;
    String arrive_station_name;
    String arrive_time;
    String leave_date;
    String passenger_name;
    int price;
    String seat_messege;
    String seat_type;
    int carriage_id;
    int row_id;
    String position;


    SQLiteDatabase db;



    //初始化组件
    public void initViews(){

        order_time_view=findViewById(R.id.order_time_view);
        start_station_view=findViewById(R.id.start_station_view);
        leave_time_view=findViewById(R.id.leave_time_view);
        trainid_view=findViewById(R.id.trainid_view);
        total_time_view=findViewById(R.id.total_time_view);
        arrive_station_view=findViewById(R.id.arrive_station_view);
        arrive_time_view=findViewById(R.id.arrive_time_view);
        leave_date_view=findViewById(R.id.leave_date_view);
        passenger_name_view=findViewById(R.id.passenger_name_view);
        price_view=findViewById(R.id.price_view);
        seat_messege_view=findViewById(R.id.seat_messege_view);
        change_ticket_view=findViewById(R.id.change_ticket_view);
        refund_ticket_view=findViewById(R.id.refund_ticket_view);
        back_button=findViewById(R.id.back_button);
        show_button=findViewById(R.id.show_button);

    }


    //接收传来的属性值
    public void acceptValues(){

        Bundle bundle=getIntent().getExtras();
        order_time=bundle.getString("order_time");
        start_station_name=bundle.getString("start_station_name");
        leave_time=bundle.getString("leave_time");
        trainid=bundle.getString("trainid");
        total_time=bundle.getString("total_time");
        arrive_station_name=bundle.getString("arrive_station_name");
        arrive_time=bundle.getString("arrive_time");
        leave_date=bundle.getString("leave_date");
        passenger_name=bundle.getString("passenger_name");
        price=bundle.getInt("price");
        seat_type=bundle.getString("seat_type");
        carriage_id=bundle.getInt("carriage_id");
        row_id=bundle.getInt("row_id");
        position=bundle.getString("position");

        seat_messege=seat_type+" "+carriage_id+"车 "+row_id+position+"号";


    }


    //给组件设置值
    public void setValues(){

        order_time_view.setText("下单时间"+order_time);
        start_station_view.setText(start_station_name);
        leave_time_view.setText(leave_time);
        trainid_view.setText(trainid);
        total_time_view.setText(total_time);
        arrive_station_view.setText(arrive_station_name);
        arrive_time_view.setText(arrive_time);
        leave_date_view.setText("发车时间"+leave_date);
        passenger_name_view.setText(passenger_name);
        price_view.setText("¥ "+price);
        seat_messege_view.setText(seat_messege);


    }



    //设置改签
    public void setChangeTicket(){

        change_ticket_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //弹出提示框
                AlertDialog.Builder adb=new AlertDialog.Builder(OrderDetailActivity.this);
                adb.setTitle("提示");
                adb.setMessage("您确定要改签吗?");
                adb.setNegativeButton("取消",null);
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //首先将该订单从订单表中删除
                        db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);//打开数据库
                        String deleteSql="delete from orderstable where username='"+passenger_name+"' and ordertime='"+order_time+"'";
                        db.execSQL(deleteSql);


                        //恢复用户余额

                        //获取用户当前余额
                        String money_sql="select * from usermessege where username='"+passenger_name+"'";
                        Cursor money_result=db.rawQuery(money_sql,null);
                        money_result.moveToFirst();
                        int old_money=money_result.getInt(3);

                        //更改余额
                        int new_money=old_money+price;
                        String new_money_sql="update usermessege set money="+new_money+" where username='"+passenger_name+"'";
                        db.execSQL(new_money_sql);




                        //然后跳转到查询界面并传递相应的数据
                        Intent intent=new Intent(OrderDetailActivity.this,QueryActivity.class);

                        Bundle bundle=new Bundle();
                        bundle.putString("username",passenger_name);
                        bundle.putString("start_city_name",start_station_name.substring(0,2));
                        bundle.putString("arrive_city_name",arrive_station_name.substring(0,2));
                        bundle.putString("time",leave_date);
                        bundle.putBoolean("only",true);
                        intent.putExtras(bundle);

                        startActivity(intent);


                    }
                });


                adb.show();




            }
        });

    }



    //设置退票
    public void setRefundTicket(){

        refund_ticket_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //弹出提示框
                AlertDialog.Builder adb=new AlertDialog.Builder(OrderDetailActivity.this);
                adb.setTitle("提示");
                adb.setMessage("您确定要退票吗?");
                adb.setNegativeButton("取消",null);
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //将该订单从订单表中删除
                        db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);//打开数据库
                        String deleteSql="delete from orderstable where username='"+passenger_name+"' and ordertime='"+order_time+"'";
                        db.execSQL(deleteSql);



                        //恢复用户余额

                        //获取用户当前余额
                        String money_sql="select * from usermessege where username='"+passenger_name+"'";
                        Cursor money_result=db.rawQuery(money_sql,null);
                        money_result.moveToFirst();
                        int old_money=money_result.getInt(3);

                        //更改余额(距离发车多于两天全额退款，不足两天扣除10%)

                        try {


                            //获取当前时间
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            String current_time = df.format(new Date());// new Date()为获取当前系统时间
                            String s[] = current_time.split("-");
                            int current_month = Integer.parseInt(s[1]);
                            int current_day = Integer.parseInt(s[2]);

                            //获取发车时间
                            String l_date = leave_date.substring(0, leave_date.length() - 6);
                            String s2[] = l_date.split("月");
                            int leave_month = Integer.parseInt(s2[0]);
                            int leave_day = Integer.parseInt(s2[1]);

                            int new_money;
                            if ((leave_month * 30 + leave_day) - (current_month * 30 + current_day) >= 2) {
                                new_money = old_money + price;
                            } else {
                                new_money = old_money + price * 9 / 10;
                            }


                            String new_money_sql = "update usermessege set money=" + new_money + " where username='" + passenger_name + "'";
                            db.execSQL(new_money_sql);


                        }catch (Exception e){
                            Toast.makeText(OrderDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }


                        //提示用户订单已删除
                        Toast.makeText(OrderDetailActivity.this,"订单已删除!",Toast.LENGTH_SHORT);



                        //跳转到订单界面并传递用户名
                        Intent intent=new Intent(OrderDetailActivity.this,OrderActivity.class);

                        Bundle bundle=new Bundle();
                        bundle.putString("username",passenger_name);
                        intent.putExtras(bundle);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                        startActivity(intent);



                    }
                });


                adb.show();

            }
        });

    }




    //设置返回
    public void setBack(){

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击返回按钮之后跳转到订单界面并传递用户名
                Intent intent=new Intent(OrderDetailActivity.this,OrderActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",passenger_name);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);

            }
        });

    }



    //设置显示二维码
    public void setShowQRCode(){

        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击按钮之后显示二维码


                //生成字符串并AES加密
                String s=passenger_name+leave_date+trainid+seat_type+row_id+position+order_time;
                String ss=AES.encode("4166b0828a80cc1b37e5e3731b176f50",s);


                //生成二维码
                Bitmap bitmap= CodeCreator.createQRCode(ss,600,600,null);
                ImageView qrimage=new ImageView(OrderDetailActivity.this);
                qrimage.setImageBitmap(bitmap);

                AlertDialog.Builder adb=new AlertDialog.Builder(OrderDetailActivity.this);
                adb.setView(qrimage);
                adb.show();


            }
        });

    }







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_page);


        //初始化组件
        initViews();


        try {
            //接收传来的属性值
            acceptValues();

            //给组件设置值
            setValues();

        }catch (Exception e){
            AlertDialog.Builder adb=new AlertDialog.Builder(OrderDetailActivity.this);
            adb.setMessage(e.getMessage());
            adb.show();
        }


        //设置改签
        setChangeTicket();


        //设置退票
        setRefundTicket();


        //设置返回
        setBack();


        //设置显示二维码
        setShowQRCode();



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
