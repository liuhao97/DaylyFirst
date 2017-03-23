package com.devliu.daylyfirst.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.devliu.daylyfirst.R;
import com.devliu.daylyfirst.adapter.NewsAdapter;
import com.devliu.daylyfirst.base.BaseFragment;
import com.devliu.daylyfirst.model.news.NewsResponse;
import com.devliu.daylyfirst.net.DataInterface;
import com.devliu.daylyfirst.net.RequestXUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by liuhao
 * on 2017/3/22
 * use to :
 */
@ContentView(R.layout.frag_news)
public class NewsFrag extends BaseFragment {

    @ViewInject(R.id.main_news_list)
    PullToRefreshListView videoList;
    private int num;
    private int number;
    private Boolean isRefresh;
    private NewsAdapter adapter;
    private DataInterface<NewsResponse> dataInterface;
    private String pathid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        initVariables();
        initViews();
        return contentView;
    }

    private void initVariables() {
        num = 0;
        number = 20;
        isRefresh = false;
        Bundle arguments = getArguments();
        pathid = arguments.getString("keyurl");
        adapter = new NewsAdapter(getContext());

        dataInterface = new DataInterface<NewsResponse>() {
            @Override
            public void setdata(List<NewsResponse> t) {
                adapter.setData(t, isRefresh);
                adapter.notifyDataSetChanged();
                videoList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videoList.onRefreshComplete();
                    }
                }, 1200);
            }
        };

    }

    private String getPath() {
        return "http://c.m.163.com/nc/article/headline/"+pathid+"/"+num+"-"+number+".html";
    }

    private void initViews() {
        videoList.setMode(PullToRefreshBase.Mode.BOTH);
        videoList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                loadMore();
            }
        });
        videoList.setAdapter(adapter);
    }

    private void loadMore() {
        num+=20;
        number+=20;
        isRefresh=false;
        RequestXUtils.utils(getPath(), NewsResponse.class,dataInterface);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    private void refresh(){
        num=0;
        number=20;
        isRefresh=true;
        RequestXUtils.utils(getPath(), NewsResponse.class,dataInterface);
    }

    public void autoRefresh(){

    }

}
