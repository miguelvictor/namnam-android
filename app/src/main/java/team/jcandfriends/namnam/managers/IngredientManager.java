package team.jcandfriends.namnam.managers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Response;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import team.jcandfriends.namnam.models.Ingredient;

public final class IngredientManager extends BaseModelManager<Ingredient> implements ExclusionStrategy {

    private static final String TAG = IngredientManager.class.getSimpleName();

    public static final String BASE_URL = "http://cookstogo.herokuapp.com/api-v2/";

    private static IngredientManager SOLE_INSTANCE;

    private Realm mRealm;
    private IngredientService mIngredientService;

    public static IngredientManager getInstance() {
        if (SOLE_INSTANCE == null) {
            SOLE_INSTANCE = new IngredientManager();

            if (SOLE_INSTANCE.mRealm == null || SOLE_INSTANCE.mRealm.isClosed()) {
                SOLE_INSTANCE.mRealm = Realm.getDefaultInstance();
            }
        }

        return SOLE_INSTANCE;
    }

    private IngredientManager() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(this)
                .create();

        mRealm = Realm.getDefaultInstance();
        mIngredientService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(IngredientService.class);
    }

    @Override
    public void get(int pk, Callback<Ingredient> callback) {
        Call<Ingredient> call = mIngredientService.get(pk);
        call.enqueue(callback);
    }

    @Override
    public void getAll(Callback<List<Ingredient>> callback) {
        Call<List<Ingredient>> call = mIngredientService.getAll();
        call.enqueue(callback);
    }

    @Override
    public boolean isCached(int pk) {
        Ingredient ingredient = mRealm.where(Ingredient.class).equalTo("pk", pk).findFirst();
        return ingredient != null && ingredient.isValid();
    }

    @Override
    public Ingredient getFromCache(int pk) {
        return mRealm.where(Ingredient.class).equalTo("pk", pk).findFirst();
    }

    @Override
    public RealmResults<Ingredient> getAllFromCache() {
        return mRealm.where(Ingredient.class).findAll();
    }

    @Override
    public void delete(int pk, Callback<Response> callback) {
        Call<Response> call = mIngredientService.delete(pk);
        call.enqueue(callback);
    }

    @Override
    public void deleteFromCache(int pk) {
        mRealm.beginTransaction();
        mRealm.where(Ingredient.class).equalTo("pk", pk).findFirst().removeFromRealm();
        mRealm.commitTransaction();
    }

    @Override
    public void deleteAllFromCache() {
        mRealm.beginTransaction();
        mRealm.where(Ingredient.class).findAll().clear();
        mRealm.beginTransaction();
    }

    @Override
    public void release() {
        mRealm.close();
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(RealmObject.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    public interface IngredientService {

        @GET("ingredients")
        Call<List<Ingredient>> getAll();

        @GET("ingredients")
        Call<List<Ingredient>> getAll(@Query("page") int page);

        @GET("ingredients/{id}")
        Call<Ingredient> get(@Path("id") int id);

        @DELETE("ingredients/{id}")
        Call<Response> delete(@Path("id") int id);

    }

}
