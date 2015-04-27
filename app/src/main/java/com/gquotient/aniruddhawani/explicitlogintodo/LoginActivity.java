package com.gquotient.aniruddhawani.explicitlogintodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static junit.framework.Assert.assertEquals;


public class LoginActivity extends ActionBarActivity {

    Client mKinveyClient;

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username   = (EditText)findViewById(R.id.editText);
        password   = (EditText)findViewById(R.id.editText2);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        mKinveyClient.ping(new KinveyPingCallback (){

            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.d("KinveyPing","Success");
                Boolean loggedInStatus = mKinveyClient.user().isUserLoggedIn();
                Log.d("loggedInStatus",String.valueOf(loggedInStatus));
                if (loggedInStatus) {
                    Log.d("loggedInStatus","truth");
                    Log.d("LoginActivity",mKinveyClient.user().getUsername());
                    try {
                        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(mKinveyClient);
                    } catch (Exception exc) {
                        Log.d("err",exc.toString());
                    }
                    Intent goToNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    //goToNextActivity.putExtras("key",)
                    startActivity(goToNextActivity);
                    finish();
                }else{
                    Log.d("loggedInStatus","not really");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("KinveyPing","Failure");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    public void signUpButtonClicked(View view) {
        Log.d("signUpButtonClicked","True");
        Intent goToNextActivity = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(goToNextActivity);
        finish();
    }

    public void logInButtonClicked(View view) {
        //Log.d("logInButtonClicked","True");
        mKinveyClient.user().login(username.getText().toString(),password.getText().toString(), new KinveyUserCallback(){
            @Override
            public void onSuccess(User user) {
                Log.d("logInButtonClicked","onSuccess");
                Intent goToNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(goToNextActivity);
                finish();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("logInButtonClicked","onFailure");
                Toast.makeText(getApplicationContext(), "logInButtonClicked onFailure",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Log.d("LoginActivity", "onBackPressed Called");
        this.finish();
    }

    public void twitterSignInButtonClicked(View view) {
        Log.d("twSignInButtonClicked","True");
    }

    public void googleSignInButtonClicked(View view) {
        Log.d("gglSignInButtonClicked","True");
    }

    public void fbSignInButtonClicked(View view) {
        Log.d("fbSignInButtonClicked","True");
    }
}
