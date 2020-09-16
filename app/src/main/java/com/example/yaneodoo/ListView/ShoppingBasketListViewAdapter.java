package com.example.yaneodoo.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yaneodoo.R;

import java.util.ArrayList;

public class ShoppingBasketListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ShoppingBasketListViewItem> listViewItemList = new ArrayList<>();
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener;

    // ListViewAdapter의 생성자
    public ShoppingBasketListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴 : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shoppingbasket_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameTextView = (TextView) convertView.findViewById(R.id.menu_name_txtView);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.menu_price_txtView);
        //TextView quantityTextView = (TextView) convertView.findViewById(R.id.menu_quantity_txtView);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ShoppingBasketListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getNameStr());
        priceTextView.setText(listViewItem.getPriceStr());
        //quantityTextView.setText(listViewItem.getAmount());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String name, String price) {
        ShoppingBasketListViewItem item = new ShoppingBasketListViewItem();

        item.setNameStr(name);
        item.setPriceStr(price);
        item.setAmount(1);

        listViewItemList.add(item);
    }

    public void deleteItem(int position) {
        listViewItemList.remove(position);
    }

    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.
    public interface ListBtnClickListener {
        void onListBtnClick(int position);
    }
}