package com.example.yaneodoo.ListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;

import com.example.yaneodoo.Customer.MyPageCustomer;
import com.example.yaneodoo.Customer.MyPageLeaveReview;
import com.example.yaneodoo.Info.Menu;
import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Request;
import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderListViewItem> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public OrderListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴 : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수 구현
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ToggleButton iconImageView = (ToggleButton) convertView.findViewById(R.id.btn_progress);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.order_date_txtView);
        TextView customerTextView = (TextView) convertView.findViewById(R.id.order_customer_txtView);
        TextView tableNumTextView = (TextView) convertView.findViewById(R.id.tableNum_txtView);
        TextView orderTextView = (TextView) convertView.findViewById(R.id.order_list);
        TextView stateTextView = (TextView) convertView.findViewById(R.id.order_progress);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        OrderListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setBackgroundDrawable(listViewItem.getProgress());
        dateTextView.setText(listViewItem.getDateStr());
        customerTextView.setText(listViewItem.getNameStr());
        if(listViewItem.getRole().equals("ROLE_STORE_OWNER"))
            tableNumTextView.setText("테이블 번호 : "+listViewItem.getTableNum());
        else
            if(listViewItem.getOrder().getHasReview())
                tableNumTextView.setText("리뷰 유무 : 유");
            else
                tableNumTextView.setText("리뷰 유무 : 무");

        orderTextView.setText(listViewItem.getOrderStr());
        stateTextView.setText(listViewItem.getStateStr());
        if(listViewItem.getRole().equals("ROLE_STORE_OWNER") || listViewItem.getStateStr().equals("접수중"))
            iconImageView.setFocusable(true);
        else
            iconImageView.setFocusable(false);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addItem(String role, Order order, Drawable photo, String date, String customer, String sorder, String state, String orderId, Integer tableNum) {
        OrderListViewItem item = new OrderListViewItem();
        item.setProgress(photo);
        item.setDateStr(date);
        item.setNameStr(customer);
        item.setOrderStr(sorder);
        item.setStateStr(state);
        item.setOrderNum(orderId);
        item.setTableNum(tableNum);
        item.setOrder(order);
        item.setRole(role);

        listViewItemList.add(item);
    }

    public void setItem(int position, Drawable progress, String state){
        listViewItemList.get(position).setProgress(progress);
        listViewItemList.get(position).setStateStr(state);
    }
}