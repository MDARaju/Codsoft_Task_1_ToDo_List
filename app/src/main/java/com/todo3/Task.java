package com.todo3;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private String title;
    private boolean completed;
    private Date dueDate;

    public Task(String title, Date dueDate) {
        this.title = title;
        this.completed = false;
        this.dueDate = dueDate;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    // Serialize the task to a JSON object
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("completed", completed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // Deserialize a JSON object to create a Task instance
    public static Task deserialize(JSONObject jsonObject) {
        try {
            String title = jsonObject.getString("title");
            boolean completed = jsonObject.getBoolean("completed");

            // Deserialize the due date string and convert it to a Date object
            String dueDateString = jsonObject.optString("dueDate", null);
            Date dueDate = null;
            if (dueDateString != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    dueDate = dateFormat.parse(dueDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Task task = new Task(title, dueDate);
            task.setCompleted(completed);
            return task;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
