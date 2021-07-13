package com.scp.cropimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class activity1 extends AppCompatActivity {



    Button saveimage,undo;
    ImageView image1;
    OutputStream outputStream;
    Uri fileUri,uri1;
    String img2;

    MainActivity main = new MainActivity();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);
        this.setTitle("Edit");






        ActivityCompat.requestPermissions(activity1.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(activity1.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        image1 = findViewById(R.id.image1);
        saveimage = findViewById(R.id.saveimage);
        undo = findViewById(R.id.undo);


        Intent intent = getIntent();
        String image_path= intent.getStringExtra("imagePath");
         img2 = intent.getStringExtra("imageUndo");






          fileUri = Uri.parse(image_path);

       final  Uri fileUndo = Uri.parse(img2);






        image1.setImageURI(fileUri);


        undo.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {






                   CropImage.activity(fileUndo)
                           .setGuidelines(CropImageView.Guidelines.ON)
                           .setMultiTouchEnabled(true)
                           .setActivityTitle("Edit")
                           .setAllowFlipping(false)
                           .start(activity1.this);












            }
        });



        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable draw = (BitmapDrawable) image1.getDrawable();
                Bitmap bitmap = draw.getBitmap();


                File filepath = Environment.getExternalStorageDirectory();



                File dir = new File(filepath.getAbsolutePath() + "/ImageCrop-TC/");
                dir.mkdirs();
                File file = new File(dir,System.currentTimeMillis()+".jpg");
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(getApplicationContext(),"Image Save Successfully",Toast.LENGTH_SHORT).show();
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent1.setData(Uri.fromFile(file));
                sendBroadcast(intent1);


            }});






    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode==0)
        {
            return;
        }








        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if(resultCode==RESULT_OK){


                fileUri = result.getUri();

                image1.setImageURI(result.getUri());

                MainActivity.getmInstanceActivity().imageR(fileUri);




                Toast.makeText(this, "Image Update Successfully", Toast.LENGTH_SHORT).show();


            }



        }





    }




}
