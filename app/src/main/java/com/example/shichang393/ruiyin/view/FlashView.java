package com.example.shichang393.ruiyin.view;

import com.example.shichang393.ruiyin.Bean.FlashBean;

import java.util.List;

/**
 * Created by Mr.zhang on 2017/6/23.
 */

public interface FlashView {
    void success(List<FlashBean.DataBean.DangtianshujuBean> list);
    void failed(String errormessage);
}
