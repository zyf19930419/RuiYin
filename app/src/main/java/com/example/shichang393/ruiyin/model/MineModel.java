package com.example.shichang393.ruiyin.model;

import android.text.TextUtils;

import com.example.shichang393.ruiyin.ApiService.MineService;
import com.example.shichang393.ruiyin.Bean.LoginBean;
import com.example.shichang393.ruiyin.Bean.LoginPostBean;
import com.example.shichang393.ruiyin.listener.OnLoginListener;
import com.example.shichang393.ruiyin.manager.SharedPreferencesMgr;
import com.example.shichang393.ruiyin.utils.CommonUtil;
import com.example.shichang393.ruiyin.utils.ConstanceValue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mr.zhang on 2017/8/10.
 */

public class MineModel {
    OnLoginListener onLoginListener;

    public MineModel(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    /**
     * 实盘登录
     *
     * @param zhanghao
     * @param mima
     */
    public void login(final String zhanghao, final String mima) {
        onLoginListener.showProgress();
        if (TextUtils.isEmpty(zhanghao)) {
            onLoginListener.showMsg("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mima)) {
            onLoginListener.showMsg("密码不能为空");
            return;
        }
        Retrofit retrofit = CommonUtil.retrofit(ConstanceValue.testurl);
        MineService mineService = retrofit.create(MineService.class);
        final Call<LoginBean> login = mineService.login(new LoginPostBean(zhanghao, mima));
        login.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                if (loginBean!=null) {
                    LoginBean.DataBean dataBean = loginBean.getData();
                    if (dataBean != null) {
                        int code = dataBean.getBusinesscode();
                        if (103 == code) {
                            LoginBean.DataBean.UsersBean usersBean = dataBean.getUsers();
                            if (usersBean != null) {
                                SharedPreferencesMgr.setuserid(usersBean.getYonghuid());
                                SharedPreferencesMgr.saveUserIcon(usersBean.getYonghutouxiang());
                                SharedPreferencesMgr.saveUserMark(usersBean.getYonghubiaozhu());
                                int zhanghaoleixing = usersBean.getZhanghaoleixing();
                                SharedPreferencesMgr.setZhanghaoleixing(zhanghaoleixing);
                                String username = isGeneral(zhanghaoleixing) ? usersBean.getYonghunicheng() : usersBean.getYonghuxingming();
                                SharedPreferencesMgr.saveUsername(username);
                                SharedPreferencesMgr.saveUserActivation(usersBean.getShifoushijihuoyonghu());
                                SharedPreferencesMgr.saveZhanghao(zhanghao);
                                SharedPreferencesMgr.saveMima(mima);
                            }
                        }
                        onLoginListener.loginsuccess(dataBean);
                    } else {
                        onLoginListener.loginfailed("数据解析失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                onLoginListener.loginfailed("数据解析失败");
            }
        });

    }

    /**
     * 是否是普通用户
     *
     * @param permission 用户权限
     * @return
     */
    private boolean isGeneral(int permission) {
        if (2 == permission || 3 == permission || 4 == permission || 5 == permission || 6 == permission) {
            return false;
        }
        return true;
    }
}
