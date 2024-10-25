package com.example.gitapp;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

//add

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;
    private List<Rep> repoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        inputText = findViewById(R.id.inputText);
        searchButton = findViewById(R.id.Search);
        recyclerView = findViewById(R.id.recyclerView);

        // 初始化 RecyclerView
        repoList = new ArrayList<>();
        repoAdapter = new RepoAdapter(repoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(repoAdapter);

        // 点击按钮时进行搜索操作
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputText.getText().toString();
                if (!username.isEmpty()) {
                    fetchGitHubRepos(username);  // 调用方法传递用户名
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 使用 HttpURLConnection 发送网络请求并解析 JSON 数据
    private void fetchGitHubRepos(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.github.com/users/" + username + "/repos");  // Android 模拟器上访问
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // 解析 JSON 数据并更新 RecyclerView
                         final List<Rep> repos = parseReposFromJson(response.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (repos.isEmpty()) {
                                    Toast.makeText(MainActivity.this, "No public repositories found", Toast.LENGTH_SHORT).show();
                                } else {
                                    repoList.clear();
                                    repoList.addAll(repos);
                                    repoAdapter.notifyDataSetChanged();  // 更新 RecyclerView
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 解析 JSON 数据
    private List<Rep> parseReposFromJson(String json) throws JSONException {
        List<Rep> repos = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject repoJson = jsonArray.getJSONObject(i);
            String name = repoJson.getString("name");
            String description = repoJson.optString("description", "No description");
            // 确保使用 stargazers_count
            int stars = repoJson.getInt("stargazers_count");
            repos.add(new Rep(name, description, stars));
        }
        return repos;
    }
}