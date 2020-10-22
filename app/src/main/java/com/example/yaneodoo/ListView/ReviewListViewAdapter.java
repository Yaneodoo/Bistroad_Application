package com.example.yaneodoo.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yaneodoo.Info.Review;
import com.example.yaneodoo.R;
import com.example.yaneodoo.REST.GetImage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ReviewListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Review> listViewItemList = new ArrayList<>() ;

    // ListViewAdapter의 생성자
    public ReviewListViewAdapter() {
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

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView writerImageView = (ImageView) convertView.findViewById(R.id.review_writer_imageView);
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.review_imgView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.review_date_txtView);
        TextView writerTextView = (TextView) convertView.findViewById(R.id.review_writer_txtView);
        TextView scoreTextView=(TextView) convertView.findViewById(R.id.review_score_txtView);
        TextView reviewTextView=(TextView) convertView.findViewById(R.id.review_text_txtView);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Review listViewItem = listViewItemList.get(position);

        Bitmap bitmap = null;
        GetImage getReviewImage = new GetImage();
        if(listViewItem.getPhoto()!=null){
            try {
                bitmap = getReviewImage.execute(listViewItem.getPhoto().getSourceUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Bitmap nbitmap = null;
        GetImage getWriterImage = new GetImage();
        if(listViewItem.getWriter().getPhoto()!=null){
            try {
                nbitmap = getWriterImage.execute(listViewItem.getWriter().getPhoto().getThumbnailUrl()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 아이템 내 각 위젯에 데이터 반영
        if(bitmap!=null) iconImageView.setImageBitmap(bitmap);
        if(nbitmap!=null) writerImageView.setImageBitmap(nbitmap);
        dateTextView.setText(listViewItem.getTimestamp().substring(0,10));
        writerTextView.setText(listViewItem.getWriter().getFullName());
        scoreTextView.setText("★"+listViewItem.getStars());
        reviewTextView.setText(listViewItem.getContents());

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
    public void addItem(Review review) {
        Review item =review;
        Log.d("ITEM",item.toString());
        listViewItemList.add(item);
    }
}
