package com.hendraaagil.todolistapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private Context context;
    private ArrayList<TodoItem> todoItems;
    private OnBtnDoneClick btnDoneClick;
    private OnBtnEditClick btnEditClick;
    private OnBtnDeleteClick btnDeleteClick;

    public interface OnBtnDoneClick {
        void btnDoneClick(int position) throws JSONException;
    }

    public void setBtnDoneClick(OnBtnDoneClick btnDoneClick) {
        this.btnDoneClick = btnDoneClick;
    }

    public interface OnBtnEditClick {
        void btnEditClick(int position);
    }

    public void setBtnEditClick(OnBtnEditClick btnEditClick) {
        this.btnEditClick = btnEditClick;
    }

    public interface OnBtnDeleteClick {
        void btnDeleteClick(int position);
    }

    public void setBtnDeleteClick(OnBtnDeleteClick btnDeleteClick) {
        this.btnDeleteClick = btnDeleteClick;
    }

    public TodoAdapter(Context context, ArrayList<TodoItem> todoItems) {
        this.context = context;
        this.todoItems = todoItems;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem item = todoItems.get(position);

        String completed = item.getCompletedAt();
        String name = item.getName();
        boolean isComplete = item.isComplete();

        holder.txtVwDateCompleted.setText("Completed At : " + formatDate(completed));
        holder.txtVwTodoName.setText(name);
        holder.btnDone.setEnabled(!isComplete);
        holder.btnEdit.setEnabled(!isComplete);
    }

    @SuppressLint("SimpleDateFormat")
    private String formatDate(String date) {
        System.out.println(date);
        String newDate = "-";
        if (date != null) {
            try {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(date);
                newDate = new SimpleDateFormat("yyyy-MM-dd").format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return newDate;
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        public TextView txtVwTodoName, txtVwDateCompleted;
        public Button btnDone, btnEdit, btnDelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVwTodoName = itemView.findViewById(R.id.txtVwTodoName);
            txtVwDateCompleted = itemView.findViewById(R.id.txtVwDateCompleted);
            btnDone = itemView.findViewById(R.id.btnDone);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDone.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (btnDoneClick != null && position != RecyclerView.NO_POSITION) {
                    try {
                        btnDoneClick.btnDoneClick(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (btnEditClick != null && position != RecyclerView.NO_POSITION) {
                    btnEditClick.btnEditClick(position);
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (btnDeleteClick != null && position != RecyclerView.NO_POSITION) {
                    btnDeleteClick.btnDeleteClick(position);
                }
            });
        }
    }
}
