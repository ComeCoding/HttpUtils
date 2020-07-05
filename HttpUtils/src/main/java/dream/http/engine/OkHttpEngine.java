package dream.http.engine;

import android.content.Context;
import android.os.Handler;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import dream.http.callback.EngineCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpEngine implements HttpEngine {
    private final Context mContext;
    private final Handler mHandler;
    private OkHttpClient mHttpClient;


    public OkHttpEngine(Context context) {
        mHttpClient = new OkHttpClient();
        mContext = context;
        mHandler = new Handler(context.getMainLooper());
    }

    @Override
    public <T> void get(String url, Map<String, Object> params, final EngineCallback<T> callback) {
        Request request = new Request.Builder().url(appendParams(url, params)).get().build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull final Call call, @NotNull final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull final Call call, @NotNull Response response) throws IOException {
                final T result = callback.convertResponse(response);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSucceed(call, result);
                    }
                });

            }
        });
    }

    @Override
    public <T> void post(String url, Map<String, Object> param, final EngineCallback<T> callback) {
        MultipartBody.Builder builder = appendParams(new MultipartBody.Builder(), param);
        MultipartBody body = builder.setType(MultipartBody.FORM).build();
        Request request = new Request.Builder().url(url).post(body).build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull final Call call, @NotNull final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) throws IOException {
                final T result = callback.convertResponse(response);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSucceed(call, result);
                    }
                });
            }
        });

    }

    private String appendParams(String url, Map<String, Object> params) {
        int index = 0;
        StringBuilder builder = new StringBuilder().append(url);
        if (params != null && !params.isEmpty()) {
            builder.append("?");
            for (String key :
                    params.keySet()) {
                builder.append(key).append("=").append(params.get(key));
                if (index < params.size() - 1) {
                    builder.append("&");
                }
                index++;
            }
        }
        return builder.toString();
    }

    private MultipartBody.Builder appendParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key :
                    params.keySet()) {
                builder.addFormDataPart(key, String.valueOf(params.get(key)));
            }
        }
        return builder;
    }

}
