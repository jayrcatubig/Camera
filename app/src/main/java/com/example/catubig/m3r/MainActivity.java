package com.example.catubig.m3r;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView ivCamera,ivGallery,ivPhoto;
    String TempTest;
    public static final int REQUEST_IMAGE_CAPTURE = 20;
    public static final int REQUEST_SAVE_IMAGE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivCamera = (ImageView)findViewById(R.id.ivCamera);
        ivGallery = (ImageView)findViewById(R.id.ivGallery);
        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
        ivCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                TempTest = "TakePicture";
            }
        });
        ivGallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispatchSavePictureIntent();
                TempTest = "DisplayImage";
            }
        });
    }
    public void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String pictureName  = getPictureName();
        File imageFile = new File(pictureDirectory, pictureName);
        Uri pictureUri = Uri.fromFile(imageFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private String getPictureName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp =  simpleDateFormat.format(new Date());
        return "IMG_M3R" + timeStamp + ".jpg";
    }


    public void dispatchSavePictureIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, REQUEST_SAVE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (TempTest== "TakePicture") {
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //ivPhoto.setImageBitmap(imageBitmap);

                Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
            }
            else if(TempTest == "DisplayImage") {
                Uri imageUri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    ivPhoto.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
