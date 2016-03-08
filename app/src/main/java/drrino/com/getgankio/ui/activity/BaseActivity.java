package drrino.com.getgankio.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;

/**
 * Created by Administrator on 16/03/04.
 */
public abstract class BaseActivity extends AppCompatActivity {
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayout());
    ButterKnife.bind(this);
    initToolBar();
  }

  private void initToolBar() {
    if (toolbar == null) {
      throw new NullPointerException("need to add a toolbar");
    }
    setSupportActionBar(toolbar);
  }

  public void setTitle(String strTitle, boolean showIcon) {
    setTitle(strTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(showIcon);
  }

  protected abstract int getLayout();

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (getMenuRes() < 0) return true;
    getMenuInflater().inflate(getMenuRes(), menu);
    return true;
  }

  public int getMenuRes(){
    return -1;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
