package drrino.com.getgankio.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;

/**
 * Created by Coder on 16/4/21.
 */
public class AboutActivity extends AppCompatActivity {
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.daimajia) TextView daimajia;
  @Bind(R.id.drakeet) TextView drakeet;
  @Bind(R.id.maoruibin) TextView maoruibin;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
    ButterKnife.bind(this);

    mCollapsingToolbarLayout.setTitle("Gank");
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mToolbar.setNavigationOnClickListener(v -> AboutActivity.this.onBackPressed());

    initViews();
  }

  private void initViews() {
    daimajia.setMovementMethod(LinkMovementMethod.getInstance());
    drakeet.setMovementMethod(LinkMovementMethod.getInstance());
    maoruibin.setMovementMethod(LinkMovementMethod.getInstance());
  }
}
