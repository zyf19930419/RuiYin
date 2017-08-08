package com.example.shichang393.ruiyin.presenter;

import com.example.shichang393.ruiyin.Bean.IptMsgBean;
import com.example.shichang393.ruiyin.listener.OnIptMsgListener;
import com.example.shichang393.ruiyin.model.IptMsgModel;
import com.example.shichang393.ruiyin.view.IptMsgView;

import java.util.List;

/**
 * Created by Mr.zhang on 2017/6/27.
 */

public class IptMsgPresenter implements OnIptMsgListener {
    IptMsgView view;
    IptMsgModel model;

    public IptMsgPresenter(IptMsgView view) {
        this.view = view;
        model=new IptMsgModel(this);
    }

    public void getData(String id,int index, int size){
        model.getData(id,index,size);
    }
    @Override
    public void success(List<IptMsgBean> list) {
        view.success(list);
    }

    @Override
    public void faild(String message) {
        view.faild(message);
    }
}
