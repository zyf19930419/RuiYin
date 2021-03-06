package com.unitesoft.huanrong.widget.fragment.live;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unitesoft.huanrong.Bean.StrategyBean;
import com.unitesoft.huanrong.R;
import com.unitesoft.huanrong.manager.SharedPreferencesMgr;
import com.unitesoft.huanrong.presenter.StrategyPresenter;
import com.unitesoft.huanrong.utils.ToastUtils;
import com.unitesoft.huanrong.view.StrategyView;
import com.unitesoft.huanrong.widget.adapter.live.StrategyAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * 策略
 */
public class StrategyFragment extends BaseFragment implements StrategyView {


    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    StrategyPresenter presenter;
    StrategyAdapter adapter;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    LinearLayoutManager manager;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_strategy, container, false);
            isPrepared = true;
            ButterKnife.inject(this, view);
            initview();
            lazyLoad();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }
    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        presenter = new StrategyPresenter(this);
        String id = SharedPreferencesMgr.getZhiboshiid();
        presenter.postData(id);
    }

    private void initview() {
         manager= new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void success(List<StrategyBean.DataBean.PROPOSALBean> list) {
        if (adapter==null){
            adapter=new StrategyAdapter(list);
        }else {
            adapter.notifyDataSetChanged();
        }
        recyclerview.setAdapter(adapter);
        recyclerview.scrollToPosition(list.size()-1);
    }

    @Override
    public void failed(String msg) {
        ToastUtils.showToast(getActivity(), msg);
    }


}
