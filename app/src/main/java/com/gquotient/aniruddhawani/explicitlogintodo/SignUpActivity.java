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

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;


public class SignUpActivity extends ActionBarActivity {

    Client mKinveyClient;

    EditText userEmail;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userEmail = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        confirmPassword = (EditText)findViewById(R.id.editText5);

        mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void signUpAndCreateUserButtonClicked(View view) {
        Log.d("SignUpActivity", "signUpAndCreateUserButtonClicked");
        //check if both passwords match
        //check if user already exists
        //create and redirect to home page
        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            Log.d("SignUpActivity", "signUpAndCreateUserButtonClicked Passwords match!");
            mKinveyClient.user().create(userEmail.getText().toString(),password.getText().toString(),new KinveyUserCallback() {
                @Override
                public void onSuccess(User user) {
                    Log.d("SignUpActivity", "signUpAndCreateUserButtonClicked onSuccess");
                    Toast.makeText(getApplicationContext(), "signUpAndCreateUserButtonClicked onSuccess", Toast.LENGTH_LONG).show();
                    Intent goToNextActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(goToNextActivity);
                    finish();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d("SignUpActivity", "signUpAndCreateUserButtonClicked onFailure");
                    Toast.makeText(getApplicationContext(), "signUpAndCreateUserButtonClicked onFailure", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Log.d("SignUpActivity","signUpAndCreateUserButtonClicked Passwords do not match!");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("SignUpActivity", "onBackPressed Called");
        Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(goToNextActivity);
        finish();
    }

}
