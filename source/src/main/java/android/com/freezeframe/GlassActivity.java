package android.com.freezeframe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.InputStream;

public class GlassActivity extends AppCompatActivity {
    static int eyeLevelLX;
    static int eyeLevelLY = -1;
    static int eyeLevelRX;
    static int eyeLevelRY;
    static ImageView imageView = null;
    static Context context = null;
    Bitmap myImage = null;
    Button btn = null;
    static int width = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glass);

        //imageView = (ImageView) findViewById(R.id.glasses);
        context = this;
        FaceDetector detector = MainActivity.detector;

        if(!detector.isOperational())
        {
            detector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build();
        }

        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, 5151);

        InputStream stream = getResources().openRawResource(R.raw.dd);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);


/*
        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();
        */


        // Create a frame from the bitmap and run face detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);

        if(faces.size() == 0)
        {
            detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

            frame = new Frame.Builder().setBitmap(bitmap).build();
            faces = detector.detect(frame);

        }

        CustomView overlay = (CustomView) findViewById(R.id.customView);
        overlay.setContent(bitmap, faces);

        detector.release();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        System.out.println("The Width of the screen is: " + width);
    }

}
