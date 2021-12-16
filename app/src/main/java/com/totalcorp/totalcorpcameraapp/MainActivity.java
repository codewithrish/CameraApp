package com.totalcorp.totalcorpcameraapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.drjacky.imagepicker.ImagePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton btnOpenGallery, btnOpenCamera;
    ImageView imgResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnOpenCamera = findViewById(R.id.btn_open_camera);
        btnOpenGallery = findViewById(R.id.btn_open_gallery);
        imgResult = findViewById(R.id.img_result);
        btnOpenCamera.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_open_camera) {
            openCamera();
        } else if (id == R.id.btn_open_gallery) {
            openGallery();
        }
    }

    private void openGallery() {
        launcher.launch(
                ImagePicker.with(this)
                        .galleryOnly()
                        .crop()
                        .createIntent()
        );
    }

    private void openCamera() {
        launcher.launch(
                ImagePicker.with(this)
                        .cameraOnly()
                        .crop()
                        .createIntent()
        );
    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    imgResult.setImageURI(uri);

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    this.sendBroadcast(mediaScanIntent);

                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });
}