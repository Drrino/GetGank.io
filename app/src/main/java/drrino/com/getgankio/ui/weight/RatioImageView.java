package drrino.com.getgankio.ui.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 16/03/03.
 */
public class RatioImageView extends ImageView {
  private int originalWidth;
  private int originalHeight;

  private int measureWidth;
  private int measureHeight;

  public RatioImageView(Context context) {
    super(context, null);
  }

  public RatioImageView(Context context, AttributeSet attrs) {
    super(context, attrs, 0);
  }

  public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setOriginalSize(int originalWidth, int originalHeight) {
    this.originalWidth = originalWidth;
    this.originalHeight = originalHeight;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (originalWidth > 0 && originalHeight > 0) {
      float ratio = (float) originalWidth / (float) originalHeight;
      measureWidth = MeasureSpec.getSize(widthMeasureSpec);
      measureHeight = MeasureSpec.getSize(heightMeasureSpec);

      if (measureWidth > 0) measureHeight = (int) ((float) measureWidth / ratio);
      setMeasuredDimension(measureWidth, measureHeight);
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }
}
