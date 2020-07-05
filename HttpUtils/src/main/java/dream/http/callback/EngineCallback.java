package dream.http.callback;

import java.io.IOException;
import java.util.List;

import dream.http.convert.Converter;
import okhttp3.Call;
import okhttp3.Response;

public interface EngineCallback<T> extends Converter<T> {

    void onFailure(Call call, IOException e);

    void onSucceed(Call call, T response);

    StringCallback DEFAULT_CALLBACK = new StringCallback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onSucceed(Call call, String response) {

        }
    };


    abstract class StringCallback implements EngineCallback<String> {
        private StringConvert mConvert;

        public StringCallback() {
            mConvert = new StringConvert();
        }

        @Override
        public String convertResponse(Response response) throws IOException {
            return mConvert.convertResponse(response);
        }
    }

    abstract class CommonCallback<T> implements EngineCallback<T> {
        private final CommonConvert<T> mConvert;

        public CommonCallback() {
            mConvert = new CommonConvert<T>();
        }

        @Override
        public T convertResponse(Response response) throws IOException {
            return mConvert.convertResponse(response);
        }
    }

    abstract class ListCallback<T> implements EngineCallback<List<T>> {
        private ListConvert<T> mListConverter;

        public ListCallback() {
            mListConverter = new ListConvert<>();
        }

        @Override
        public List<T> convertResponse(Response response) throws IOException {
            return mListConverter.convertResponse(response);
        }
    }

}
