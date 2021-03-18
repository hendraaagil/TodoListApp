package com.hendraaagil.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnBtnDoneClick, TodoAdapter.OnBtnEditClick, TodoAdapter.OnBtnDeleteClick {
    public JSONArray todos;
    public RecyclerView recyclerView;
    public TodoAdapter todoAdapter;
    public ArrayList<TodoItem> todoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoItems = new ArrayList<>();

        new getTodos().execute();

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddActivity.class));
        });
    }

    @Override
    public void btnDoneClick(int position) {
        TodoItem item = todoItems.get(position);

        System.out.println(item.getName());
    }

    @Override
    public void btnEditClick(int position) {
        TodoItem item = todoItems.get(position);

        System.out.println(item.getId());
    }

    @Override
    public void btnDeleteClick(int position) {
        TodoItem item = todoItems.get(position);

        System.out.println(item.getId());
    }

    @SuppressLint("StaticFieldLeak")
    public class getTodos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.0.2.2:5000/api/TodoItems");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                todos = new JSONArray(response.toString());
                System.out.println(todos);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Gagal", Toast.LENGTH_SHORT).show());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                for (int i = 0; i < todos.length(); i++) {
                    JSONObject todo = todos.getJSONObject(i);

                    int id = todo.getInt("id");
                    String name = todo.getString("name");
                    String completedAt = todo.getString("completedAt");
                    boolean isComplete = todo.getBoolean("isComplete");

                    System.out.println(completedAt);

                    todoItems.add(new TodoItem(id, name, completedAt, isComplete));
                }

                todoAdapter = new TodoAdapter(MainActivity.this, todoItems);
                recyclerView.setAdapter(todoAdapter);
                todoAdapter.setBtnDoneClick(MainActivity.this);
                todoAdapter.setBtnEditClick(MainActivity.this);
                todoAdapter.setBtnDeleteClick(MainActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}