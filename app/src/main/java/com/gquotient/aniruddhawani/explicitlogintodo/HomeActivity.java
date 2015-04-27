package com.gquotient.aniruddhawani.explicitlogintodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyDeleteResponse;

import java.util.ArrayList;
import java.util.Collections;


public class HomeActivity extends ActionBarActivity {

    Client mKinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        Boolean loggedInStatus = mKinveyClient.user().isUserLoggedIn();
        Log.d("lIS HomeActivity", String.valueOf(loggedInStatus));
        updateUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_task) {
            Log.d("HomeActivity","action_add_task true");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add a task");
            builder.setMessage("What do you want to do?");
            final EditText inputField = new EditText(this);
            builder.setView(inputField);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String task = inputField.getText().toString();
                    Log.d("MainActivity", task);
                    pushToKinvey(task);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
            return true;
        } else if (id == R.id.action_logout) {
            Log.d("HomeActivity","action_logout true");
            mKinveyClient.user().logout().execute();
            Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(goToNextActivity);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pushToKinvey(String task) {
        //The EventEntity class is defined above
        tasksToDo taskToAdd = new tasksToDo();
        taskToAdd.setText(task);
        AsyncAppData<tasksToDo> myevents = mKinveyClient.appData("tasksToDo", tasksToDo.class);
        myevents.save(taskToAdd, new KinveyClientCallback<tasksToDo>() {
            @Override
            public void onFailure(Throwable e) {
                Log.e("pushToKinvey", "failed to save event data", e);
            }

            @Override
            public void onSuccess(tasksToDo r) {
                Log.d("pushToKinvey", "saved data for entity " + r.getText());
            }
        });
        updateUI();
    }

    private void updateUI() {
        Log.d("updateUI", "updateUI called");
        //The EventEntity class is defined above
        AsyncAppData<tasksToDo> tasks = mKinveyClient.appData("tasksToDo", tasksToDo.class);
        tasks.get(new KinveyListCallback<tasksToDo>() {
            @Override
            public void onSuccess(tasksToDo[] result) {
                Log.v("updateUI", "received " + result.length + " tasks");
                Toast.makeText(getApplicationContext(), "received " + result.length + " tasks",
                        Toast.LENGTH_LONG).show();
                // Construct the data source
                ArrayList<tasksToDo> arrayOfTasks = new ArrayList<>();
                /*
                for (int i = 0; i < result.length; i++) {
                    arrayOfTasks.add(result[i]);
                }
                */
                Collections.addAll(arrayOfTasks, result);
                // Create the adapter to convert the array to views
                tasksAdapter adapter = new tasksAdapter(HomeActivity.this, getApplicationContext(), arrayOfTasks);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.listViewItem);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e("updateUI", "failed to fetch all", error);
                Toast.makeText(getApplicationContext(), "failed to update tasks!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDoneButtonClick(String id) {
        Log.d("onDoneButtonClick",id);
        AsyncAppData<tasksToDo> tasks = mKinveyClient.appData("tasksToDo", tasksToDo.class);
        tasks.delete(id, new KinveyDeleteCallback() {
            @Override
            public void onSuccess(KinveyDeleteResponse response) {
                Log.v("onDoneButtonClick", "deleted successfully");
                updateUI();
            }
            public void onFailure(Throwable error) {
                Log.e("onDoneButtonClick", "failed to delete ", error);
                Toast.makeText(getApplicationContext(), "failed to delete task!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("HomeActivity", "onBackPressed Called");
        Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(goToNextActivity);
        finish();
    }
}
