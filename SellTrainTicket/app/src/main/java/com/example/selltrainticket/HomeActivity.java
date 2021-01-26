package com.example.selltrainticket;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ywp.addresspickerlib.AddressPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity {

    TextView start_city;
    TextView arrive_city;
    TextView time_view;
    Switch onlyGD;
    Button search_button;
    TextView clear_history;
    ImageView order_image;
    ImageView my_image;
    LinearLayout jiemian;
    AddressPickerView apv;

    String username;
    String start_city_name;
    String arrive_city_name;
    String time;
    Boolean only;


    Calendar currentDate;

    String city;




    public void initViews(){
        start_city=findViewById(R.id.start_city);
        arrive_city=findViewById(R.id.arrive_city);
        time_view=findViewById(R.id.time_view);
        onlyGD=findViewById(R.id.onlyGD);
        search_button=findViewById(R.id.search_button);
        clear_history=findViewById(R.id.clean_history);
        order_image=findViewById(R.id.order_image);
        my_image=findViewById(R.id.my_image);

        jiemian=findViewById(R.id.jiemian);

    }

    //设置页面跳转
    public void setPageJump(){
        order_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到订单页面并传递用户名
                Intent intent=new Intent(HomeActivity.this,OrderActivity.class);

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
                Intent intent=new Intent(HomeActivity.this,MyActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                startActivity(intent);
            }
        });
    }

    //设置选择城市
    public void setSelectCity(){
        start_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //弹出省市区选择器
                apv=new AddressPickerView(HomeActivity.this);
                jiemian.addView(apv);

                apv.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
                    @Override
                    public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {

                        String addr[]=address.split(" ");

                        if(addr[0].equals("北京市")||addr[0].equals("上海市")||addr[0].equals("重庆市")||addr[0].equals("天津市")){

                            //选择四大直辖市
                            city=addr[0].substring(0,2);
                            start_city.setText(city);

                        }
                        else{

                            //选择其他省的城市
                            city=addr[1];

                            if(city.endsWith("市")){
                                city=city.substring(0,city.length()-1);
                            }

                            start_city.setText(city);


                        }

                        jiemian.removeView(apv);

                    }
                });


            }
        });

        arrive_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //弹出省市区选择器
                apv=new AddressPickerView(HomeActivity.this);
                jiemian.addView(apv);

                apv.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
                    @Override
                    public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {

                        String addr[]=address.split(" ");

                        if(addr[0].equals("北京市")||addr[0].equals("上海市")||addr[0].equals("重庆市")||addr[0].equals("天津市")){

                            //选择四大直辖市
                            city=addr[0].substring(0,2);
                            arrive_city.setText(city);

                        }
                        else{

                            //选择其他省的城市
                            city=addr[1];

                            if(city.endsWith("市")){
                                city=city.substring(0,city.length()-1);
                            }

                            arrive_city.setText(city);


                        }


                        jiemian.removeView(apv);

                    }
                });


            }
        });

    }


    //设置选择日期
    public void setSelectDate(){

        //设置初始日期
        currentDate= Calendar.getInstance(Locale.CHINA);

        int currentMonth=currentDate.get(Calendar.MONTH)+1;
        int currentDay=currentDate.get(Calendar.DAY_OF_MONTH);
        int week=currentDate.get(Calendar.DAY_OF_WEEK);
        String nums[]={"日","一","二","三","四","五","六"};
        String currentTime=currentMonth+"月"+currentDay+"日"+"   "+"周"+nums[week-1];


        time_view.setText(currentTime);

        //点击后弹出日期选择器
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取当前时间
                currentDate= Calendar.getInstance(Locale.CHINA);

                //弹出日期选择器
                new DatePickerDialog(HomeActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                            // 绑定监听器(How the parent is notified that the date is set.)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // 此处得到选择的时间，可以进行你想要的操作


                        //根据日期计算周几
                        int week=DateToWeek(year,monthOfYear+1,dayOfMonth);
                        String nums[]={"日","一","二","三","四","五","六"};
                        String date=(monthOfYear+1)+"月"+dayOfMonth+"日"+"   "+"周"+nums[week-1];

                        time_view.setText(date);


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

    //执行查询操作
    public void Search(){

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取开始城市，到达城市，时间，是否勾选只看高铁/动车
                start_city_name=start_city.getText().toString();
                arrive_city_name=arrive_city.getText().toString();
                time=time_view.getText().toString();
                only=onlyGD.isChecked();

                //跳转到查询界面并传递信息
                Intent intent=new Intent(HomeActivity.this,QueryActivity.class);

                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                bundle.putString("start_city_name",start_city_name);
                bundle.putString("arrive_city_name",arrive_city_name);
                bundle.putString("time",time);
                bundle.putBoolean("only",only);

                intent.putExtras(bundle);

                startActivity(intent);



            }
        });
    }




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        initViews();

        //接收用户名
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");


        //设置页面跳转
        setPageJump();


        //设置选择城市
        setSelectCity();

        //设置选择日期
        setSelectDate();


        //设置查询
        Search();




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
