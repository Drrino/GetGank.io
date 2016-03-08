package drrino.com.getgankio.ui.adapter;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import drrino.com.getgankio.R;
import drrino.com.getgankio.core.GankCategory;
import drrino.com.getgankio.data.entity.Gank;
import drrino.com.getgankio.ui.util.DateUtils;
import drrino.com.getgankio.ui.util.StringStyleUtils;
import drrino.com.getgankio.ui.view.IClickItem;
import drrino.com.getgankio.ui.weight.RatioImageView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 16/03/03.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolderItem> {
  private List<Gank> mGankList;
  private Context mContext;

  private static IClickItem mIClickItem;
  //blur girl
  private static ColorFilter mColorFilter;

  public MainListAdapter(Context context) {
    mContext = context;
    mGankList = new ArrayList<>();
    mGankList.add(getGankGirls());
    float[] array = new float[] {
        1, 0, 0, 0, -70, 0, 1, 0, 0, -70, 0, 0, 1, 0, -70, 0, 0, 0, 1, 0,
    };
    mColorFilter = new ColorMatrixColorFilter(new ColorMatrix(array));
  }

  public void setIClickItem(IClickItem IClickItem) {
    mIClickItem = IClickItem;
  }

  private enum EItemType {
    ITEM_TYPE_GIRL,
    ITEM_TYPE_NORMAL,
    ITEM_TYPE_CATEGOTY;
  }

  @Override public int getItemViewType(int position) {
    Gank gank = mGankList.get(position);
    if (gank.isGirl()) {
      return EItemType.ITEM_TYPE_GIRL.ordinal();
    } else if (gank.isHeader) {
      return EItemType.ITEM_TYPE_CATEGOTY.ordinal();
    } else {
      return EItemType.ITEM_TYPE_NORMAL.ordinal();
    }
  }

  /**
   * before add data,remove history data
   */
  public void updateWithClear(List<Gank> data) {
    mGankList.clear();
    update(data);
  }

  /**
   * add data append to history data
   */
  public void update(List<Gank> data) {
    formatGankData(data);
    notifyDataSetChanged();
  }

  /**
   * filter list and Insert category entity into list
   */
  private void formatGankData(List<Gank> data) {
    //Insert headers info list of items
    String lastHeader = "";
    for (int i = 0; i < data.size(); i++) {
      Gank gank = data.get(i);
      String header = gank.type;
      if (!gank.isGirl() && !TextUtils.equals(lastHeader, header)) {
        //Insert new header view
        Gank gankHeader = gank.clone();
        lastHeader = header;
        gankHeader.isHeader = true;
        mGankList.add(gankHeader);
      }
      gank.isHeader = false;
      mGankList.add(gank);
    }
  }

  private Gank getGankGirls() {
    Gank gank = new Gank();
    gank.publishedAt = new Date(System.currentTimeMillis());
    gank.url = "empty";
    gank.type = GankCategory.福利.name();
    return gank;
  }

  @Override public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    if (viewType == EItemType.ITEM_TYPE_GIRL.ordinal()) {
      view = LayoutInflater.from(mContext).inflate(R.layout.gank_item_girl, null);
      return new ViewHolderItemGirl(view);
    } else if (viewType == EItemType.ITEM_TYPE_CATEGOTY.ordinal()) {
      view = LayoutInflater.from(mContext).inflate(R.layout.gank_item_category, null);
      return new ViewHolderItemCategory(view);
    } else {
      view = LayoutInflater.from(mContext).inflate(R.layout.gank_item_normal, null);
      return new ViewHolderItemNormal(view);
    }
  }

  @Override public void onBindViewHolder(ViewHolderItem holder, int position) {
    holder.bindItem(mContext, mGankList.get(position));
  }

  @Override public int getItemCount() {
    return mGankList.size();
  }

  abstract static class ViewHolderItem extends RecyclerView.ViewHolder {
    public ViewHolderItem(View itemView) {
      super(itemView);
    }

    abstract void bindItem(Context context, Gank gank);
  }

  static class ViewHolderItemNormal extends ViewHolderItem {
    @Bind(R.id.tv_gank_title) TextView mTvTitle;
    @Bind(R.id.gank_parent) RelativeLayout mGankParent;

    public ViewHolderItemNormal(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @Override void bindItem(Context context, Gank gank) {
      mTvTitle.setText(StringStyleUtils.getGankInfoSequence(context, gank));
      mGankParent.setOnClickListener(v -> mIClickItem.onClickGankItemNormal(gank, v));
    }
  }

  static class ViewHolderItemCategory extends ViewHolderItem {
    @Bind(R.id.tv_category) TextView mTvCategory;

    public ViewHolderItemCategory(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @Override void bindItem(Context context, Gank gank) {
      mTvCategory.setText(gank.type);
    }
  }

  static class ViewHolderItemGirl extends ViewHolderItem {
    @Bind(R.id.tv_video_name) TextView mTvTime;
    @Bind(R.id.iv_index_photo) RatioImageView mImageView;

    public ViewHolderItemGirl(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mImageView.setOriginalSize(200, 100);
    }

    @Override void bindItem(Context context, Gank gank) {
      mTvTime.setText(DateUtils.toDate(gank.publishedAt));
      Picasso.with(context).load(gank.url).into(mImageView, new Callback() {
        @Override public void onSuccess() {
          mImageView.setColorFilter(mColorFilter);
        }

        @Override public void onError() {

        }
      });
      mTvTime.setText(DateUtils.toDate(gank.publishedAt));
      mImageView.setOnClickListener(
          v -> mIClickItem.onClickGankItemGirl(gank, mImageView, mTvTime));
    }
  }
}
