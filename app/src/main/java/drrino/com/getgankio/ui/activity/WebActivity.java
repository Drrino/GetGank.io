package drrino.com.getgankio.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;
import drrino.com.getgankio.ui.util.GankUtils;

/**
 * Created by Coder on 16/3/9.
 */
public class WebActivity extends BaseSwipeRefreshActivity {
  @Bind(R.id.web_content) WebView mWebContent;

  private static final String EXTRA_URL = "URL";
  private static final String EXTRA_TITLE = "TITLE";

  public static void gotoWebActivity(Activity context, String url, String title) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra(EXTRA_URL, url);
    intent.putExtra(EXTRA_TITLE, title);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String url = getIntent().getStringExtra(EXTRA_URL);
    String title = getIntent().getStringExtra(EXTRA_TITLE);

    if (!TextUtils.isEmpty(title)) setTitle(title, true);

    setupWebView(mWebContent);
    loadUrl(mWebContent, url);
  }

  private void setupWebView(WebView webView) {
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    //设置webView自适应手机屏幕
    settings.setLoadWithOverviewMode(true);
    //设置h5缓存
    settings.setAppCacheEnabled(true);
    //用WebView显示图片，可使用这个参数 设置网页布局类型：
    // 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
    // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    //支持缩放
    settings.setSupportZoom(true);
    webView.setWebViewClient(new LoveClient());
  }

  public void loadUrl(WebView webView, String url) {
    webView.loadUrl(url);
  }

  @Override protected void onRefreshStarted() {
    refresh();
  }

  private void refresh() {
    mWebContent.reload();
  }

  @Override protected int getLayout() {
    return R.layout.activity_web;
  }

  @Override public int getMenuRes() {
    return R.menu.menu_web;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_copy_url:
        String copyDone = getString(R.string.toast_copy_done);
        GankUtils.copyToClipBoard(this, mWebContent.getUrl(), copyDone);
        return true;
      case R.id.action_open_url:
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(mWebContent.getUrl());
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
          startActivity(intent);
        } else {
          Toast.makeText(WebActivity.this, R.string.toast_open_fail, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mWebContent != null) mWebContent.destroy();
    ButterKnife.unbind(this);
  }

  @Override protected void onPause() {
    super.onPause();
    if (mWebContent != null) mWebContent.onPause();
  }

  private class LoveClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if (url != null) view.loadUrl(url);
      return true;
    }

    @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
      showRefresh();
    }

    @Override public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      hideRefresh();
    }
  }
}
