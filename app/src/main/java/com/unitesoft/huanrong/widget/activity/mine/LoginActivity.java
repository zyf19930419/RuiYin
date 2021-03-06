package com.unitesoft.huanrong.widget.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitesoft.huanrong.Bean.LoginBean;
import com.unitesoft.huanrong.R;
import com.unitesoft.huanrong.manager.SharedPreferencesMgr;
import com.unitesoft.huanrong.presenter.LoginPresenter;
import com.unitesoft.huanrong.utils.ToastUtils;
import com.unitesoft.huanrong.view.mine.LoginView;
import com.unitesoft.huanrong.widget.activity.MainActivity;
import com.unitesoft.huanrong.widget.view.sweetdialog.SweetAlertDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @InjectView(R.id.btn_login_exit)
    ImageButton btnLoginExit;
    @InjectView(R.id.login_edit_number)
    EditText loginEditNumber;
    @InjectView(R.id.login_edit_password)
    EditText loginEditPassword;
    @InjectView(R.id.login_text_tishi)
    TextView loginTextTishi;
    @InjectView(R.id.btn_login_denglu)
    Button btnLoginDenglu;
    @InjectView(R.id.btn_login_rem)
    Button btnLoginRem;
    @InjectView(R.id.btn_login_zhuce)
    Button btnLoginZhuce;
    LoginPresenter loginPresenter;
    @InjectView(R.id.image_eye)
    ImageView imageEye;

    private SweetAlertDialog sweetAlertDialog;

    private boolean change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        change = getIntent().getBooleanExtra("ischange", false);
        loginEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    imageEye.setVisibility(View.GONE);
                } else {
                    imageEye.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_login_exit, R.id.btn_login_denglu, R.id.btn_login_rem, R.id.btn_login_zhuce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            退出
            case R.id.btn_login_exit:
                finish();
                break;
//            登录
            case R.id.btn_login_denglu:
                showProgress();
                loginPresenter = new LoginPresenter(this);
                loginPresenter.login();
                break;
//            忘记密码
            case R.id.btn_login_rem:
                RegisterActivity.startIntent(LoginActivity.this, 0);
                break;
//            注册
            case R.id.btn_login_zhuce:
                RegisterActivity.startIntent(LoginActivity.this, 1);
                break;
        }
    }

    public static void startIntent(Context mContext, boolean ischange) {
        Intent intent = new Intent();
        intent.putExtra("ischange", ischange);
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public String zhanghao() {
        String zhanghao = loginEditNumber.getText().toString().trim();
        return zhanghao;
    }

    @Override
    public String mima() {
        String mima = loginEditPassword.getText().toString().trim();
        return mima;
    }

    @Override
    public void loginsuccess(LoginBean.DataBean dataBean) {

        int code = dataBean.getBusinesscode();
//        103实盘用户  109游客  106 重置密码成功  102用户不存在  104密码错误
        if (103 == code || 100 == code || 106 == code) {
            SharedPreferencesMgr.isShipan(true);
            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText("登录成功");
            sweetAlertDialog.showConfirmButton(false);
            sweetAlertDialog.setDialogCloseListener(new SweetAlertDialog.DialogCloseListener() {
                @Override
                public void dialogClose() {
                    dismissProgress();
                    Intent intent = new Intent();
                    intent.putExtra("change", change);
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else if (109 == code) {
            SharedPreferencesMgr.isShipan(false);
            loginPresenter.visitorLogin();
        } else if (104 == code) {
            dismissProgress();
            ToastUtils.showToast(LoginActivity.this, "密码错误");
        } else if (102 == code) {
            dismissProgress();
            loginTextTishi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void visitorloginsuccess(LoginBean.DataBean dataBean) {
        int businesscode = dataBean.getBusinesscode();
        if (103 == businesscode || 100 == businesscode || 106 == businesscode) {
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
                SharedPreferencesMgr.saveZhanghao(loginEditNumber.getText().toString().trim());
                SharedPreferencesMgr.saveMima(loginEditPassword.getText().toString().trim());
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("登录成功");
                sweetAlertDialog.showConfirmButton(false);
                sweetAlertDialog.setDialogCloseListener(new SweetAlertDialog.DialogCloseListener() {
                    @Override
                    public void dialogClose() {
                        dismissProgress();
                        Intent intent = new Intent();
                        intent.putExtra("change", change);
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        } else if (104 == businesscode) {
            dismissProgress();
            ToastUtils.showToast(LoginActivity.this, "密码错误");
        } else if (102 == businesscode) {
            dismissProgress();
            loginTextTishi.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void loginfailed(String msg) {
        dismissProgress();
        ToastUtils.showToast(LoginActivity.this, msg);
    }

    @Override
    public void showProgress() {
        if (null == sweetAlertDialog) {
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        } else {
            sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        }
        sweetAlertDialog.setTitleText("登录中...");
        sweetAlertDialog.show();
    }

    private void dismissProgress() {
        if (null != sweetAlertDialog && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
    }
}
