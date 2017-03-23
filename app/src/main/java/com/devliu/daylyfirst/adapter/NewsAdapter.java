package com.devliu.daylyfirst.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devliu.daylyfirst.R;
import com.devliu.daylyfirst.model.news.NewsResponse;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao
 * on 2017/3/23
 * use to :
 */

public class NewsAdapter extends BaseAdapter {

    public static final int TYPE_1 = 1;
    public static final int TYPE_0 = 0;
    private Context context;
    private List<NewsResponse> newsContentList;

    public NewsAdapter(Context context) {
        this.context = context;
        newsContentList = new ArrayList<>();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        NewsResponse news = newsContentList.get(position);
        if(news.getImgextra()!=null){
            return TYPE_1;
        }else{
            return TYPE_0;
        }
    }

    @Override
    public int getCount() {
        return newsContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderOne holderOne;
        ViewHolderThree holderThree;
        NewsResponse news = newsContentList.get(position);
        switch (getItemViewType(position)){
            case TYPE_0:
                if(convertView==null){
                    convertView = View.inflate(context, R.layout.item_news_one_pic, null);
                    holderOne = new ViewHolderOne();
                    x.view().inject(holderOne, convertView);
                    convertView.setTag(holderOne);
                }else{
                    holderOne = (ViewHolderOne) convertView.getTag();
                }
                holderOne.topic.setText(news.getTitle());
                x.image().bind(holderOne.pic, news.getImgsrc());
                holderOne.source.setText(news.getSource());
                holderOne.commenCount.setText(news.getReplyCount()+"评论");

                break;
            case TYPE_1:
                if(convertView==null){
                    convertView = View.inflate(context, R.layout.item_news_three_pic, null);
                    holderThree = new ViewHolderThree();
                    x.view().inject(holderThree, convertView);
                    convertView.setTag(holderThree);
                }else{
                    holderThree = (ViewHolderThree) convertView.getTag();
                }
                holderThree.topic.setText(news.getTitle());
                x.image().bind(holderThree.picOne, news.getImgsrc());
                x.image().bind(holderThree.picTwo, news.getImgextra().get(0).getImgsrc());
                x.image().bind(holderThree.picThree, news.getImgextra().get(1).getImgsrc());
                holderThree.source.setText(news.getSource());
                holderThree.commenCount.setText(news.getReplyCount()+"评论");
                break;
        }

        return convertView;
    }

    class ViewHolderOne{
        @ViewInject(R.id.item_one_topic)
        TextView topic;
        @ViewInject(R.id.item_one_pic)
        ImageView pic;
        @ViewInject(R.id.item_one_source)
        TextView source;
        @ViewInject(R.id.item_one_comment_count)
        TextView commenCount;
        @ViewInject(R.id.item_one_dislike)
        ImageView dislike;
    }

    class ViewHolderThree{
        @ViewInject(R.id.item_three_topic)
        TextView topic;
        @ViewInject(R.id.item_three_pic_one)
        ImageView picOne;
        @ViewInject(R.id.item_three_pic_two)
        ImageView picTwo;
        @ViewInject(R.id.item_three_pic_three)
        ImageView picThree;
        @ViewInject(R.id.item_three_source)
        TextView source;
        @ViewInject(R.id.item_three_comment_count)
        TextView commenCount;
        @ViewInject(R.id.item_three_dislike)
        ImageView dislike;
    }

    public void setData(List<NewsResponse> data, boolean isRefresh){
        if(isRefresh){
            newsContentList.clear();
        }
        newsContentList.addAll(data);
    }

}
