package com.xdag.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.xdag.wallet.R;
import com.xdag.wallet.model.Constants;
import com.xdag.wallet.model.XdagContactsModel;
import com.xdag.wallet.ui.activity.AddNewContractActivity;
import com.xdag.wallet.ui.adapter.BaseRecyclerViewAdapter;
import com.xdag.wallet.ui.adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxuguo on 2018/6/8.
 */

public class ContactsFragment extends BaseFragment {

    TextView textView;
    ImageView ivTitleRight;
    RecyclerView recyclerView;
    List<XdagContactsModel> list = new ArrayList<>();
    private ContactsAdapter adapter;

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
        SQLite.select()
                .from(XdagContactsModel.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<XdagContactsModel>() {
                    @Override
                    public void onListQueryResult(QueryTransaction queryTransaction, @NonNull List<XdagContactsModel> l) {
                        if (l != null && l.size() > 0) {
                            list.addAll(l);
                            adapter.setData(list);
                            Log.d(Constants.TAG,"list size: "+list.size());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).execute();

    }

    private void initView() {
        textView.setText(getString(R.string.tab_main_contracts));
        ivTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewContractActivity.class);
                startActivity(intent);
            }
        });
        adapter = new ContactsAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<XdagContactsModel>() {
            @Override
            public void onItemClick(View view, XdagContactsModel data) {

            }
        });
        adapter.setItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener<XdagContactsModel>() {
            @Override
            public void onItemLongClick(View view, XdagContactsModel data) {

            }
        });
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
