package com.example.alex.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {
    public static final String BASE_URL = "https://api.github.com/";
    public static final String USERNAME = "username";
    public static final String CHOICE = "choice";
    public static final String LAST_SEARCH = "lastSearch";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<Repo> listRepo;
    private String username;
    private String choiceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        username = getIntent().getExtras().getString(USERNAME);
        choiceValue = getIntent().getExtras().getString(CHOICE);

        listRepo = new ArrayList<>();

        initializeToolbar();
        initializeRecyclerView();
        workWithAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDate:
                Collections.sort(listRepo, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                    }
                });
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.sortByName:
                Collections.sort(listRepo, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        return o1.getFullName().compareToIgnoreCase(o2.getFullName());
                    }
                });
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
        }
        return true;
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(LAST_SEARCH, new String[]{username, choiceValue});
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(listRepo));
    }

    private void workWithAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        service.listRepos(username).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.isSuccessful()) {
                    for (Repo r : response.body()) {
                        if (choiceValue.equals("Forks")) {
                            if (r.isFork())
                                listRepo.add(r);
                        } else if (choiceValue.equals("Sources")) {
                            if (!r.isFork())
                                listRepo.add(r);
                        } else if (choiceValue.equals("All"))
                            listRepo.add(r);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    String error = "Ошибка " + response.code() + ": " + response.message();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Toast.makeText(SecondActivity.this, "Интернет-соединение отсутствует",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
