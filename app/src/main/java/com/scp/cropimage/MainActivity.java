package com.scp.cropimage;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;


import static android.media.MediaScannerConnection.scanFile;

public class MainActivity extends AppCompatActivity {

    public static WeakReference<MainActivity> weakActivity;

    public static MainActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    Button tslefie,fromgal;
    ImageView image1;
    Button saveimage2;
    Uri uri,img2,pic1;
    ContentValues values;
    Uri imageUri,uri1;
    public static final int Camera_Request=11,REQUEST_GALLERY_IMAGE=22;







    OutputStream outputStream;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tslefie=findViewById(R.id.tself);
        fromgal=findViewById(R.id.fromgal);
        image1=findViewById(R.id.image1);
        saveimage2=findViewById(R.id.saveimage);

        weakActivity = new WeakReference<>(MainActivity.this);



        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);









        fromgal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
            }
        });

       tslefie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                 imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Camera_Request);











            }

        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 0) {
            return;
        }

        if (requestCode == REQUEST_GALLERY_IMAGE) {


            img2 = data.getData();


            if (CropImage.isReadExternalStoragePermissionsRequired(this, img2)) {
                uri = img2;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);


            }


            if (resultCode == Activity.RESULT_OK) {
                startCrop(img2);
            } else {

                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }


        } else if (requestCode == Camera_Request) {




           img2 = imageUri;






            if (resultCode == Activity.RESULT_OK) {
                startCrop(img2);
            } else {

                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }

        }



        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {

                 uri1 = result.getUri();



                Intent intent1 = new Intent(MainActivity.this, activity1.class);
                intent1.putExtra("imagePath", uri1.toString());
                intent1.putExtra("imageUndo", img2.toString());
                startActivity(intent1);

                image1.setImageURI(uri1);








                Toast.makeText(this, "Image Update Successfully", Toast.LENGTH_SHORT).show();









            }







        }





    }










    public void startCrop(Uri image) {
         CropImage.activity(image)
                 .setGuidelines(CropImageView.Guidelines.ON)
                 .setMultiTouchEnabled(true)
                 .setActivityTitle("Edit")
                 .setAllowFlipping(false)

                 .start(this);







    }



    public void imageR(Uri uri){

        image1.setImageURI(uri);

    }











}
