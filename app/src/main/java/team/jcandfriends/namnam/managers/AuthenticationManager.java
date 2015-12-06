package team.jcandfriends.namnam.managers;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;
import team.jcandfriends.namnam.models.auth.FacebookCredentials;
import team.jcandfriends.namnam.models.auth.GoogleCredentials;
import team.jcandfriends.namnam.models.auth.LoginCredentials;
import team.jcandfriends.namnam.models.auth.RegistrationCredentials;
import team.jcandfriends.namnam.models.auth.Token;

public final class AuthenticationManager {

    private static final String TAG = AuthenticationManager.class.getSimpleName();

    private static final String BASE_URL = "http://namnam.herokuapp.com/api-auth/";

    private static AuthenticationManager SOLE_INSTANCE;

    private Context context;
    private AuthenticationService mAuthenticationService;

    public static AuthenticationManager getInstance(Context context) {
        if (SOLE_INSTANCE == null) {
            SOLE_INSTANCE = new AuthenticationManager(context);
        }

        return SOLE_INSTANCE;
    }

    private AuthenticationManager(Context context) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        mAuthenticationService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(AuthenticationService.class);

        this.context = context;
    }

    public void login(String identifier, String password, Callback<Token> callback) {
        mAuthenticationService
                .login(new LoginCredentials(context, identifier, password))
                .enqueue(callback);
    }

    public void loginToFacebook(long id, String name, String email, Callback<Token> callback) {
        mAuthenticationService
                .loginToFacebook(new FacebookCredentials(context, id, email, name))
                .enqueue(callback);
    }

    public void loginToGoogle(long id, String name, String email, Callback<Token> callback) {
        mAuthenticationService
                .loginToGoogle(new GoogleCredentials(context, id, email, name))
                .enqueue(callback);
    }

    public void register (String email, String password, Callback<Token> callback) {
        mAuthenticationService
                .register(new RegistrationCredentials(context, email, password))
                .enqueue(callback);
    }

    public interface AuthenticationService {

        @POST("signin")
        Call<Token> login(@Body LoginCredentials credentials);

        @POST("signin-fb")
        Call<Token> loginToFacebook(@Body FacebookCredentials credentials);

        @POST("signin-google")
        Call<Token> loginToGoogle(@Body GoogleCredentials credentials);

        @POST("register")
        Call<Token> register(@Body RegistrationCredentials credentials);
    }

}
