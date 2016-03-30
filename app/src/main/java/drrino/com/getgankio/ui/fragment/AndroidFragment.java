package drrino.com.getgankio.ui.fragment;

import drrino.com.getgankio.R;

/**
 * Created by Coder on 16/3/26.
 */
public class AndroidFragment extends BaseSwipeFragment {
  @Override protected int getLayoutId() {
    return R.layout.fragment_gank;
  }

  @Override protected void onRefreshStarted() {

  }

  @Override protected boolean prepareRefresh() {
    return false;
  }
}
