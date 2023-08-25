package com.todo3;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private static final String PREFS_NAME = "MyPrefsFile"; // SharedPreferences file name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load saved tasks from SharedPreferences
        loadTasks();

        RecyclerView recyclerView = findViewById(R.id.highPriorityRecyclerView);
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        FloatingActionButton fabab = findViewById(R.id.fabAbout);

        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskSnackbar();
            }
        });

        fabab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, about.class);
                startActivity(i);
            }
        });

        // Update the completion percentage and notify the adapter
        updateCompletionPercentageTextView();
        taskAdapter.notifyDataSetChanged();
    }



    @Override
    public void onTaskClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Task Options");

        String[] options = {"Complete", "Edit"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { // Complete
                    Task task = taskList.get(position);
                    task.setCompleted(true);
                    taskAdapter.notifyItemChanged(position);
                    updateCompletionPercentageTextView(); // Update completion percentage
                    saveTasks(); // Save the tasks after modifying
                } else if (which == 1) { // Edit
                    showEditTaskDialog(position); // Call the edit task dialog method
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showEditTaskDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task Title");

        final EditText taskTitleEditText = new EditText(this);
        taskTitleEditText.setText(taskList.get(position).getTitle());
        builder.setView(taskTitleEditText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = taskTitleEditText.getText().toString();
                taskList.get(position).setTitle(newTitle);
                taskAdapter.notifyItemChanged(position);
                saveTasks(); // Save the tasks after modifying
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void updateCompletionPercentageTextView() {
        int totalTasks = taskList.size();
        int completedTasks = 0;

        for (Task task : taskList) {
            if (task.isCompleted()) {
                completedTasks++;
            }
        }

        int completionPercentage = (int) ((completedTasks * 100.0) / totalTasks);

        TextView completionPercentageTextView = findViewById(R.id.completionPercentageTextView);
        String percentageText = completionPercentage + "% Completed";
        completionPercentageTextView.setText(percentageText);
    }

    void editTaskTitle(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task Title");

        final EditText taskTitleEditText = new EditText(this);
        taskTitleEditText.setText(taskList.get(position).getTitle());
        builder.setView(taskTitleEditText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = taskTitleEditText.getText().toString();
                taskList.get(position).setTitle(newTitle);
                taskAdapter.notifyItemChanged(position);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddTaskSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_INDEFINITE);

        // Inflate the custom snackbar layout
        View customView = LayoutInflater.from(this).inflate(R.layout.snackbar_add_task, null);
        final EditText taskTitleEditText = customView.findViewById(R.id.taskTitleEditText);
        TextView actionTextView = customView.findViewById(R.id.actionTextView);

        // Inside the showAddTaskSnackbar method
        actionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskTitle = taskTitleEditText.getText().toString();
                if (!taskTitle.isEmpty()) {
                    // Show due date picker
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, monthOfYear, dayOfMonth);
                            Date dueDate = calendar.getTime();

                            // Create a new task with the title and due date
                            Task newTask = new Task(taskTitle, dueDate);

                            // Add the task and update UI
                            taskList.add(newTask);
                            taskAdapter.notifyDataSetChanged();
                            taskTitleEditText.setText("");
                            Snackbar.make(findViewById(android.R.id.content), "Task added!", Snackbar.LENGTH_SHORT).show();
                            updateCompletionPercentageTextView();
                            saveTasks();
                        }
                    };

                    // Show date picker dialog
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(MainActivity.this, dateSetListener, year, month, day).show();
                }
            }
        });


        snackbar.getView().setPadding(0, 0, 0, 0);
        snackbar.getView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((Snackbar.SnackbarLayout) snackbar.getView()).addView(customView, 0);
        snackbar.show();
    }



    private int calculateCompletionPercentage() {
        int totalTasks = taskList.size(); // Total number of tasks
        int completedTasks = 0; // Number of completed tasks

        for (Task task : taskList) {
            if (task.isCompleted()) {
                completedTasks++;
            }
        }

        if (totalTasks > 0) {
            return (completedTasks * 100) / totalTasks;
        } else {
            return 0; // Avoid division by zero
        }
    }

    // Method to save tasks using SharedPreferences
    private void saveTasks() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        JSONArray taskArray = new JSONArray();
        for (Task task : taskList) {
            // Serialize the task to JSON and add it to the array
            JSONObject taskObject = new JSONObject();
            try {
                taskObject.put("title", task.getTitle());
                taskObject.put("dueDate", task.getDueDate().getTime()); // Convert Date to long
                taskArray.put(taskObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("taskList", taskArray.toString());
        editor.apply();
    }


    // Method to load tasks from SharedPreferences
    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String taskArrayString = prefs.getString("taskList", "[]");
        try {
            JSONArray taskArray = new JSONArray(taskArrayString);
            for (int i = 0; i < taskArray.length(); i++) {
                JSONObject serializedTask = taskArray.getJSONObject(i);
                String title = serializedTask.getString("title");
                long dueDateMillis = serializedTask.getLong("dueDate");
                Date dueDate = new Date(dueDateMillis); // Convert long to Date
                Task task = new Task(title, dueDate);
                taskList.add(task);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDeleteClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);
                updateCompletionPercentageTextView(); // Update completion percentage
                saveTasks(); // Save the tasks after deleting
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
