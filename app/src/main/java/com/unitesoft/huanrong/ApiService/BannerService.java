package com.unitesoft.huanrong.ApiService;


import com.unitesoft.huanrong.Bean.BannerBean;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Mr.zhang on 2017/6/27.
 */

public interface BannerService {
    @POST("appsv/liveRoomListView/getLunBo.do")
    Call<BannerBean> postService();
}
