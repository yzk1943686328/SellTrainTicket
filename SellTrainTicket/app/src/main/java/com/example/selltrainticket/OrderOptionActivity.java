package com.example.selltrainticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class OrderOptionActivity extends LinearLayout{

    TextView trinid_view;
    TextView start_arrive_view;
    TextView leavetime_view;
    TextView seat_view;
    TextView passenger_view;
    LinearLayout option_view;


    String username;
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

    Context orderContext;


    //初始化组件
    public void initViews(){
        trinid_view=findViewById(R.id.trainid_view2);
        start_arrive_view=findViewById(R.id.start_arrive_view2);
        leavetime_view=findViewById(R.id.leavetime_view2);
        seat_view=findViewById(R.id.seat_view2);
        passenger_view=findViewById(R.id.passenger_view2);
        option_view=findViewById(R.id.option_view2);
    }


    //初始化变量
    public void initValues(String username,String time,String trainid,String startstationname,String leavetime,String arrivestationname,String arrivetime,int carriageid,int rowid,String position,String seattype,int price,String ordertime,Context orderContext){
        this.username=username;
        this.time=time;
        this.trainid=trainid;
        this.startstationname=startstationname;
        this.leavetime=leavetime;
        this.arrivestationname=arrivestationname;
        this.arrivetime=arrivetime;
        this.carriageid=carriageid;
        this.rowid=rowid;
        this.position=position;
        this.seattype=seattype;
        this.price=price;
        this.ordertime=ordertime;
        this.orderContext=orderContext;

    }


    //给组件设置值
    public void setValues(){

        trinid_view.setText(trainid);
        start_arrive_view.setText(startstationname+" 一 "+arrivestationname);
        leavetime_view.setText("发车时间："+time+" "+leavetime+"开");
        seat_view.setText("座        次："+carriageid+"车厢 "+rowid+position+" "+seattype);
        passenger_view.setText("乘  客  名："+username);


    }









    public OrderOptionActivity(Context context, @Nullable AttributeSet attrs,String username,String time,String trainid,String startstationname,String leavetime,String arrivestationname,String arrivetime,int carriageid,int rowid,String position,String seattype,int price,String ordertime,Context orderContext) {
        super(context, attrs);

        //设置布局
        LayoutInflater.from(context).inflate(R.layout.order_option_view,this);

        //初始化组件
        initViews();


        //初始化变量
        initValues(username,time,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime,orderContext);


        //给组件设置值
        setValues();







    }
}
