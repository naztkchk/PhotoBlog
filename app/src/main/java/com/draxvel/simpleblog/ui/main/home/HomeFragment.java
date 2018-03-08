package com.draxvel.simpleblog.ui.main.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.ui.main.home.adapter.BlogRecyclerAdapter;

public class HomeFragment extends Fragment implements IHomeView{

    private View root;
    private RecyclerView blog_list_rv;

    private HomePresenter homePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        initPresenter();

        homePresenter.setData();

        return root;
    }

    private void initView() {
        blog_list_rv = root.findViewById(R.id.blog_list_rv);
        blog_list_rv.setLayoutManager(new LinearLayoutManager(root.getContext()));
    }

    private void initPresenter() {
        homePresenter = new HomePresenter(this);
    }


    @Override
    public void setAdapter(BlogRecyclerAdapter adapter) {
        blog_list_rv.setAdapter(adapter);
    }
}
