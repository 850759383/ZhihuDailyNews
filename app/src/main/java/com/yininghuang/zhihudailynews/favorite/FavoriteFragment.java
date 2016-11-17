package com.yininghuang.zhihudailynews.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.adapter.FavoriteAdapter;
import com.yininghuang.zhihudailynews.model.db.Favorite;
import com.yininghuang.zhihudailynews.utils.MyItemTouchHelper;

import java.util.List;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class FavoriteFragment extends BaseFragment implements FavoriteContract.View, MyItemTouchHelper.OnItemSwipeListener {

    private RecyclerView mContentRec;
    private Toolbar mToolbar;

    private FavoriteContract.Presenter mPresenter;
    private FavoriteAdapter mAdapter;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    private void initViews(View rootView) {
        mContentRec = (RecyclerView) rootView.findViewById(R.id.contentRec);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((FavoriteActivity) getActivity()).setSupportActionBar(mToolbar);
        ((FavoriteActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_star);
        ((FavoriteActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FavoriteAdapter(getActivity());
        mContentRec.setAdapter(mAdapter);
        new ItemTouchHelper(new MyItemTouchHelper(this))
                .attachToRecyclerView(mContentRec);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        setHasOptionsMenu(true);
        initViews(rootView);
        mPresenter.init();
        return rootView;
    }

    @Override
    public void setResults(List<Favorite> favorites) {
        mAdapter.addFavorite(favorites);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(FavoriteContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
            case R.id.deleteAll: {
                if (mAdapter.getFavorites().size() == 0)
                    return false;
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.notice)
                        .setMessage(R.string.delete_all_confirm)
                        .setPositiveButton(R.string.delete_all, (dialog, which) -> {
                            mAdapter.getFavorites().clear();
                            mAdapter.notifyDataSetChanged();
                            mPresenter.clearAll();
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemDismiss(final int position) {
        List<Favorite> favorites = mAdapter.getFavorites();
        final Favorite favorite = favorites.get(position);
        mPresenter.remove(favorite.getId());
        favorites.remove(position);
        mAdapter.notifyItemRemoved(position);

        if (getView() != null)
            Snackbar.make(getView(), R.string.restore, Snackbar.LENGTH_LONG)
                    .setAction(R.string.confirm, v -> {
                        mAdapter.addFavorite(favorite, position);
                        mPresenter.add(favorite);
                        mAdapter.notifyItemInserted(position);
                    }).show();

    }
}
