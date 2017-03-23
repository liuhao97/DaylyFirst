package com.devliu.daylyfirst.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.devliu.daylyfirst.R;
import com.devliu.daylyfirst.adapter.VideoAdapter;
import com.devliu.daylyfirst.base.BaseFragment;
import com.devliu.daylyfirst.model.video.VideoResponse;
import com.devliu.daylyfirst.net.DataInterface;
import com.devliu.daylyfirst.net.RequestXUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by liuhao
 * on 2017/3/21
 * use to :
 */
@ContentView(R.layout.frag_video)
public class VideoFrag extends BaseFragment{

    @ViewInject(R.id.main_video_list)
    PullToRefreshListView videoList;
    private int num;
    private int number;
    private Boolean isRefresh;
    private VideoAdapter adapter;
    private DataInterface<VideoResponse> dataInterface;
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
        num = 1;
        number = 10;
        isRefresh = false;
        Bundle arguments = getArguments();
        pathid = arguments.getString("keyurl");
        adapter = new VideoAdapter(getContext());

        dataInterface = new DataInterface<VideoResponse>() {
            @Override
            public void setdata(List<VideoResponse> t) {
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
        return "http://c.3g.163.com/nc/video/list/"+pathid+"/n/"+num+"-"+number+".html";
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
        num+=10;
        number+=10;
        isRefresh=false;
        RequestXUtils.utils(getPath(), VideoResponse.class,dataInterface);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void refresh(){
        num=1;
        number=10;
        isRefresh=true;
        RequestXUtils.utils(getPath(), VideoResponse.class,dataInterface);
    }

    public void autoRefresh(){

    }

}
