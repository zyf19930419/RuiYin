package com.example.shichang393.ruiyin.model;

import com.example.shichang393.ruiyin.ApiService.StrategyService;
import com.example.shichang393.ruiyin.Bean.StrategyBean;
import com.example.shichang393.ruiyin.Bean.StrategyPostData;
import com.example.shichang393.ruiyin.listener.StrategyListener;
import com.example.shichang393.ruiyin.utils.ConstanceValue;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mr.zhang on 2017/7/18.
 */

public class StrategyModel {
    StrategyListener listener;

    public StrategyModel(StrategyListener listener) {
        this.listener = listener;
    }

    public  void postData(String zhiboshiid){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ConstanceValue.bannerurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        StrategyService strategyService = retrofit.create(StrategyService.class);
        Call<StrategyBean> poststrategy = strategyService.poststrategy(new StrategyPostData(zhiboshiid));
        poststrategy.enqueue(new Callback<StrategyBean>() {
            @Override
            public void onResponse(Call<StrategyBean> call, Response<StrategyBean> response) {
                StrategyBean body = response.body();
                if (body==null){return;}
                List<StrategyBean.DataBean.PROPOSALBean> proposal = body.getData().getPROPOSAL();
                if (proposal==null){return;}
                if (proposal.size()>0){
                    listener.success(proposal);
                }else {
                    listener.failed("暂无策略");
                }
            }

            @Override
            public void onFailure(Call<StrategyBean> call, Throwable t) {
                listener.failed("数据解析失败");
            }
        });

    }
}
