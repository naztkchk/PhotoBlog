package com.draxvel.simpleblog.ui.main.account;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.ui.main.adapter.BlogRecyclerAdapter;

public class AccountFragment extends Fragment implements IAccountView{

    private View root;
    private AccountPresenter accountPresenter;
    private RecyclerView account_posts_list_rv;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_account, container, false);

        initView();
        initPresenter();
        accountPresenter.setData();

        return root;
    }

    private void initView() {
        account_posts_list_rv = root.findViewById(R.id.account_posts_list_rv);
        account_posts_list_rv.setLayoutManager(new LinearLayoutManager(root.getContext()));
    }


    private void initPresenter() {
        accountPresenter = new AccountPresenter(this, getActivity());
    }


    @Override
    public void setAdapter(BlogRecyclerAdapter blogRecyclerAdapter) {
        account_posts_list_rv.setAdapter(blogRecyclerAdapter);
    }
}
