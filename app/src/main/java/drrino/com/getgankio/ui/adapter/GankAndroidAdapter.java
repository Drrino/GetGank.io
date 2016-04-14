package drrino.com.getgankio.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import drrino.com.getgankio.R;
import drrino.com.getgankio.data.entity.Gank;
import drrino.com.getgankio.ui.util.DateUtils;
import drrino.com.getgankio.ui.util.StringStyleUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16/03/03.
 */
public class GankAndroidAdapter extends RecyclerView.Adapter<GankAndroidAdapter.ViewHolderItem> {
  private Context mContext;
  private List<Gank> mAndroidList;
  private static IClickItem mIClickItem;

  public void setIClickItem(IClickItem IClickItem) {
    mIClickItem = IClickItem;
  }

  public interface IClickItem {
    void onClickGankItemAndroid(Gank gank, View view);
  }

  public GankAndroidAdapter(Context context) {
    mContext = context;
    mAndroidList = new ArrayList<>();
  }

  /**
   * before add data,remove history data
   */
  public void updateWithClear(List<Gank> data) {
    mAndroidList.clear();
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
    for (int i = 0; i < data.size(); i++) {
      Gank gankData = data.get(i);
      if (gankData.isAndroid()) {
        mAndroidList.add(gankData);
      }
    }
  }

  @Override public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolderItem(
        LayoutInflater.from(mContext).inflate(R.layout.item_android, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolderItem holder, int position) {
    Gank gank = mAndroidList.get(position);
    holder.mGankTitle.setText(StringStyleUtils.getGankInfoSequence(mContext, gank));
    holder.mGankDate.setText(DateUtils.toDate(gank.publishedAt));

    holder.mGankAndroid.setOnClickListener(v -> mIClickItem.onClickGankItemAndroid(gank, v));
  }

  @Override public int getItemCount() {
    return mAndroidList.size();
  }

  public static class ViewHolderItem extends RecyclerView.ViewHolder {
    @Bind(R.id.gank_android) LinearLayout mGankAndroid;
    @Bind(R.id.gank_title) TextView mGankTitle;
    @Bind(R.id.gank_date) TextView mGankDate;

    public ViewHolderItem(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}