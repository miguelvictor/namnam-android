package team.jcandfriends.namnam.models.auth;

import android.content.Context;

import team.jcandfriends.namnam.R;

public class RegistrationCredentials implements Authorized {

    String username;
    String email;
    String password;

    String clientId;
    String clientSecret;

    public RegistrationCredentials (Context context, String email, String password) {
        this.email = email;
        this.password = password;

        username = email.split("@")[0];

        authorize(context);
    }

    @Override
    public void authorize(Context context) {
        clientId = context.getString(R.string.namnam_client_id);
        clientSecret = context.getString(R.string.namnam_client_secret);
    }

}
