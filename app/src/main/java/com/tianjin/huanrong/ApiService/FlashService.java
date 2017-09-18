package com.tianjin.huanrong.ApiService;


import com.tianjin.huanrong.Bean.FlashBean;
import com.tianjin.huanrong.Bean.FlashPostData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Mr.zhang on 2017/6/23.
 * 财经快讯
 */

public interface FlashService {
    /**
     * http://zb.006006.cn:10001/appsv/financialNewsView/getData.do
     */
    @POST("appsv/financialNewsView/getData.do")
    Call<FlashBean> getBody(@Body FlashPostData data);

}