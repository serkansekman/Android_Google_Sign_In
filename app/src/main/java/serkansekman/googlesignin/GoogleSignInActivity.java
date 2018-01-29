package serkansekman.googlesignin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private static final int code=100;

    SignInButton btnGoogleSignIn;
    ImageView ivUser;
    TextView tvName,tvSurname,tvEmail;
    Button btnLogout;
    LinearLayout lyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        ivUser = findViewById(R.id.ivUser);
        tvName = findViewById(R.id.tvName);
        tvSurname = findViewById(R.id.tvSurname);
        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);
        lyProfile = findViewById(R.id.lyProfile);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSocialLogin();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogout();
            }
        });
    }

    private void googleSocialLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResultGoogleSignIn(result);
        }
    }

    private void handleResultGoogleSignIn(GoogleSignInResult result) {
        if (result.isSuccess()) {

            btnGoogleSignIn.setVisibility(View.GONE);
            lyProfile.setVisibility(View.VISIBLE);

            GoogleSignInAccount account = result.getSignInAccount();
            String socialFirstName = account.getGivenName();
            String socialLastName = account.getFamilyName();
            String socialEmail = account.getEmail();
            String socialUserId = account.getId();
            String socialImageUrl = account.getPhotoUrl().toString();

            tvName.setText(socialFirstName);
            tvSurname.setText(socialLastName);
            tvEmail.setText(socialEmail);

            Picasso.with(this)
                    .load(socialImageUrl)
                    .into(ivUser);

        }
    }

    private void googleLogout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                btnGoogleSignIn.setVisibility(View.VISIBLE);
                lyProfile.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
