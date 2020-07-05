package dream.http;

import android.content.Context;

import androidx.collection.ArrayMap;

import java.util.Map;
import java.util.Objects;

import dream.http.callback.EngineCallback;
import dream.http.engine.HttpEngine;
import dream.http.engine.OkHttpEngine;

public class HttpUtils {
    private static final int REQUEST_GET_TYPE = 0x001;
    private static final int REQUEST_POST_TYPE = 0x002;
    private int mRequestType = REQUEST_GET_TYPE;
    private final Context mContext;
    private HttpEngine mHttpEngine;
    private String mUrl;
    private Map<String, Object> mParams;


    private HttpUtils(Context context) {
        mContext = context;
        mHttpEngine = new OkHttpEngine(context);
        mParams = new ArrayMap<>();
    }


    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }

    public HttpUtils get() {
        mRequestType = REQUEST_GET_TYPE;
        return this;
    }

    public HttpUtils post() {
        mRequestType = REQUEST_POST_TYPE;
        return this;
    }

    public <T> void execute(EngineCallback<T> callback) {
        switch (mRequestType) {
            case REQUEST_GET_TYPE:
                get(mUrl, mParams, callback != null ? callback : EngineCallback.DEFAULT_CALLBACK);
                break;
            case REQUEST_POST_TYPE:
                post(mUrl, mParams, callback != null ? callback : EngineCallback.DEFAULT_CALLBACK);
                break;
        }
    }

    public void execute() {
        execute(null);
    }

    public HttpUtils addParam(String key, Objects value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParam(Map<String, Objects> map) {
        mParams.putAll(map);
        return this;
    }


    private <T> void get(String url, Map<String, Object> params, EngineCallback<T> callback) {
        mHttpEngine.get(url, params, callback);
    }

    private <T> void post(String url, Map<String, Object> params, EngineCallback<T> callback) {
        mHttpEngine.post(url, params, callback);
    }


}
