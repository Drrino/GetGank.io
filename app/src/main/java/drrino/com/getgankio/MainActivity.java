package drrino.com.getgankio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.ui.activity.AboutActivity;
import drrino.com.getgankio.ui.activity.BaseMainActivity;
import drrino.com.getgankio.ui.fragment.GankFragment;
import drrino.com.getgankio.ui.fragment.MenuFragment;
import drrino.com.getgankio.ui.view.IMainView;

public class MainActivity extends BaseMainActivity implements IMainView {
  private static final String TAG_MENU = "menu";
  private static final String TAG_GANK = "gank";

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @Bind(R.id.fab) FloatingActionButton fab;

  private ActionBarDrawerToggle mActionBarDrawerToggle;

  @Override protected void initView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    initToolbar();
    setupDrawerLayout();

    fab.setOnClickListener(view -> startActivity(new Intent(this, AboutActivity.class)));
  }

  private void setupDrawerLayout() {
    mActionBarDrawerToggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
            R.string.app_name) {
          @Override public void onDrawerOpened(View drawerView) {
            invalidateOptionsMenu();
          }

          @Override public void onDrawerClosed(View drawerView) {
            invalidateOptionsMenu();
          }
        };
    mActionBarDrawerToggle.syncState();
    mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

    addFragment(R.id.frame_container, new GankFragment(), TAG_GANK);
    addFragment(R.id.drawer_container, new MenuFragment(), TAG_MENU);
  }

  private void initToolbar() {
    setTitle("Gank");
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void closeDrawer() {
    mDrawerLayout.closeDrawers();
  }

  @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mActionBarDrawerToggle.syncState();
  }
}
