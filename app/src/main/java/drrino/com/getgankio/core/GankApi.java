package drrino.com.getgankio.core;

import drrino.com.getgankio.data.GankData;
import drrino.com.getgankio.data.GirlData;
import drrino.com.getgankio.data.休息视频Data;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 16/03/04.
 */
public interface GankApi {
  @GET("/day/{year}/{month}/{day}") Observable<GankData> getGankData(@Path("year") int year,
      @Path("month") int month, @Path("day") int day);

  @GET("/data/福利/{pagesize}/{page}") Observable<GirlData> getGirlData(
      @Path("pagesize") int pagesize, @Path("page") int page);

  @GET("/data/Android/{pagesize}/{page}") Observable<GirlData> getAndroidData(
      @Path("pagesize") int pagesize, @Path("page") int page);

  @GET("/data/休息视频/{pagesize}/{page}") Observable<休息视频Data> get休息视频Data(
      @Path("pagesize") int pagesize, @Path("page") int page);
}
