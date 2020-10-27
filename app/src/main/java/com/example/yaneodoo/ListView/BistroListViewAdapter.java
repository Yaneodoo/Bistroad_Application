package com.example.yaneodoo.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yaneodoo.Info.Store;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BistroListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<Store> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public BistroListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bistro_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.bistro_imgView);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.bistro_name_txtView);
        TextView locationTextView = (TextView) convertView.findViewById(R.id.bistro_location_txtView);
        TextView descTextView = (TextView) convertView.findViewById(R.id.bistro_desc_txtView);
        CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.checkBox1);
        cb1.setVisibility(View.INVISIBLE);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Store listViewItem = listViewItemList.get(position);

        Bitmap bitmap = null;
        GetImage getStoreImage = new GetImage();
        if(listViewItem.getPhoto()!=null){
            try {
                bitmap = getStoreImage.execute(listViewItem.getPhoto().getThumbnailUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 아이템 내 각 위젯에 데이터 반영
        if(bitmap!=null) iconImageView.setImageBitmap(bitmap);
        else iconImageView.setImageResource(R.drawable.no_image_box);
        titleTextView.setText(listViewItem.getName());
        locationTextView.setText(listViewItem.getAddress());
        descTextView.setText(listViewItem.getDescription());

        if(!listViewItem.isChecked()) convertView.setBackgroundResource(R.drawable.list_item_border);
        else convertView.setBackgroundColor(R.id.dark);

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
    public void addItem(Store store) {
        Store item = store;
        listViewItemList.add(item);
    }

    public void setItem(int position, Store store){
        listViewItemList.set(position, store);
    }
}
