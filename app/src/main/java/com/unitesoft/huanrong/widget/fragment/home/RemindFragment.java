package com.unitesoft.huanrong.widget.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unitesoft.huanrong.Bean.FlashBean;
import com.unitesoft.huanrong.R;
import com.unitesoft.huanrong.presenter.FlashPresenter;
import com.unitesoft.huanrong.utils.ToastUtils;
import com.unitesoft.huanrong.view.FlashView;
import com.unitesoft.huanrong.widget.activity.home.NewsActivity;
import com.unitesoft.huanrong.widget.adapter.RemindAdapter;
import com.unitesoft.huanrong.widget.fragment.dialog.LoadDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * 提醒
 */
public class RemindFragment extends Fragment implements FlashView {

    FlashPresenter presenter;
    RemindAdapter adapter;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LoadDialog loadDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        ButterKnife.inject(this, view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDialog=new LoadDialog();
        loadDialog.show(getChildFragmentManager(),"");
        presenter = new FlashPresenter(this);
        presenter.setModel();
        presenter.getData("市场播报#政经要闻");
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void success(final List<FlashBean.DataBean.DangtianshujuBean> list) {
        loadDialog.dismiss();
        if (adapter==null){
            adapter=new RemindAdapter(list);
        }else {
            adapter.notifyDataSetChanged();
        }
        recyclerview.setAdapter(adapter);
        adapter.setItemClickLitener(new RemindAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String name=list.get(position).getClassname();
                if ("政经要闻".equals(name)) {
                    if (!TextUtils.isEmpty(list.get(position).getNewsreferurl())) {
                        NewsActivity.startIntent(getActivity(), list.get(position).getNewsreferurl());
                    }
                }
            }
        });
    }

    @Override
    public void failed(String errormessage) {
        loadDialog.dismiss();
        ToastUtils.showToast(getActivity(),errormessage);
    }
}
