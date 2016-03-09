package drrino.com.getgankio.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import butterknife.Bind;
import drrino.com.getgankio.R;

/**
 * Created by Coder on 16/3/9.
 */
public abstract class BaseSwipeRefreshActivity extends BaseActivity {
  @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initSwipeRefresh();
  }

  private void initSwipeRefresh() {
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
    mSwipeRefreshLayout.setOnRefreshListener(() -> {
      if (prepareRefresh()) {
        onRefreshStarted();
      } else {
        hideRefresh();
      }
    });
  }

  public void hideRefresh() {
    mSwipeRefreshLayout.postDelayed(() -> {
      if(mSwipeRefreshLayout != null){
        mSwipeRefreshLayout.setRefreshing(false);
      }
    },1000);
  }

  protected boolean prepareRefresh(){
    return true;
  }

  public void showRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);
  }

  protected abstract void onRefreshStarted();


}
