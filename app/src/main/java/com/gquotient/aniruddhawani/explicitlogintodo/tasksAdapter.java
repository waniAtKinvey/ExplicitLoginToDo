package com.gquotient.aniruddhawani.explicitlogintodo;

import android.content.Context;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kinvey.java.User;

import java.util.ArrayList;

/**
 * Created by Aniruddha Wani on 23-Apr-15.
 */
public class tasksAdapter extends ArrayAdapter<tasksToDo> {
    HomeActivity activity;

    public tasksAdapter(HomeActivity activity, Context context, ArrayList<tasksToDo> users) {
        super(context, 0, users);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final tasksToDo task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_view, parent, false);
        }
        // Lookup view for data population
        TextView taskText = (TextView) convertView.findViewById(R.id.idTextViewItem);
        Button doneButton = (Button) convertView.findViewById(R.id.doneButton);
        // Populate the data into the template view using the data object
        taskText.setText(task.getText());
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String task = "Pratik baba";
                Log.d("onDoneButtonClick", task.getText());
                Log.d("onDoneButtonClick", task.getId());
                //code item delete
                //activity.updateUI();
                activity.onDoneButtonClick(task.getId());
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
