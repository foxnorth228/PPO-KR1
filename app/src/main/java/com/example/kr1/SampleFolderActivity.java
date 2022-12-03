package com.example.kr1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;

public class SampleFolderActivity extends Activity implements IFolderItemListener {
    FolderLayout localFolders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);

        localFolders = findViewById(R.id.localfolders);
        localFolders.setIFolderItemListener(this);
        //localFolders.setDir("./sys");//change directory if u want,default is root
    }

    public void OnCannotFileRead(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon)
                .setTitle(
                        "[" + file.getName()
                                + "] folder can't be read!")
                .setPositiveButton("OK",
                        (dialog, which) -> finish()).show();
    }

    public void OnFileClicked(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon)
                .setTitle("[" + file.getName() + "]")
                .setPositiveButton("OK",
                        (dialog, which) -> {
                            Intent data = new Intent();
                            data.putExtra(MainActivity.ACCESS_MESSAGE, file.getAbsolutePath());
                            setResult(RESULT_OK, data);
                            finish();
                        }).show();
    }
}