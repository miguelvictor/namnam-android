package team.jcandfriends.namnam.models.auth;

import android.content.Context;

import team.jcandfriends.namnam.R;

public class GoogleCredentials implements Authorized {

    long id;
    String email;
    String name;

    String clientId;
    String clientSecret;

    public GoogleCredentials (Context context, long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;

        authorize(context);
    }

    @Override
    public void authorize(Context context) {
        clientId = context.getString(R.string.namnam_client_id);
        clientSecret = context.getString(R.string.namnam_client_secret);
    }
}
