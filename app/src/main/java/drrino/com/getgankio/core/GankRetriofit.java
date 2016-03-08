package drrino.com.getgankio.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Administrator on 16/03/04.
 */
public class GankRetriofit {
  final GankApi mService;

  final Gson gson =
      new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").serializeNulls().create();

  public GankRetriofit() {
    OkHttpClient client = new OkHttpClient();
    client.setReadTimeout(12, TimeUnit.SECONDS);
    RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(client))
        .setEndpoint("http://gank.io/api")
        .setConverter(new GsonConverter(gson))
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();
    mService = restAdapter.create(GankApi.class);
  }

  public GankApi getService() {
    return mService;
  }
}
