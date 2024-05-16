package ncolrod.socialfutv3.api.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private TokenHolder tokenHolder = TokenHolder.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        System.out.println(request.headers());

        if (tokenHolder.getToken()!=null){
            request = request.newBuilder().addHeader("Authorization","Bearer "+ tokenHolder.getToken()).build();
            System.out.println(request.headers());
        }

        return chain.proceed(request);
    }

}
