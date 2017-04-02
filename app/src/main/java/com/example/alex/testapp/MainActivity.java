package com.example.alex.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName;
    private Button button;
    private Spinner spinner;
    private String[] lastSearch = new String[2];
    private String[] previousSearch = new String[2];
    private TextView last;
    private TextView previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            previousSearch = savedInstanceState.getStringArray("prev");
            lastSearch = savedInstanceState.getStringArray("last");
        }

        userName = (EditText) findViewById(R.id.editText);
        spinner = (Spinner) findViewById(R.id.spinner);

        button = (Button) findViewById(R.id.button);
        last = (TextView) findViewById(R.id.lastSearchView);
        previous = (TextView) findViewById(R.id.previousSearchView);
        button.setOnClickListener(this);
        last.setOnClickListener(this);
        previous.setOnClickListener(this);

        /*
        поля последних запросов заполняются в двух местах:
        1 - в onActivityResult после получения из второй активити
        2 - здесь, после вызова onSaveInstanceState.
        возможно, есть другой способ, но если убрать вызов из onActivityResult,
        поля вообще не заполняются
        */
        setTextToLastSearchFields();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);

        switch (v.getId()) {
            case R.id.button:
                intent.putExtra(SecondActivity.USERNAME, userName.getText().toString());
                intent.putExtra(SecondActivity.CHOICE, spinner.getSelectedItem().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.lastSearchView:
                intent.putExtra(SecondActivity.USERNAME, lastSearch[0]);
                intent.putExtra(SecondActivity.CHOICE, lastSearch[1]);
                startActivity(intent);
                break;
            case R.id.previousSearchView:
                intent.putExtra(SecondActivity.USERNAME, previousSearch[0]);
                intent.putExtra(SecondActivity.CHOICE, previousSearch[1]);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                previousSearch = lastSearch;
                lastSearch = data.getStringArrayExtra(SecondActivity.LAST_SEARCH);
            }
        setTextToLastSearchFields();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArray("prev", previousSearch);
        outState.putStringArray("last", lastSearch);
    }

    private void setTextToLastSearchFields() {
        if (previousSearch[0] != null)
            previous.setText(previousSearch[0] + " | " + previousSearch[1]);
        if (lastSearch[0] != null)
            last.setText(lastSearch[0] + " | " + lastSearch[1]);
    }
}
