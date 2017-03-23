package com.devliu.daylyfirst.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.devliu.daylyfirst.R;
import com.devliu.daylyfirst.model.video.VideoResponse;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by liuhao
 * on 2017/3/21
 * use to :
 */

public class VideoAdapter extends BaseAdapter {
    private List<VideoResponse> videoList;
    private Context context;

    public VideoAdapter(Context context) {
        videoList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VideoResponse video = videoList.get(position);
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_video, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        JCVideoPlayerStandard jcVideoPlayerStandard = holder.player;
        jcVideoPlayerStandard.setUp(
                video.getMp4_url(),
                JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                video.getTitle());
        x.image().bind(jcVideoPlayerStandard.thumbImageView, video.getCover());

        return convertView;
    }

    class ViewHolder{
        @ViewInject(R.id.item_video)
        JCVideoPlayerStandard player;
    }

    public void setData(List<VideoResponse> data, boolean isRefresh){
        if(isRefresh){
            videoList.clear();
        }
        videoList.addAll(data);
    }

}
