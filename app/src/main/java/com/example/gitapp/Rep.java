package com.example.gitapp;

import java.util.ArrayList;
import java.util.List;

public class Rep {
    private String name;         // 项目名称
    private String description;  // 项目简介
    private int stargazersCount; //follower


    public Rep(String name, String description, int stargazersCount) {
        this.name = name;
        this.description = description;
        this.stargazersCount = stargazersCount;
    }

    // Getter 方法
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    @Override
    public String toString() {
        return "Repository Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Stars: " + stargazersCount + "\n";
    }

    // 静态方法，用于存储多个项目
    public static List<Rep> createRepoList() {
        return new ArrayList<>(); // 返回一个新的List，用于存储多个项目
    }
}
