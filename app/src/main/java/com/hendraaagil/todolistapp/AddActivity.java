package com.hendraaagil.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AddActivity extends AppCompatActivity {
    private TextInputLayout txtLayoutName;
    private TextInputEditText txtInputName;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle("Add Todo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtLayoutName = findViewById(R.id.txtLayoutName);
        txtInputName = findViewById(R.id.txtInputName);
        btnSubmit = findViewById(R.id.btnSubmit);

        txtInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtInputName.getText().toString().isEmpty()) {
                    txtLayoutName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSubmit.setOnClickListener(v -> {
            if (validate()) {
                new addTodo().execute();
            }
        });
    }

    private boolean validate() {
        if (txtInputName.getText().toString().isEmpty()) {
            txtLayoutName.setErrorEnabled(true);
            txtLayoutName.setError("This field is required!");
            return false;
        }
        return true;
    }

    public class addTodo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.0.2.2:5000/api/TodoItems");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                JSONObject object = new JSONObject();
                object.put("id", 0);
                object.put("name", txtInputName.getText().toString());
                object.put("isComplete", false);
                object.put("completedAt", null);

                OutputStream outputStream = connection.getOutputStream();
                byte[] input = object.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                System.out.println(bufferedReader.readLine());

                runOnUiThread(() -> Toast.makeText(AddActivity.this, "Data Successfully Added!", Toast.LENGTH_SHORT).show());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AddActivity.this, "Gagal", Toast.LENGTH_SHORT).show());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(AddActivity.this, MainActivity.class));
        }
    }
}