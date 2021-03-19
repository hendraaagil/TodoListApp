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

public class EditActivity extends AppCompatActivity {
    private TextInputLayout txtLayoutName;
    private TextInputEditText txtInputName;
    private Button btnSubmit;
    private int id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setTitle("Edit Todo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.id = intent.getIntExtra("id", 0);
        this.name = intent.getStringExtra("name");

        txtLayoutName = findViewById(R.id.txtLayoutNameEdit);
        txtInputName = findViewById(R.id.txtInputNameEdit);
        btnSubmit = findViewById(R.id.btnSubmitEdit);
        txtInputName.setText(this.name);

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
                try {
                    JSONObject object = new JSONObject();
                    object.put("id", this.id);
                    object.put("name", txtInputName.getText().toString());
                    object.put("isComplete", false);
                    object.put("completedAt", null);

                    new editAct(object).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public class editAct extends AsyncTask<Void, Void, Void> {
        JSONObject object;

        public editAct(JSONObject object) {
            this.object = object;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.0.2.2:5000/api/TodoItems/" + this.object.getString("id"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                OutputStream outputStream = connection.getOutputStream();
                byte[] input = this.object.toString().getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                System.out.println(this.object);
                System.out.println(bufferedReader.readLine());

                runOnUiThread(() -> Toast.makeText(EditActivity.this, "Data Successfully Edited!", Toast.LENGTH_SHORT).show());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(EditActivity.this, "Gagal", Toast.LENGTH_SHORT).show());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(EditActivity.this, MainActivity.class));
        }
    }
}