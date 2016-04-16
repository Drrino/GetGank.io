package drrino.com.getgankio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;
import drrino.com.getgankio.model.MenuItem;
import drrino.com.getgankio.ui.adapter.MenuAdapter;

/**
 * Created by Administrator on 16/03/03.
 */
public class MenuFragment extends Fragment {
  @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_drawer, container, false);
    ButterKnife.bind(this, view);

    setupRecyclerView();
    return view;
  }

  private void setupRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(linearLayoutManager);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    MenuAdapter mAdapter = new MenuAdapter(getActivity());
    addAllMenuItems(mAdapter);
    mRecyclerView.setAdapter(mAdapter);
  }

  private void addAllMenuItems(MenuAdapter mAdapter) {
    mAdapter.menuItems.clear();
    mAdapter.menuItems.add(
        new MenuItem("Gank", R.mipmap.home, MenuItem.FragmentType.Gank, GankFragment.class));
    mAdapter.menuItems.add(new MenuItem("Picture", R.mipmap.picture, MenuItem.FragmentType.Girl,
        GirlPictureFragment.class));
    mAdapter.menuItems.add(new MenuItem("Android", R.mipmap.android, MenuItem.FragmentType.Android,
        AndroidFragment.class));
    mAdapter.menuItems.add(new MenuItem("IOS", R.mipmap.ios, MenuItem.FragmentType.IOS,
        IOSFragment.class));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
