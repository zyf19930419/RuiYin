package com.tianjin.huanrong.presenter.mine;


import com.tianjin.huanrong.listener.mine.OnSMSListener;
import com.tianjin.huanrong.model.mine.SMSModel;
import com.tianjin.huanrong.view.mine.SMSView;

/**
 * Created by Mr.zhang on 2017/8/11.
 */

public class SMSPresenter implements OnSMSListener {
    SMSView smsView;
    SMSModel smsModel;

    public SMSPresenter(SMSView smsView) {
        this.smsView = smsView;
        smsModel = new SMSModel(this);
    }

    public void sendValidateCode() {
        smsModel.sendValidateCode(smsView.getUserphone());
    }

    /**
     * 校验验证码
     */
    public void checkValidate() {
        smsModel.checkValidate(smsView.getUserphone(), smsView.getValidateCode(), smsView.getSendValidateCode(), smsView.getPassword());
    }

    public void zhuce() {
        smsModel.useRegister(smsView.getUserphone(), smsView.getValidateCode(), smsView.getSendValidateCode(), smsView.getPassword());
    }

    @Override
    public void checkValidateSuccess(String sendMsg) {
        smsView.checkValidateSuccess(sendMsg);
    }

    @Override
    public void failed(String msg) {
        smsView.failed(msg);
    }

    @Override
    public void buttonChange(Boolean clickAble, String showText) {
        smsView.buttonChange(clickAble, showText);
    }

    @Override
    public void resetSuccess() {
        smsView.resetSuccess();
    }

    @Override
    public void registerSuccess() {
        smsView.registerSuccess();
    }
}