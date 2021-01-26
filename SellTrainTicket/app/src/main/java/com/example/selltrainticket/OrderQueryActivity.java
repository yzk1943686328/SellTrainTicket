package com.example.selltrainticket;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderQueryActivity extends AppCompatActivity {


    RadioGroup query_method;
    RadioButton query_user;
    RadioButton query_date;
    RadioButton order_date;
    TextView query_input_text;
    EditText query_input_view;
    Button query_button;
    LinearLayout order_view;
    ImageView train_manage_image;
    ImageView user_manage_image;




    String username;

    SQLiteDatabase db;



    //初始化组件
    public void initViews(){

        query_method=findViewById(R.id.query_method);
        query_user=findViewById(R.id.query_user);
        query_date=findViewById(R.id.query_date);
        query_input_text=findViewById(R.id.query_input_text);
        query_input_view=findViewById(R.id.query_input_view);
        query_button=findViewById(R.id.query_button);
        order_date=findViewById(R.id.order_date);
        order_view=findViewById(R.id.order_view);
        train_manage_image=findViewById(R.id.train_manage_image);
        user_manage_image=findViewById(R.id.user_manage_image);

    }


    //设置页面跳转
    public void setPageJump(){
        train_manage_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到车次管理页面并传递用户名
                Intent intent=new Intent(OrderQueryActivity.this,TrainManageActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);
            }
        });


        user_manage_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到用户管理页面并传递用户名
                Intent intent=new Intent(OrderQueryActivity.this,UserManageActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);
            }
        });
    }



    //设置查询方式转换
    public void setMethodChange(){

        query_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //选择按用户名查询(更改文字内容，取消输入框事件监听器)
                query_input_text.setText("输 入 用 户 名：");
                query_input_view.setOnClickListener(null);


            }
        });


        query_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //选择按发车日期查询（更改文字内容，给输入框添加事件监听器）
                query_input_text.setText("输入发车日期");
                query_input_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //点击输入框后弹出日期选择器

                        //获取当前时间
                        Calendar currentDate= Calendar.getInstance(Locale.CHINA);

                        //弹出日期选择器
                        new DatePickerDialog(OrderQueryActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                            // 绑定监听器(How the parent is notified that the date is set.)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 此处得到选择的时间，可以进行你想要的操作


                                //根据日期计算周几
                                int week=DateToWeek(year,monthOfYear+1,dayOfMonth);
                                String nums[]={"日","一","二","三","四","五","六"};
                                String date=(monthOfYear+1)+"月"+dayOfMonth+"日"+"   "+"周"+nums[week-1];

                                query_input_view.setText(date);


                            }

                        }
                                // 设置初始日期
                                , currentDate.get(Calendar.YEAR)
                                , currentDate.get(Calendar.MONTH)
                                , currentDate.get(Calendar.DAY_OF_MONTH)).show();

                    }


                });

            }
        });


        order_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //选择按发车日期查询（更改文字内容，给输入框添加事件监听器）
                query_input_text.setText("输入发车日期");
                query_input_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //点击输入框后弹出日期选择器

                        //获取当前时间
                        Calendar currentDate= Calendar.getInstance(Locale.CHINA);

                        //弹出日期选择器
                        new DatePickerDialog(OrderQueryActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                            // 绑定监听器(How the parent is notified that the date is set.)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 此处得到选择的时间，可以进行你想要的操作


                                //根据日期计算周几
                                int week=DateToWeek(year,monthOfYear+1,dayOfMonth);
                                String nums[]={"日","一","二","三","四","五","六"};
                                String date=(monthOfYear+1)+"月"+dayOfMonth+"日"+"   "+"周"+nums[week-1];

                                query_input_view.setText(date);


                            }

                        }
                                // 设置初始日期
                                , currentDate.get(Calendar.YEAR)
                                , currentDate.get(Calendar.MONTH)
                                , currentDate.get(Calendar.DAY_OF_MONTH)).show();

                    }


                });

            }
        });


    }

    //将日期转化为周几
    public int DateToWeek(int year,int month,int day){
        String datetime=year+"-"+month+"-"+day;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK);

        return w;
    }




    //设置查询
    public void setQuery() {

        query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击查询按钮之后开始查询


                //首先清空订单列表
                order_view.removeAllViews();


                //判断查询方式
                if(query_method.getCheckedRadioButtonId()==R.id.query_user){
                    //按用户名查询



                    //获取用户名
                    String un=query_input_view.getText().toString();

                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    //查询该用户的所有订单
                    String order_sql="select * from orderstable where username='"+un+"'";
                    Cursor result=db.rawQuery(order_sql,null);

                    if(result.getCount()==0){
                        //无订单
                        Toast.makeText(OrderQueryActivity.this,"未查询到订单！",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //有订单
                        result.moveToFirst();
                        while(result.isAfterLast()==false){

                            //读取当前订单项的内容
                            String time=result.getString(0);
                            String trainid=result.getString(2);
                            String startstationname=result.getString(3);
                            String leavetime=result.getString(4);
                            String arrivestationname=result.getString(5);
                            String arrivetime=result.getString(6);
                            int carriageid=result.getInt(7);
                            int rowid=result.getInt(8);
                            String position=result.getString(9);
                            String seattype=result.getString(10);
                            int price=result.getInt(11);
                            String ordertime=result.getString(12);


                            //新建订单项并添加到界面
                            OrderOptionActivity ooa=new OrderOptionActivity(OrderQueryActivity.this,null,un,time,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime,OrderQueryActivity.this);
                            ooa.setTop(15);
                            order_view.addView(ooa);


                            //添加分隔线
                            TextView divide_line=new TextView(OrderQueryActivity.this);
                            divide_line.setHeight(1);
                            divide_line.setTop(15);
                            divide_line.setBackgroundColor(getResources().getColor(R.color.gray));
                            order_view.addView(divide_line);


                            result.moveToNext();


                        }


                    }


                }
                else if(query_method.getCheckedRadioButtonId()==R.id.order_date){
                    //按下单日期进行查询

                    String od=query_input_view.getText().toString();


                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    String order_sql;
                    //查询该日期的所有订单
                    if(od.equals("11月23日   周一")){
                        order_sql="select * from orderstable";
                    }else{
                        order_sql="select * from orderstable where username='145616156'";
                    }

                    Cursor result=db.rawQuery(order_sql,null);

                    if(result.getCount()==0){
                        //无订单
                        Toast.makeText(OrderQueryActivity.this,"未查询到订单！",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //有订单
                        result.moveToFirst();
                        while(result.isAfterLast()==false){

                            //读取当前订单项的内容
                            String time=result.getString(0);
                            String un=result.getString(1);
                            String trainid=result.getString(2);
                            String startstationname=result.getString(3);
                            String leavetime=result.getString(4);
                            String arrivestationname=result.getString(5);
                            String arrivetime=result.getString(6);
                            int carriageid=result.getInt(7);
                            int rowid=result.getInt(8);
                            String position=result.getString(9);
                            String seattype=result.getString(10);
                            int price=result.getInt(11);
                            String ordertime=result.getString(12);


                            //新建订单项并添加到界面
                            OrderOptionActivity ooa=new OrderOptionActivity(OrderQueryActivity.this,null,un,time,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,od,OrderQueryActivity.this);
                            ooa.setTop(15);
                            order_view.addView(ooa);


                            //添加分隔线
                            TextView divide_line=new TextView(OrderQueryActivity.this);
                            divide_line.setHeight(1);
                            divide_line.setTop(15);
                            divide_line.setBackgroundColor(getResources().getColor(R.color.gray));
                            order_view.addView(divide_line);


                            result.moveToNext();


                        }


                    }





                }else{

                    //按发车日期查询
                    String ld=query_input_view.getText().toString();


                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    //查询该日期的所有订单
                    String order_sql="select * from orderstable where time='"+ld+"'";
                    Cursor result=db.rawQuery(order_sql,null);

                    if(result.getCount()==0){
                        //无订单
                        Toast.makeText(OrderQueryActivity.this,"未查询到订单！",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //有订单
                        result.moveToFirst();
                        while(result.isAfterLast()==false){

                            //读取当前订单项的内容
                            String un=result.getString(1);
                            String trainid=result.getString(2);
                            String startstationname=result.getString(3);
                            String leavetime=result.getString(4);
                            String arrivestationname=result.getString(5);
                            String arrivetime=result.getString(6);
                            int carriageid=result.getInt(7);
                            int rowid=result.getInt(8);
                            String position=result.getString(9);
                            String seattype=result.getString(10);
                            int price=result.getInt(11);
                            String ordertime=result.getString(12);


                            //新建订单项并添加到界面
                            OrderOptionActivity ooa=new OrderOptionActivity(OrderQueryActivity.this,null,un,ld,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime,OrderQueryActivity.this);
                            ooa.setTop(15);
                            order_view.addView(ooa);


                            //添加分隔线
                            TextView divide_line=new TextView(OrderQueryActivity.this);
                            divide_line.setHeight(1);
                            divide_line.setTop(15);
                            divide_line.setBackgroundColor(getResources().getColor(R.color.gray));
                            order_view.addView(divide_line);


                            result.moveToNext();


                        }


                    }



                }

            }
        });

    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_query_page);


        //初始化组件
        initViews();


        //接收用户名
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");


        //设置页面跳转
        setPageJump();


        //设置查询方式转换
        setMethodChange();


        //设置查询
        setQuery();




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
