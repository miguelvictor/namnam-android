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
import retrofit.http.GET;
import team.jcandfriends.namnam.models.Recipe;

public final class RecipeManager extends BaseModelManager<Recipe> implements ExclusionStrategy {

    private static final String TAG = RecipeManager.class.getSimpleName();

    private static RecipeManager SOLE_INSTANCE;

    private Realm mRealm;
    private RecipeService mRecipeService;

    public static RecipeManager getInstance() {
        if (SOLE_INSTANCE == null) {
            SOLE_INSTANCE = new RecipeManager();

            if (SOLE_INSTANCE.mRealm == null || SOLE_INSTANCE.mRealm.isClosed()) {
                SOLE_INSTANCE.mRealm = Realm.getDefaultInstance();
            }
        }

        return SOLE_INSTANCE;
    }

    private RecipeManager() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(this)
                .create();

        mRealm = Realm.getDefaultInstance();
        mRecipeService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RecipeService.class);
    }

    @Override
    public void get(int pk, Callback<Recipe> callback) {

    }

    @Override
    public void getAll(Callback<List<Recipe>> callback) {

    }

    @Override
    public boolean isCached(int pk) {
        Recipe recipe = mRealm.where(Recipe.class).equalTo("pk", pk).findFirst();
        return recipe != null && recipe.isValid();
    }

    @Override
    public void delete(int pk, Callback<Response> callback) {

    }

    @Override
    public Recipe getFromCache(int pk) {
        return mRealm.where(Recipe.class).equalTo("pk", pk).findFirst();
    }

    @Override
    public RealmResults<Recipe> getAllFromCache() {
        return mRealm.where(Recipe.class).findAll();
    }

    @Override
    public void deleteFromCache(int pk) {

    }

    @Override
    public void deleteAllFromCache() {

    }

    @Override
    public void release() {

    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(RealmObject.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    public interface RecipeService {

        @GET("recipes")
        Call<List<Recipe>> getRecipes();



    }
}
