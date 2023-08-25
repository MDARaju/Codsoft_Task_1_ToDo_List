package com.todo3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private OnTaskClickListener onTaskClickListener;

    public interface OnTaskClickListener {
        void onTaskClick(int position);
        void onDeleteClick(int position);
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.taskList = tasks;
        this.onTaskClickListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view, onTaskClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitleTextView.setText(task.getTitle());

        int backgroundColor = task.isCompleted() ? R.color.lavender : android.R.color.white;
        holder.itemView.setBackgroundResource(backgroundColor);

        if (task.getDueDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            holder.dueDateTextView.setText("Due date: " + dateFormat.format(task.getDueDate()));
            holder.dueDateTextView.setVisibility(View.VISIBLE);
        } else {
            holder.dueDateTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTaskClickListener != null) {
                    onTaskClickListener.onTaskClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitleTextView;
        ImageView deleteImageView;
        TextView dueDateTextView;

        TaskViewHolder(View itemView, final OnTaskClickListener onTaskClickListener) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.taskTitleTextView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTaskClickListener != null) {
                        onTaskClickListener.onDeleteClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
