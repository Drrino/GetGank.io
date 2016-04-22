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
public class GankArticleAdapter extends RecyclerView.Adapter<GankArticleAdapter.ViewHolderItem> {
  private Context mContext;
  private List<Gank> mArticleList;
  private static IClickItem mIClickItem;

  public void setIClickItem(IClickItem IClickItem) {
    mIClickItem = IClickItem;
  }

  public interface IClickItem {
    void onClickGankItem(Gank gank, View view);
  }

  public GankArticleAdapter(Context context) {
    mContext = context;
    mArticleList = new ArrayList<>();
  }

  /**
   * before add data,remove history data
   */
  public void updateWithClear(List<Gank> data) {
    mArticleList.clear();
    mArticleList.addAll(data);
    notifyDataSetChanged();
  }

  /**
   * add data append to history data
   */
  public void update(List<Gank> data) {
    mArticleList.addAll(data);
    notifyDataSetChanged();
  }

  @Override public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_article, null);
    return new ViewHolderItem(view);
  }

  @Override public void onBindViewHolder(ViewHolderItem holder, int position) {
    Gank gank = mArticleList.get(position);
    String title = "* " + gank.desc;
    holder.mGankTitle.setText(title);
    holder.mGankAuthor.setText(
        StringStyleUtils.format(mContext, "(via." + gank.who + ")", R.style.ViaTextAppearance));
    holder.mGankDate.setText(DateUtils.toDate(gank.publishedAt));

    holder.mGankArticle.setOnClickListener(v -> mIClickItem.onClickGankItem(gank, v));
  }

  @Override public int getItemCount() {
    return mArticleList.size();
  }

  public static class ViewHolderItem extends RecyclerView.ViewHolder {
    @Bind(R.id.gank_article) LinearLayout mGankArticle;
    @Bind(R.id.gank_title) TextView mGankTitle;
    @Bind(R.id.gank_author) TextView mGankAuthor;
    @Bind(R.id.gank_date) TextView mGankDate;

    public ViewHolderItem(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
