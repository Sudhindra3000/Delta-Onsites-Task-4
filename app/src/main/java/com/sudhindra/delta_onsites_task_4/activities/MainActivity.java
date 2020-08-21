package com.sudhindra.delta_onsites_task_4.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.sudhindra.delta_onsites_task_4.adapters.FileAdapter;
import com.sudhindra.delta_onsites_task_4.databinding.ActivityMainBinding;
import com.sudhindra.delta_onsites_task_4.models.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 10;

    private ArrayList<FileItem> fileItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkStoragePermission();
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
            else
                getFiles();
        } else
            getFiles();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFiles();
            }
        }
    }

    private void getFiles() {
        File rootDir = Environment.getExternalStorageDirectory();
        File[] files = rootDir.listFiles();
        if (files != null) {
            Arrays.sort(files);
            fileItems = new ArrayList<>();
            for (File file : files) {
                fileItems.add(new FileItem(file));
            }

            buildRecyclerView();
        } else {
            Toast.makeText(this, "Failed to get Files", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildRecyclerView() {
        FileAdapter adapter = new FileAdapter(this);
        adapter.setFileItems(fileItems);

        binding.allFiles.setHasFixedSize(true);

        ((SimpleItemAnimator) Objects.requireNonNull(binding.allFiles.getItemAnimator())).setSupportsChangeAnimations(false);
        binding.allFiles.setAdapter(adapter);
        binding.allFiles.setLayoutManager(new LinearLayoutManager(this));
    }
}