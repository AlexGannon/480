package android.com.freezeframe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PickGlassesActivity extends AppCompatActivity {
    Button button = null;
    Uri selectedImage = null;
    Context context = null;
    int count = 0;
    ProgressDialog progressDialog = null;
    ProgressBar pb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_glasses);

        button = (Button) findViewById(R.id.button);
        context = this;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0)
                    getImage(v.getContext());
                else
                {


                }
                count++;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 76:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                }

                break;
            case 79:
                if (resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                }
                break;
        }
        pb.setVisibility(View.VISIBLE);
        Intent i = new Intent(context, GlassActivity.class);
        i.putExtra("userImage", selectedImage.toString());
        finish();
        startActivity(i);
    }

    public void getImage(Context context)
    {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Pictures Option");
        myAlertDialog.setMessage("Select Picture Mode");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 79);
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 76);
            }
        });
        myAlertDialog.show();
    }


}
