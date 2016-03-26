package drrino.com.getgankio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;

/**
 * Created by Coder on 16/3/26.
 */
public abstract class BaseSwipeFragment extends Fragment {
  @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

  protected abstract int getLayoutId();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    trySetUpSwipeLayout();
  }

  void trySetUpSwipeLayout() {
    if (mSwipeRefreshLayout != null) {
      mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
      mSwipeRefreshLayout.setOnRefreshListener(() -> requestDataRefresh());
    }
  }

  protected abstract void requestDataRefresh();

  void setRefreshing(boolean refreshing) {
    if (mSwipeRefreshLayout == null) return;
    if (!refreshing) {
      mSwipeRefreshLayout.postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
    } else {
      mSwipeRefreshLayout.setRefreshing(true);
    }
  }
}
