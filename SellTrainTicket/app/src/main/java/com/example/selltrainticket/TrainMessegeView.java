package com.example.selltrainticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class TrainMessegeView extends LinearLayout {

    String username;
    SQLiteDatabase db;


    TextView trainid_view;
    TextView start_station_view;
    TextView leave_time_view;
    TextView arrive_station_view;
    TextView arrive_time_view;
    TextView total_time_view;
    TextView first_seat_number_view;
    TextView first_seat_price_view;
    TextView second_seat_number_view;
    TextView second_seat_price_view;

    Button first_seat_buy_button;
    Button second_seat_buy_button;



    String time;
    String trainid;
    String start_station_name;
    int start_station_id;
    String arrive_station_name;
    int arrive_station_id;
    String leavetime;
    String arrivetime;
    int FirstSeatNumber;
    int SecondSeatNumber;
    int FirstSeatPrice;
    int SecondSeatPrice;


    //初始化各组件
    public void initViews(){
        trainid_view=findViewById(R.id.trainid_view);
        start_station_view=findViewById(R.id.start_station_view);
        leave_time_view=findViewById(R.id.leave_time_view);
        arrive_station_view=findViewById(R.id.arrive_station_view);
        arrive_time_view=findViewById(R.id.arrive_time_view);
        total_time_view=findViewById(R.id.total_time_view);
        first_seat_number_view=findViewById(R.id.first_seat_number_view);
        first_seat_price_view=findViewById(R.id.first_seat_price_view);
        second_seat_number_view=findViewById(R.id.second_seat_number_view);
        second_seat_price_view=findViewById(R.id.second_seat_price_view);

        first_seat_buy_button=findViewById(R.id.first_seat_buy_button);
        second_seat_buy_button=findViewById(R.id.second_seat_buy_button);

    }


    //初始化各变量
    public void initValues(String time,String trainid, String start_station_name,int start_station_id, String arrive_station_name,int arrive_station_id, String leavetime, String arrivetime,int FirstSeatNumber, int SecondSeatNumber,int FirstSeatPrice, int SecondSeatPrice,String username,SQLiteDatabase db){
        this.time=time;
        this.trainid=trainid;
        this.start_station_name=start_station_name;
        this.start_station_id=start_station_id;
        this.arrive_station_name=arrive_station_name;
        this.arrive_station_id=arrive_station_id;
        this.leavetime=leavetime;
        this.arrivetime=arrivetime;
        this.FirstSeatNumber=FirstSeatNumber;
        this.SecondSeatNumber=SecondSeatNumber;
        this.FirstSeatPrice=FirstSeatPrice;
        this.SecondSeatPrice=SecondSeatPrice;

        this.username=username;
        this.db=db;

    }


    //给组件设置值
    public void setValues(){
        //给组件设置值
        trainid_view.setText(trainid);
        start_station_view.setText(start_station_name);
        leave_time_view.setText(leavetime);
        arrive_station_view.setText(arrive_station_name);
        arrive_time_view.setText(arrivetime);

        total_time_view.setText(calculateTotalTime(leavetime,arrivetime));

        first_seat_price_view.setText(FirstSeatPrice+"");
        second_seat_price_view.setText(SecondSeatPrice+"");

        if(FirstSeatNumber>=10){
            first_seat_number_view.setText("有");
        }else{
            first_seat_number_view.setText(FirstSeatNumber+"张");
        }


        if(SecondSeatNumber>=10){
            second_seat_number_view.setText("有");
        }else{
            second_seat_number_view.setText(SecondSeatNumber+"张");
        }

    }


    //设置购买事件监听器
    public void setBuyListener(){

        first_seat_buy_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //首先判断是否有余票
                if(FirstSeatNumber<=0){
                    //没有余票
                }else{
                    //有余票


                    //弹出选择框让用户选择座位

                    //建立选择器
                    final RadioGroup seat_position=new RadioGroup(getContext());
                    seat_position.setOrientation(HORIZONTAL);

                    RadioButton AButton=new RadioButton(getContext());
                    AButton.setText("A");
                    AButton.setId(R.id.AButton);
                    RadioButton BButton=new RadioButton(getContext());
                    BButton.setId(R.id.BButton);
                    BButton.setText("B");
                    RadioButton CButton=new RadioButton(getContext());
                    CButton.setId(R.id.CButton);
                    CButton.setText("C");
                    RadioButton DButton=new RadioButton(getContext());
                    DButton.setId(R.id.DButton);
                    DButton.setText("D");
                    RadioButton FButton=new RadioButton(getContext());
                    FButton.setId(R.id.FButton);
                    FButton.setText("F");


                    seat_position.addView(AButton);
                    seat_position.addView(BButton);
                    seat_position.addView(CButton);
                    seat_position.addView(DButton);
                    seat_position.addView(FButton);


                    AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                    adb.setTitle("请选择座位的位置");
                    adb.setView(seat_position);
                    adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //点击确定后开始购票


                            //获取用户选择的位置
                            String position_detail=getResources().getResourceName(seat_position.getCheckedRadioButtonId());
                            String position=position_detail.substring(position_detail.length()-7,position_detail.length()-6);

                            //查询用户选择的位置是否还有余票
                            String FirstSeatSql0="select * from trainseat where trainid='"+trainid+"' and seattype='一等座' and position='"+position+"' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                            Cursor result0=db.rawQuery(FirstSeatSql0,null);

                            if(result0.getCount()>0){

                                //所选类型的座位还有余票,在该类型的座位中随机选一个


                                //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                Random random=new Random();
                                int rn=random.nextInt(result0.getCount())+1;


                                String FirstSeatSql="select * from trainseat where trainid='"+trainid+"' and seattype='一等座' and position='"+position+"' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                                Cursor fc=db.rawQuery(FirstSeatSql,null);
                                fc.moveToFirst();

                                int current=1;
                                while(current<rn){
                                    fc.moveToNext();
                                    current++;
                                }

                                //获取选中座位的信息
                                int rowid=fc.getInt(3);
                                int from=fc.getInt(6);
                                int to=fc.getInt(7);


                                //修改该座位的信息
                                int new_from=Math.min(from,start_station_id);
                                int new_to=Math.max(to,arrive_station_id);
                                String updateSql1="update trainseat set fromstation="+new_from+" where trainid='"+trainid+"' and seattype='一等座' and rowid="+rowid+" and position='"+position+"'";
                                String updateSql2="update trainseat set tostation="+new_to+" where trainid='"+trainid+"' and seattype='一等座' and rowid="+rowid+" and position='"+position+"'";
                                db.execSQL(updateSql1);
                                db.execSQL(updateSql2);


                                //添加订单
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                String ordertime=df.format(new Date());// 获取当前系统时间(下单时间)

                                String orderSql="insert into orderstable values('"+time+"','"+username+"','"+trainid+"','"+start_station_name+"','"+leavetime+"','"+arrive_station_name+"','"+arrivetime+"',1,"+rowid+",'"+position+"','一等座',"+FirstSeatPrice+",'"+ordertime+"')";
                                db.execSQL(orderSql);


                                //扣除用户余额

                                //获取用户当前余额
                                String money_sql="select * from usermessege where username='"+username+"'";
                                Cursor money_result=db.rawQuery(money_sql,null);
                                money_result.moveToFirst();
                                int old_money=money_result.getInt(3);

                                //更改余额
                                int new_money=old_money-FirstSeatPrice;
                                String new_money_sql="update usermessege set money="+new_money+" where username='"+username+"'";
                                db.execSQL(new_money_sql);



                                //提示用户购买成功
                                AlertDialog.Builder adb2=new AlertDialog.Builder(getContext());
                                adb2.setTitle("购买成功提醒");
                                adb2.setMessage("恭喜你成功购买了"+time+"从"+start_station_name+"开往"+arrive_station_name+"的"+trainid+"次列车一等座,您的座位为1车厢"+rowid+position);
                                adb2.show();

                            }
                            else{

                                //所选座位无余票,在所有座位中随机选一个

                                //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                Random random=new Random();
                                int rn=random.nextInt(FirstSeatNumber)+1;


                                String FirstSeatSql="select * from trainseat where trainid='"+trainid+"' and seattype='一等座' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                                Cursor fc=db.rawQuery(FirstSeatSql,null);
                                fc.moveToFirst();


                                int current=1;
                                while(current<rn){
                                    fc.moveToNext();
                                    current++;
                                }

                                //获取选中座位的信息
                                int rowid=fc.getInt(3);
                                String position2=fc.getString(4);
                                int from=fc.getInt(6);
                                int to=fc.getInt(7);


                                //修改该座位的信息
                                int new_from=Math.min(from,start_station_id);
                                int new_to=Math.max(to,arrive_station_id);
                                String updateSql1="update trainseat set fromstation="+new_from+" where trainid='"+trainid+"' and seattype='一等座' and rowid="+rowid+" and position='"+position2+"'";
                                String updateSql2="update trainseat set tostation="+new_to+" where trainid='"+trainid+"' and seattype='一等座' and rowid="+rowid+" and position='"+position2+"'";
                                db.execSQL(updateSql1);
                                db.execSQL(updateSql2);


                                //添加订单
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                String ordertime=df.format(new Date());// 获取当前系统时间(下单时间)

                                String orderSql="insert into orderstable values('"+time+"','"+username+"','"+trainid+"','"+start_station_name+"','"+leavetime+"','"+arrive_station_name+"','"+arrivetime+"',1,"+rowid+",'"+position2+"','一等座',"+FirstSeatPrice+",'"+ordertime+"')";
                                db.execSQL(orderSql);



                                //扣除用户余额

                                //获取用户当前余额
                                String money_sql="select * from usermessege where username='"+username+"'";
                                Cursor money_result=db.rawQuery(money_sql,null);
                                money_result.moveToFirst();
                                int old_money=money_result.getInt(3);

                                //更改余额
                                int new_money=old_money-FirstSeatPrice;
                                String new_money_sql="update usermessege set money="+new_money+" where username='"+username+"'";
                                db.execSQL(new_money_sql);



                                //提示用户购买成功
                                AlertDialog.Builder adb2=new AlertDialog.Builder(getContext());
                                adb2.setTitle("购买成功提醒");
                                adb2.setMessage("恭喜你成功购买了"+time+"从"+start_station_name+"开往"+arrive_station_name+"的"+trainid+"次列车一等座,您的座位为1车厢"+rowid+position2);
                                adb2.show();

                            }

                        }
                    });
                    adb.setNegativeButton("取消",null);
                    adb.show();

                }


            }
        });


        second_seat_buy_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //首先判断是否有余票
                if(SecondSeatNumber<=0){
                    //没有余票
                }else{
                    //有余票


                    //弹出选择框让用户选择座位

                    //建立选择器
                    final RadioGroup seat_position=new RadioGroup(getContext());
                    seat_position.setOrientation(HORIZONTAL);

                    RadioButton AButton=new RadioButton(getContext());
                    AButton.setText("A");
                    AButton.setId(R.id.AButton);
                    RadioButton BButton=new RadioButton(getContext());
                    BButton.setId(R.id.BButton);
                    BButton.setText("B");
                    RadioButton CButton=new RadioButton(getContext());
                    CButton.setId(R.id.CButton);
                    CButton.setText("C");
                    RadioButton DButton=new RadioButton(getContext());
                    DButton.setId(R.id.DButton);
                    DButton.setText("D");
                    RadioButton FButton=new RadioButton(getContext());
                    FButton.setId(R.id.FButton);
                    FButton.setText("F");


                    seat_position.addView(AButton);
                    seat_position.addView(BButton);
                    seat_position.addView(CButton);
                    seat_position.addView(DButton);
                    seat_position.addView(FButton);


                    AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                    adb.setTitle("请选择座位的位置");
                    adb.setView(seat_position);
                    adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //点击确定后开始购票


                            //获取用户选择的位置
                            String position_detail=getResources().getResourceName(seat_position.getCheckedRadioButtonId());
                            String position=position_detail.substring(position_detail.length()-7,position_detail.length()-6);

                            //查询用户选择的位置是否还有余票
                            String FirstSeatSql0="select * from trainseat where trainid='"+trainid+"' and seattype='二等座' and position='"+position+"' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                            Cursor result0=db.rawQuery(FirstSeatSql0,null);

                            if(result0.getCount()>0){

                                //所选类型的座位还有余票,在该类型的座位中随机选一个


                                //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                Random random=new Random();
                                int rn=random.nextInt(result0.getCount())+1;


                                String FirstSeatSql="select * from trainseat where trainid='"+trainid+"' and seattype='二等座' and position='"+position+"' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                                Cursor fc=db.rawQuery(FirstSeatSql,null);
                                fc.moveToFirst();

                                int current=1;
                                while(current<rn){
                                    fc.moveToNext();
                                    current++;
                                }

                                //获取选中座位的信息
                                int rowid=fc.getInt(3);
                                int from=fc.getInt(6);
                                int to=fc.getInt(7);


                                //修改该座位的信息
                                int new_from=Math.min(from,start_station_id);
                                int new_to=Math.max(to,arrive_station_id);
                                String updateSql1="update trainseat set fromstation="+new_from+" where trainid='"+trainid+"' and seattype='二等座' and rowid="+rowid+" and position='"+position+"'";
                                String updateSql2="update trainseat set tostation="+new_to+" where trainid='"+trainid+"' and seattype='二等座' and rowid="+rowid+" and position='"+position+"'";
                                db.execSQL(updateSql1);
                                db.execSQL(updateSql2);


                                //添加订单
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                String ordertime=df.format(new Date());// 获取当前系统时间(下单时间)

                                String orderSql="insert into orderstable values('"+time+"','"+username+"','"+trainid+"','"+start_station_name+"','"+leavetime+"','"+arrive_station_name+"','"+arrivetime+"',2,"+rowid+",'"+position+"','二等座',"+SecondSeatPrice+",'"+ordertime+"')";
                                db.execSQL(orderSql);



                                //扣除用户余额

                                //获取用户当前余额
                                String money_sql="select * from usermessege where username='"+username+"'";
                                Cursor money_result=db.rawQuery(money_sql,null);
                                money_result.moveToFirst();
                                int old_money=money_result.getInt(3);

                                //更改余额
                                int new_money=old_money-SecondSeatPrice;
                                String new_money_sql="update usermessege set money="+new_money+" where username='"+username+"'";
                                db.execSQL(new_money_sql);



                                //提示用户购买成功
                                AlertDialog.Builder adb2=new AlertDialog.Builder(getContext());
                                adb2.setTitle("购买成功提醒");
                                adb2.setMessage("恭喜你成功购买了"+time+"从"+start_station_name+"开往"+arrive_station_name+"的"+trainid+"次列车二等座,您的座位为2车厢"+rowid+position);
                                adb2.show();


                            }
                            else{

                                //所选座位无余票,在所有座位中随机选一个

                                //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                Random random=new Random();
                                int rn=random.nextInt(SecondSeatNumber)+1;


                                String FirstSeatSql="select * from trainseat where trainid='"+trainid+"' and seattype='二等座' and (tostation<="+start_station_id+" or fromstation>="+arrive_station_id+")";
                                Cursor fc=db.rawQuery(FirstSeatSql,null);
                                fc.moveToFirst();


                                int current=1;
                                while(current<rn){
                                    fc.moveToNext();
                                    current++;
                                }

                                //获取选中座位的信息
                                int rowid=fc.getInt(3);
                                String position2=fc.getString(4);
                                int from=fc.getInt(6);
                                int to=fc.getInt(7);


                                //修改该座位的信息
                                int new_from=Math.min(from,start_station_id);
                                int new_to=Math.max(to,arrive_station_id);
                                String updateSql1="update trainseat set fromstation="+new_from+" where trainid='"+trainid+"' and seattype='二等座' and rowid="+rowid+" and position='"+position2+"'";
                                String updateSql2="update trainseat set tostation="+new_to+" where trainid='"+trainid+"' and seattype='二等座' and rowid="+rowid+" and position='"+position2+"'";
                                db.execSQL(updateSql1);
                                db.execSQL(updateSql2);


                                //添加订单
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                String ordertime=df.format(new Date());// 获取当前系统时间(下单时间)

                                String orderSql="insert into orderstable values('"+time+"','"+username+"','"+trainid+"','"+start_station_name+"','"+leavetime+"','"+arrive_station_name+"','"+arrivetime+"',1,"+rowid+",'"+position2+"','二等座',"+SecondSeatPrice+",'"+ordertime+"')";
                                db.execSQL(orderSql);



                                //扣除用户余额

                                //获取用户当前余额
                                String money_sql="select * from usermessege where username='"+username+"'";
                                Cursor money_result=db.rawQuery(money_sql,null);
                                money_result.moveToFirst();
                                int old_money=money_result.getInt(3);

                                //更改余额
                                int new_money=old_money-SecondSeatPrice;
                                String new_money_sql="update usermessege set money="+new_money+" where username='"+username+"'";
                                db.execSQL(new_money_sql);


                                //提示用户购买成功
                                AlertDialog.Builder adb2=new AlertDialog.Builder(getContext());
                                adb2.setTitle("购买成功提醒");
                                adb2.setMessage("恭喜你成功购买了"+time+"从"+start_station_name+"开往"+arrive_station_name+"的"+trainid+"次列车二等座,您的座位为2车厢"+rowid+position2);
                                adb2.show();




                            }





                        }
                    });
                    adb.setNegativeButton("取消",null);
                    adb.show();

                }


            }
        });


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




    public TrainMessegeView(Context context, @Nullable AttributeSet attrs,String time,String trainid, String start_station_name,int start_station_id, String arrive_station_name, int arrive_station_id,String leavetime, String arrivetime,int FirstSeatNumber, int SecondSeatNumber,int FirstSeatPrice, int SecondSeatPrice,String username,SQLiteDatabase db) {
        super(context, attrs);

        //设置布局
        LayoutInflater.from(context).inflate(R.layout.train_messege_view,this);

        //初始化组件
        initViews();

        //初始化变量
        initValues(time,trainid,start_station_name,start_station_id,arrive_station_name,arrive_station_id,leavetime,arrivetime,FirstSeatNumber,SecondSeatNumber,FirstSeatPrice,SecondSeatPrice,username,db);

        //给组件设置值
        setValues();


        try {
            //设置购买事件监听器
            setBuyListener();
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }
}
