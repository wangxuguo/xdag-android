package com.xdag.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdag.wallet.R;
import com.xdag.wallet.model.XdagContactsModel;
import com.xdag.wallet.ui.activity.AddNewContractActivity;
import com.xdag.wallet.ui.adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class ContactsFragment extends BaseFragment {

    TextView textView;
    ImageView ivTitleRight;
    RecyclerView recyclerView ;
    List<XdagContactsModel> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        textView = (TextView) rootView.findViewById(R.id.tv_title);
        ivTitleRight = (ImageView) rootView.findViewById(R.id.iv_title_right);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        textView.setText(getString(R.string.tab_main_contracts));
        ivTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddNewContractActivity.class);
                startActivity(intent);
            }
        });
        RecyclerView.Adapter adapter = new ContactsAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
