package team.jcandfriends.namnam.models.auth;

import android.content.Context;

import team.jcandfriends.namnam.R;

public class LoginCredentials implements Authorized {

    String identifier;
    String password;

    String clientId;
    String clientSecret;

    public LoginCredentials (Context context, String identifier, String password) {
        this.identifier = identifier;
        this.password = password;

        authorize(context);
    }

    @Override
    public void authorize(Context context) {
        clientId = context.getString(R.string.namnam_client_id);
        clientSecret = context.getString(R.string.namnam_client_secret);
    }

}
