package dream.http.convert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public interface  Converter<T> {

    T convertResponse(Response response) throws IOException;


    class StringConvert implements Converter<String> {
        @Override
        public String convertResponse(Response response) throws IOException {
            String result = null;
            if (response.isSuccessful()) {
                result = response.body() != null ? response.body().string() : null;
            }
            return result;
        }
    }

    class CommonConvert<T> implements Converter<T> {
        @Override
        public T convertResponse(Response response) throws IOException {
            String str = new StringConvert().convertResponse(response);
            return new Gson().fromJson(str, new TypeToken<T>() {
            }.getType());
        }
    }


    class ListConvert<T> implements Converter<List<T>> {
        @Override
        public List<T> convertResponse(Response response) throws IOException {
            String str = new StringConvert().convertResponse(response);
            return new Gson().fromJson(str, new TypeToken<List<T>>() {
            }.getType());
        }
    }

}
