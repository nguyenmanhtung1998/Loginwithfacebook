package com.example.loginwithfacebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChucnangActivity extends AppCompatActivity {
    EditText editTextten,editTextmota,editTextduongdan;
Button buttonlink,buttonimage,buttonpickvideo,buttonsharevideo;
ImageView imageView;
VideoView videoView;
    Bitmap bitmap;
ShareDialog shareDialog;
Uri selecvideo;
ShareLinkContent shareLinkContent;
public static int Select_image=1;
public static int Pickvideo=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chucnang);
        anhxa();
        shareDialog=new ShareDialog(ChucnangActivity.this);
        buttonlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareLinkContent=new ShareLinkContent.Builder().
                            setContentTitle(editTextten.getText().toString()).
                            setContentDescription(editTextmota.getText().toString()).
                            setContentUrl(Uri.parse(editTextduongdan.getText().toString())).build();

                }
                shareDialog.show(shareLinkContent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Select_image);
            }
        });
        buttonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        });
        buttonpickvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent,Pickvideo);
            }
        });
        buttonsharevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareVideo video= new ShareVideo.Builder()
                        .setLocalUrl(selecvideo)
                        .build();
                ShareVideoContent content = new ShareVideoContent.Builder()
                        .setVideo(video)
                        .build();
                shareDialog.show(content);
                videoView.stopPlayback();
            }
        });
    }

    private void anhxa() {
        editTextten=findViewById(R.id.edittexttieude);
        editTextmota=findViewById(R.id.edittextmota);
        editTextduongdan=findViewById(R.id.edittextduongdan);
        buttonlink=findViewById(R.id.buttonsherelink);
        buttonimage=findViewById(R.id.Shareimage);
        buttonpickvideo  =findViewById(R.id.Pickvideo);
        buttonsharevideo=findViewById(R.id.Sharevideo);
        imageView=findViewById(R.id.imageview);
        videoView=findViewById(R.id.videoview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==Select_image&&resultCode==RESULT_OK){
            try {
                InputStream inputStream=getContentResolver().openInputStream(data.getData());
               bitmap= BitmapFactory.decodeStream(inputStream);
               imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode==Pickvideo&&resultCode==RESULT_OK){
            selecvideo=data.getData();
            videoView.setVideoURI(selecvideo);

            videoView.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
