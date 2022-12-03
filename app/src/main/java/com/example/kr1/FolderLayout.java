package com.example.kr1;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderLayout extends LinearLayout implements AdapterView.OnItemClickListener {
    Context context;
    IFolderItemListener folderListener;
    private List<String> path = null;
    private final String root = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final TextView myPath;
    private final ListView lstView;

    public FolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.folderview, this);
        myPath = findViewById(R.id.path);
        lstView = findViewById(R.id.list);
        getDir(root);
    }

    public void setIFolderItemListener(IFolderItemListener folderItemListener) {
        this.folderListener = folderItemListener;
    }

    private void getDir(String dirPath) {
        myPath.setText("Location: " + dirPath);
        List<String> item = new ArrayList<>();
        path = new ArrayList<>();
        File f = new File(dirPath);
        File[] files = f.listFiles((pathname) -> {
            if(pathname.getName().startsWith(".")) {
                return false;
            } else if(pathname.isDirectory()) {
                return true;
            } else if(pathname.getName().endsWith(".png")) {
                return true;
            } else return pathname.getName().endsWith(".jpg");
        });

        if (!dirPath.equals(root)) {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }
        for (File file : files) {
            path.add(file.getPath());
            if (file.isDirectory())
                item.add(file.getName() + "/");
            else
                item.add(file.getName());
        }
        setItemList(item);
    }

    public void setItemList(List<String> item){
        ArrayAdapter<String> fileList = new ArrayAdapter<>(context,
                R.layout.row, item);
        lstView.setAdapter(fileList);
        lstView.setOnItemClickListener(this);
    }

    public void onListItemClick(int position) {
        File file = new File(path.get(position));
        if (file.isDirectory()) {
            if (file.canRead())
                getDir(path.get(position));
            else {
                if (folderListener != null) {
                    folderListener.OnCannotFileRead(file);

                }
            }
        } else {
            if (folderListener != null) {
                folderListener.OnFileClicked(file);
            }
        }
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        onListItemClick(arg2);
    }
}
