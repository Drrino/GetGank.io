package drrino.com.getgankio.core;

/**
 * Created by Administrator on 16/03/04.
 */
public class GankFactory {
  protected static volatile GankApi mGankApi;
  protected static final Object monitor = new Object();

  public static GankApi getGankApiInstance(){
    synchronized (monitor){
      if (mGankApi==null){
        mGankApi = new GankRetriofit().getService();
      }
      return mGankApi;
    }
  }
}
