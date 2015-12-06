package team.jcandfriends.namnam.managers;

import com.squareup.okhttp.Response;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import retrofit.Callback;

/**
 * The base manager for each of the Realm models defined in this application. It contains all
 * fundamental methods such as retrieving objects from the server's REST API, caching elements,
 * deleting cached elements, retrieval of cached elements.
 *
 * @param <T>
 */
public abstract class BaseModelManager<T extends RealmObject> {

    public static final String BASE_URL = "http://cookstogo.herokuapp.com/api-v2/";

    /**
     * Fetches an element from the server's REST API. This method must strictly follow the REST
     * convention when building URLs. After the successful retrieval of the element, it is cached
     * and onSuccess() is executed from the given callbacks parameter.
     *
     * @param pk       the id of the element to be fetched
     * @param callback the callbacks that will be invoked appropriately
     */
    public abstract void get(int pk, Callback<T> callback);

    public abstract T getFromCache(int pk);

    public abstract void getAll(Callback<List<T>> callback);

    public abstract RealmResults<T> getAllFromCache();

    public abstract boolean isCached(int pk);

    public abstract void delete(int pk, Callback<Response> callback);

    public abstract void deleteFromCache(int pk);

    public abstract void deleteAllFromCache();

    public abstract void release();

    public interface SingleCallbacks<T> {
        void onSuccess(T element);
        void onFailure(int reason);
    }

    public interface ListCallbacks<T extends RealmObject> {
        void onSuccess(RealmList<T> elements);
        void onFailure(int reason);
    }

}