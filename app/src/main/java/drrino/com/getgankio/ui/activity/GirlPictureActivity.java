package drrino.com.getgankio.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import com.squareup.picasso.Picasso;
import drrino.com.getgankio.R;
import drrino.com.getgankio.ui.util.GankUtils;
import drrino.com.getgankio.ui.util.SaveImageUtils;
import java.io.File;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 16/03/04.
 */
public class GirlPictureActivity extends BaseActivity {
  @Bind(R.id.iv_girl_detail) ImageView mIvGirlDetail;

  private static final String EXTRA_BUNDLE_URL = "BUNDLE_URL";
  private static final String EXTRA_BUNDLE_TITLE = "BUNDLE_TITLE";

  private static final String VIEW_NAME_HEADER_IMAGE = "DETAIL_IMAGE";
  private static final String VIEW_NAME_HEADER_TITLE = "DETAIL_TITLE";

  private String mUrl, mTitle;
  PhotoViewAttacher mPhotoViewAttacher;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPhotoViewAttacher = new PhotoViewAttacher(mIvGirlDetail);
    mUrl = getIntent().getStringExtra(EXTRA_BUNDLE_URL);
    mTitle = getIntent().getStringExtra(EXTRA_BUNDLE_TITLE);
    setTitle(mTitle, true);

    ViewCompat.setTransitionName(mIvGirlDetail, VIEW_NAME_HEADER_IMAGE);
    ViewCompat.setTransitionName(GankUtils.getTitleViewInToolbar(toolbar), VIEW_NAME_HEADER_TITLE);

    loadPicture();
  }

  public static void gotoGirlDetail(Activity context, String url, String title,
      final View viewImage, final View viewText) {
    Intent intent = new Intent(context, GirlPictureActivity.class);
    intent.putExtra(EXTRA_BUNDLE_URL, url);
    intent.putExtra(EXTRA_BUNDLE_TITLE, title);

    ActivityOptionsCompat activityOptions =
        ActivityOptionsCompat.makeSceneTransitionAnimation(context,
            new Pair<View, String>(viewImage, VIEW_NAME_HEADER_IMAGE),
            new Pair<View, String>(viewText, VIEW_NAME_HEADER_TITLE));
    ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
  }

  private void loadPicture() {
    if (GankUtils.isAndroidL()) {
      loadThumbnail();
    } else {
      loadFullSizeImage();
    }
  }

  private void loadFullSizeImage() {
    Picasso.with(this).load(mUrl).noFade().noPlaceholder().into(mIvGirlDetail);
  }

  private void loadThumbnail() {
    Picasso.with(this).load(mUrl).noFade().into(mIvGirlDetail);
  }

  @Override protected int getLayout() {
    return R.layout.activity_girl_detail;
  }

  @Override public int getMenuRes() {
    return R.menu.picture_detail;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.save) {
      saveGirl();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void saveGirl() {
    Subscription s = SaveImageUtils.saveImageAndGetPathObservable(this, mUrl, mTitle)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(uri -> {
          File appDir = new File(Environment.getExternalStorageDirectory(), "GankMeizhi");
          String msg = String.format("图片以保存至 %s 文件夹", appDir.getAbsolutePath());
          Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }, error -> Toast.makeText(this, error.getMessage() + "\n再试试...", Toast.LENGTH_SHORT)
            .show());
    CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    mCompositeSubscription.add(s);
  }
}
