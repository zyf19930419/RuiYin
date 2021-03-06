package com.unitesoft.huanrong.widget.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unitesoft.huanrong.Bean.CCTVBean;
import com.unitesoft.huanrong.R;
import com.unitesoft.huanrong.model.CCTVModel;
import com.unitesoft.huanrong.presenter.CCTVPresenter;
import com.unitesoft.huanrong.utils.ConstanceValue;
import com.unitesoft.huanrong.utils.ToastUtils;
import com.unitesoft.huanrong.view.CCTView;
import com.unitesoft.huanrong.widget.activity.home.PlayCCTVActivity;
import com.unitesoft.huanrong.widget.adapter.home.CCTVAdapter;
import com.unitesoft.huanrong.widget.fragment.dialog.LoadDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * 央视
 */
public class CCTVFragment extends Fragment implements CCTView {


    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    CCTVPresenter presenter;
    CCTVAdapter adapter;

    private LoadDialog loadDialog;

    public CCTVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cctv, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview();
        initdata();
    }
    private void initview() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
    }

    private void initdata() {
        loadDialog=new LoadDialog();
        loadDialog.show(getChildFragmentManager(),"");
        presenter = new CCTVPresenter(this);
        presenter.setModel(new CCTVModel());
        presenter.getData("鲸鱼APP视频");
    }

    @Override
    public void success(final List<CCTVBean.DataBean> data) {
        loadDialog.dismiss();
        if (adapter == null) {
            adapter = new CCTVAdapter(data);
        } else {
            adapter.notifyDataSetChanged();
        }
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new CCTVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<String> urls=new ArrayList<String>();
                List<String> images=new ArrayList<String>();
                List<String>  titles=new ArrayList<String>();
                for (int i = 0; i < data.size(); i++) {
                    urls.add(data.get(i).getVideoUrl());
                    images.add(ConstanceValue.iptmsgurl+data.get(i).getVideoPicture());
                    titles.add(data.get(i).getVideoName());
                }
                PlayCCTVActivity.startIntent(getActivity(),urls,images,titles);
            }
        });
    }

    @Override
    public void failed(String message) {
        loadDialog.dismiss();
        ToastUtils.showToast(getActivity(), message);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
