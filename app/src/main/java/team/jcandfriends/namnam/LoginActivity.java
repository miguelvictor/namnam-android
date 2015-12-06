package team.jcandfriends.namnam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import team.jcandfriends.namnam.managers.AuthenticationManager;
import team.jcandfriends.namnam.models.auth.Token;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private EditText etIdentifier;
    private EditText etPassword;

    private AuthenticationManager mAuthManager;

    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etIdentifier = (EditText) findViewById(R.id.etIdentifier);
        etPassword = (EditText) findViewById(R.id.etPassword);

        mAuthManager = AuthenticationManager.getInstance(this);

        initializeFacebookLogin();
        initializeGoogleLogin();
    }

    private void initializeFacebookLogin() {
        mCallbackManager = CallbackManager.Factory.create();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.i(TAG, "AccessToken: " + accessToken);

        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        FacebookRequestError error = response.getError();
                        if (error == null) {
                            try {
                                Log.i(TAG, "Facebook Account Details");
                                Log.i(TAG, "ID : " + object.getString("id"));
                                Log.i(TAG, "Name : " + object.getString("name"));
                                Log.i(TAG, "Email : " + object.getString("email"));
                            } catch (JSONException e) {
                                // can be ignored sometimes
                                Log.e(TAG, "Error parsing JSON response from Graph Request : " + e.getMessage(), e);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Something unexpected has occurred. Sorry!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "Login attempt cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Something unexpected has occurred. Sorry!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.google_login_button);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                Log.i(TAG, "GOOGLE ACCOUNT DETAILS");
                Log.i(TAG, "ID: " + account.getId());
                Log.i(TAG, "Name: " + account.getDisplayName());
                Log.i(TAG, "Email: " + account.getEmail());

                mAuthManager.loginToGoogle();
            } else {
                Toast.makeText(this, "Something unexpected has occurred. Sorry!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Something unexpected has occurred. Sorry!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                startGoogleSignIn();
                break;
        }
    }

    private void startGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void onLogin(View view) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Logging you in...")
                .setCancelable(false)
                .create();

        String identifier = etIdentifier.getText().toString();
        String password = etPassword.getText().toString();

        mAuthManager.login(identifier, password, new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                // do something with response
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                // inform user
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
