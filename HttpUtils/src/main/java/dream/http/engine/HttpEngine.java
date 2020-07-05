package dream.http.engine;

import java.util.Map;

import dream.http.callback.EngineCallback;

public interface HttpEngine {

    <T> void get(String url, Map<String, Object> params, EngineCallback<T> callback);

    <T> void post(String url, Map<String, Object> params, EngineCallback<T> callback);
}
