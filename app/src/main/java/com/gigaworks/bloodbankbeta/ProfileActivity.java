package com.gigaworks.bloodbankbeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Arch on 20-04-2017.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView Name,Email,Phone,Address,Blood;
    private SharedPreferences sharedPreferences;
    private FloatingActionButton fab;
    private Button saveButton;
    private ImageView profilePic;
    private ArrayList<String> imgUrl;

    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    private static final String UPLOAD_URL="https://gigaworks.000webhostapp.com/getImage.php";
    private static final String GET_IMAGE_URL="https://gigaworks.000webhostapp.com/downloadImage.php";

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        requestStoragePermission();

        Name=(TextView)findViewById(R.id.tvNumber1);
        Email=(TextView)findViewById(R.id.tvNumber3);
        Phone=(TextView)findViewById(R.id.tvNumber2);
        Address=(TextView)findViewById(R.id.tvNumber4);
        Blood=(TextView)findViewById(R.id.tvNumber5);
        profilePic=(ImageView)findViewById(R.id.iv_profile);
        imgUrl=new ArrayList<>();
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
        saveButton=(Button)findViewById(R.id.btn_save);
        saveButton.setOnClickListener(this);

        sharedPreferences=getApplicationContext()
                .getSharedPreferences("bloodbank.pref",MODE_PRIVATE);


        Name.setText(sharedPreferences.getString("name",""));
        Email.setText(sharedPreferences.getString("email",""));
        Phone.setText(sharedPreferences.getString("phone",""));
        Address.setText(sharedPreferences.getString("place",""));
        Blood.setText(sharedPreferences.getString("bloodgroup",""));

        String imgUrl=sharedPreferences.getString("url","");

        if(!imgUrl.equals("")){
            new DownloadImageTask(profilePic).execute(imgUrl);
        }
        else {

        }

    }

    @Override
    public void onClick(View v){

        if(v==fab){
            showFileChooser();
        }
        else if(v==saveButton){
            uploadMultipart();
        }
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void uploadMultipart() {

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("email",sharedPreferences.getString("email",""))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

}

class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String urldisplay = params[0];
        Bitmap mIcon11 = null;
        if(urldisplay!=null){
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap==null){
            bmImage.setImageResource(R.drawable.default_profile_pic);
        }
        else {
            bmImage.setImageBitmap(bitmap);
        }
    }
}
