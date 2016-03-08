package drrino.com.getgankio.ui.adapter;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import drrino.com.getgankio.R;
import drrino.com.getgankio.data.entity.Girl;
import drrino.com.getgankio.ui.util.DateUtils;
import drrino.com.getgankio.ui.weight.RatioImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Coder on 16/3/8.
 */
public class GirlPictureAdapter extends RecyclerView.Adapter<GirlPictureAdapter.ViewHolder> {
  private List<Girl> mGirlList;
  private Context mContext;
  private IClickItem mIClickItem;
  //blur meizi
  private static ColorFilter mColorFilter;

  public GirlPictureAdapter(Context context) {
    mContext = context;
    mGirlList = new ArrayList<>();

    float[] array = new float[] {
        1, 0, 0, 0, -70, 0, 1, 0, 0, -70, 0, 0, 1, 0, -70, 0, 0, 0, 1, 0,
    };
    mColorFilter = new ColorMatrixColorFilter(new ColorMatrix(array));
  }

  public void setIClickItem(IClickItem IClickItem) {
    mIClickItem = IClickItem;
  }

  public interface IClickItem {
    void onClickPhoto(int position, View view, View textView);
  }

  /**
   * before add data,remove history data
   */
  public void updateWithClear(List<Girl> data) {
    mGirlList.clear();
    mGirlList.addAll(data);
    notifyDataSetChanged();
  }

  /**
   * add data append to history data
   *
   * @param data new data
   */
  public void update(List<Girl> data) {
    mGirlList.addAll(data);
    notifyDataSetChanged();
  }

  public Girl getGirl(int position) {
    return mGirlList.get(position);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_girl_picture, null);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Girl entity = mGirlList.get(position);

    Glide.with(mContext)
        .load(entity.url)
        .centerCrop()
        .into(holder.mIvGirlPicture)
        .getSize((width, height) -> holder.mIvGirlPicture.setColorFilter(mColorFilter));
    holder.mTvDate.setText(DateUtils.toDate(entity.publishedAt));
    if (mIClickItem != null) {
      holder.mIvGirlPicture.setOnClickListener(
          v -> mIClickItem.onClickPhoto(position, holder.mIvGirlPicture, holder.mTvDate));
    }
  }

  @Override public int getItemCount() {
    return mGirlList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.iv_girl_picture) RatioImageView mIvGirlPicture;
    @Bind(R.id.tv_date) TextView mTvDate;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mIvGirlPicture.setOriginalSize(50, 70);
    }
  }
}
