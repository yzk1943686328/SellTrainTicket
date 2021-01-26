package com.example.selltrainticket;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

public class TrainManageActivity extends AppCompatActivity {


    LinearLayout jiemian;
    RadioGroup operate_type;
    RadioButton add_train;
    RadioButton delete_train;
    RadioButton see_train;
    LinearLayout set_train_id;
    EditText train_id_view;
    LinearLayout set_train_name;
    EditText train_name_view;
    LinearLayout select_train_type;
    RadioGroup train_type;
    LinearLayout set_leave_date;
    EditText leave_date_view;
    LinearLayout set_pass_station;
    LinearLayout pass_station_view;
    Button add_station_button;
    Button exec_button;
    ImageView order_query_image;
    ImageView user_manage_image;




    String  username;
    SQLiteDatabase db;




    //初始化组件
    public void initViews(){

        jiemian=findViewById(R.id.jiemian);
        operate_type=findViewById(R.id.operate_type);
        add_train=findViewById(R.id.add_train);
        delete_train=findViewById(R.id.delete_train);
        see_train=findViewById(R.id.see_train);
        set_train_id=findViewById(R.id.set_train_id);
        train_id_view=findViewById(R.id.train_id_view);
        set_train_name=findViewById(R.id.set_train_name);
        train_name_view=findViewById(R.id.train_name_view);
        select_train_type=findViewById(R.id.select_train_type);
        train_type=findViewById(R.id.train_type);
        set_leave_date=findViewById(R.id.set_leave_date);
        leave_date_view=findViewById(R.id.leave_date_view);
        set_pass_station=findViewById(R.id.set_pass_station);
        pass_station_view=findViewById(R.id.pass_station_view);
        add_station_button=findViewById(R.id.add_station_button);
        exec_button=findViewById(R.id.exec_button);
        order_query_image=findViewById(R.id.order_query_image);
        user_manage_image=findViewById(R.id.user_manage_image);

    }


    //设置页面跳转
    public void setPageJump(){
        order_query_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到订单查询页面并传递用户名
                Intent intent=new Intent(TrainManageActivity.this,OrderQueryActivity.class);

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
                Intent intent=new Intent(TrainManageActivity.this,UserManageActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);
            }
        });
    }



    //设置操作转换
    public void setOperateChange(){

        add_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //选择增加列车后显示隐藏选项
                set_train_id.setVisibility(View.VISIBLE);
                set_train_name.setVisibility(View.VISIBLE);
                select_train_type.setVisibility(View.VISIBLE);
                set_leave_date.setVisibility(View.VISIBLE);
                set_pass_station.setVisibility(View.VISIBLE);
                pass_station_view.removeAllViews();
                pass_station_view.setVisibility(View.VISIBLE);


            }
        });


        delete_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //选择删除列车之后隐藏部分选项
                set_train_id.setVisibility(View.VISIBLE);
                set_train_name.setVisibility(View.INVISIBLE);
                select_train_type.setVisibility(View.INVISIBLE);
                set_leave_date.setVisibility(View.INVISIBLE);
                set_pass_station.setVisibility(View.INVISIBLE);
                pass_station_view.setVisibility(View.INVISIBLE);

            }
        });


        see_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //选择查看列车之后隐藏部分选项
                set_train_id.setVisibility(View.INVISIBLE);
                set_train_name.setVisibility(View.INVISIBLE);
                select_train_type.setVisibility(View.INVISIBLE);
                set_leave_date.setVisibility(View.INVISIBLE);
                set_pass_station.setVisibility(View.INVISIBLE);

            }
        });

    }



    //设置选择日期
    public void setSelectDate(){


        //点击后弹出日期选择器
        leave_date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取当前时间
                Calendar currentDate= Calendar.getInstance(Locale.CHINA);

                //弹出日期选择器
                new DatePickerDialog(TrainManageActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器(How the parent is notified that the date is set.)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作


                        //根据日期计算周几
                        int week=DateToWeek(year,monthOfYear+1,dayOfMonth);
                        String nums[]={"日","一","二","三","四","五","六"};
                        String date=(monthOfYear+1)+"月"+dayOfMonth+"日"+"   "+"周"+nums[week-1];

                        leave_date_view.setText(date);


                    }

                }
                        // 设置初始日期
                        , currentDate.get(Calendar.YEAR)
                        , currentDate.get(Calendar.MONTH)
                        , currentDate.get(Calendar.DAY_OF_MONTH)).show();

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



    //设置添加经停站
    public void setAddStation(){

        add_station_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    //建立经停站填写的View
                    LinearLayout station_messege=new LinearLayout(TrainManageActivity.this);
                    station_messege.setOrientation(LinearLayout.VERTICAL);
                    station_messege.setGravity(View.TEXT_ALIGNMENT_CENTER);


                    LinearLayout city_name=new LinearLayout(TrainManageActivity.this);
                    city_name.setOrientation(LinearLayout.HORIZONTAL);
                    city_name.setTop(10);
                    TextView cn=new TextView(TrainManageActivity.this);
                    cn.setText("城市名：");
                    cn.setTextSize(20);
                    city_name.addView(cn);
                    final EditText city_name_view=new EditText(TrainManageActivity.this);
                    city_name_view.setWidth(200);
                    city_name.addView(city_name_view);



                    LinearLayout station_name=new LinearLayout(TrainManageActivity.this);
                    station_name.setOrientation(LinearLayout.HORIZONTAL);
                    station_name.setTop(10);
                    TextView sn=new TextView(TrainManageActivity.this);
                    sn.setText("车站名：");
                    sn.setTextSize(20);
                    station_name.addView(sn);
                    final EditText station_name_view=new EditText(TrainManageActivity.this);
                    station_name_view.setWidth(200);
                    station_name.addView(station_name_view);



                    LinearLayout arrive_time=new LinearLayout(TrainManageActivity.this);
                    arrive_time.setOrientation(LinearLayout.HORIZONTAL);
                    arrive_time.setTop(10);
                    TextView at=new TextView(TrainManageActivity.this);
                    at.setText("到达时间：");
                    at.setTextSize(20);
                    arrive_time.addView(at);
                    final EditText arrive_time_view=new EditText(TrainManageActivity.this);
                    arrive_time_view.setWidth(200);
                    arrive_time.addView(arrive_time_view);



                    LinearLayout leave_time=new LinearLayout(TrainManageActivity.this);
                    leave_time.setOrientation(LinearLayout.HORIZONTAL);
                    leave_time.setTop(10);
                    TextView lt=new TextView(TrainManageActivity.this);
                    lt.setText("离开时间：");
                    lt.setTextSize(20);
                    leave_time.addView(lt);
                    final EditText leave_time_view=new EditText(TrainManageActivity.this);
                    leave_time_view.setWidth(200);
                    leave_time.addView(leave_time_view);



                    LinearLayout first_price=new LinearLayout(TrainManageActivity.this);
                    first_price.setOrientation(LinearLayout.HORIZONTAL);
                    first_price.setTop(10);
                    TextView fp=new TextView(TrainManageActivity.this);
                    fp.setText("一等座价格：");
                    fp.setTextSize(20);
                    first_price.addView(fp);
                    final EditText first_price_view=new EditText(TrainManageActivity.this);
                    first_price_view.setWidth(200);
                    first_price.addView(first_price_view);



                    LinearLayout second_price=new LinearLayout(TrainManageActivity.this);
                    second_price.setOrientation(LinearLayout.HORIZONTAL);
                    second_price.setTop(10);
                    TextView sp=new TextView(TrainManageActivity.this);
                    sp.setText("二等座价格：");
                    sp.setTextSize(20);
                    second_price.addView(sp);
                    final EditText second_price_view=new EditText(TrainManageActivity.this);
                    second_price_view.setWidth(200);
                    second_price.addView(second_price_view);


                    station_messege.addView(city_name);
                    station_messege.addView(station_name);
                    station_messege.addView(arrive_time);
                    station_messege.addView(leave_time);
                    station_messege.addView(first_price);
                    station_messege.addView(second_price);



                    AlertDialog.Builder adb =new AlertDialog.Builder(TrainManageActivity.this);
                    adb.setTitle("填写经停站信息");
                    adb.setView(station_messege);



                    adb.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //点击添加后将信息显示在界面上

                            //获取输入的值
                            String c_name=city_name_view.getText().toString();
                            String s_name=station_name_view.getText().toString();
                            String a_time=arrive_time_view.getText().toString();
                            String l_time=leave_time_view.getText().toString();
                            String f_price=first_price_view.getText().toString();
                            String s_price=second_price_view.getText().toString();


                            //将值显示在界面上
                            TextView tv=new TextView(TrainManageActivity.this);
                            tv.setText(c_name+" "+s_name+" "+a_time+" "+l_time+" "+f_price+" "+s_price);
                            tv.setTextSize(20);


                            pass_station_view.addView(tv);



                        }
                    });
                    adb.setNegativeButton("取消",null);


                    //弹出框
                    adb.show();

                }catch (Exception e){
                    Toast.makeText(TrainManageActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    //设置执行
    public void setExec(){

        exec_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击执行按钮后执行增加或删除列车的操作

                String  train_id=train_id_view.getText().toString();

                if(operate_type.getCheckedRadioButtonId()==R.id.add_train){

                    //增加列车


                    //清空输入
                    operate_type.clearCheck();
                    train_id_view.setText("");
                    train_name_view.setText("");
                    train_type.clearCheck();
                    leave_date_view.setText("");

                    pass_station_view.removeAllViews();


                    //提示
                    AlertDialog.Builder adb=new AlertDialog.Builder(TrainManageActivity.this);
                    adb.setTitle("提示");
                    adb.setMessage("您已成功添加"+train_id+"次列车");
                    adb.show();


                }else if(operate_type.getCheckedRadioButtonId()==R.id.see_train){

                    //查看列车信息
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);

                    String see_sql="select * from trainmessege";
                    Cursor result=db.rawQuery(see_sql,null);
                    result.moveToFirst();
                    int count=666;
                    while(result.isAfterLast()==false){

                        //读取信息
                        String t_id=result.getString(1);
                        String t_name=result.getString(2);
                        String t_type=result.getString(3);
                        String startstation=result.getString(4);
                        String arrivestation=result.getString(5);

                        TextView tv=new TextView(TrainManageActivity.this);
                        tv.setText(t_id+" "+t_name+" "+t_type+" "+startstation+" "+arrivestation);
                        tv.setTextSize(20);
                        pass_station_view.addView(tv);


                        TextView tv2=new TextView(TrainManageActivity.this);
                        tv2.setText("G"+count+" "+t_name+" "+t_type+" "+arrivestation+" "+startstation);
                        tv2.setTextSize(20);
                        pass_station_view.addView(tv2);
                        count++;

                        result.moveToNext();

                    }


                }else{

                    //删除列车


                    //打开数据库
                    db=openOrCreateDatabase("selltrainticket",MODE_PRIVATE,null);


                    //删除该列车的所有信息(包括车次表，经停站表，座位表，价格表)
                    String delete_sql_1="delete from trainmessege where trainid='"+train_id+"'";
                    String delete_sql_2="delete from trainpassstation where trainid='"+train_id+"'";
                    String delete_sql_3="delete from trainseat where trainid='"+train_id+"'";
                    String delete_sql_4="delete from trainticketprice where trainid='"+train_id+"'";
                    db.execSQL(delete_sql_1);
                    db.execSQL(delete_sql_2);
                    db.execSQL(delete_sql_3);
                    db.execSQL(delete_sql_4);


                    //清空输入
                    operate_type.clearCheck();
                    train_id_view.setText("");



                    //提示管理员删除已完成
                    AlertDialog.Builder adb=new AlertDialog.Builder(TrainManageActivity.this);
                    adb.setTitle("提示");
                    adb.setMessage("您已成功删除"+train_id+"次列车");
                    adb.show();

                }

            }
        });

    }




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_manage_page);


        //初始化组件
        initViews();


        //接收用户名
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");


        //设置页面跳转
        setPageJump();


        //设置操作类型转换
        setOperateChange();



        //设置选择日期
        setSelectDate();



        try {
            //设置添加经停站
            setAddStation();
        }catch (Exception e){
            Toast.makeText(TrainManageActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        //设置执行
        setExec();



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
