package com.example.kr1;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.Objects;

public class ProductEditorActivity extends AppCompatActivity {

    public static String ID = "";
    public final static String SELECTED_ITEM = "SELECTED_ITEM";
    public final static String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public final static String CHANGE_NAME = "CHANGE_NAME";
    public final static String CREATE_NAME = "CREATE_NAME";
    public static Product SelectedProduct;
    Product product;
    String newName;
    boolean isResultOtherActivity = true;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                    File file = new File(accessMessage);
                    if(file.exists()) {
                        ImageView i = findViewById(R.id.productImage);
                        ImageView i2 = findViewById(R.id.c_productImage);
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        if (i == null) {
                            ProductCreatorFragment fragment1 = (ProductCreatorFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.detailCreateFragment);
                            fragment1.setBitmap(file.getAbsolutePath());
                            i2.setImageBitmap(bitmap);
                        } else if (i2 == null) {
                            ProductEditorFragment fragment = (ProductEditorFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.detailFragment);
                            fragment.setBitmap(file.getAbsolutePath());
                            i.setImageBitmap(bitmap);
                        }
                        isResultOtherActivity = false;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        Intent intent = getIntent();
        String type_fragment = intent.getStringExtra(FRAGMENT_TYPE);
        if (Objects.equals(type_fragment, "CREATE")) {
            Intent intent1 = getIntent();
            newName = intent1.getStringExtra(CREATE_NAME);
            setContentView(R.layout.activity_detail_create);
        } else {
            setContentView(R.layout.activity_detail);
            Bundle extras = getIntent().getExtras();
            if (extras != null)
                product = (Product) extras.getSerializable(SELECTED_ITEM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductEditorFragment fragment = (ProductEditorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        ProductCreatorFragment fragment1 = (ProductCreatorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailCreateFragment);
        if(fragment1 != null) {
            fragment1.setName(newName);
        }
        if(fragment != null) {
            if(isResultOtherActivity) {
                fragment.setProduct(product);
            } else {
                isResultOtherActivity = true;
            }
        }
    }

    public void returnMessage(String mes) {
        sendMessage(mes);
    }

    private void sendMessage(String message){
        Intent data = new Intent();
        data.putExtra(MainActivity.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();
    }

    public void sendProduct(int src, String name, Integer count, String url) {
        Intent data = new Intent();
        data.putExtra(ACCESS_MESSAGE, "CREATE");
        data.putExtra(SELECTED_ITEM, new Product(src, url, name, count));
        setResult(RESULT_OK, data);
        finish();
    }

    public void sendProductChange(int src, String oldName, String name, Integer count, String url) {
        Intent data = new Intent();
        data.putExtra(ACCESS_MESSAGE, "CHANGE");
        data.putExtra(CHANGE_NAME, oldName);
        data.putExtra(SELECTED_ITEM, new Product(src, url, name, count));
        setResult(RESULT_OK, data);
        finish();
    }

    public void startFileManager() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);//permission request code is just an int
            } else {
                Intent intent = new Intent(getApplicationContext(), SampleFolderActivity.class);
                mStartForResult.launch(intent);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent(getApplicationContext(), SampleFolderActivity.class);
                mStartForResult.launch(intent);
            }
        }
    }
}