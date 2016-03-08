package android.com.freezeframe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GlassActivity extends AppCompatActivity {
    static int eyeLevelLX;
    static int eyeLevelLY;
    static int eyeLevelRX;
    static int eyeLevelRY;
    static ImageView imageView = null;
    static Context context = null;
    Bitmap myImage = null;
    Button btn = null;
    static int width = 0;
    private int count = 0;
    static int cheekR, cheekL;
    Bitmap bitmap;
    Uri selectedImage = null;
    Button button = null;
    ProgressBar pb = null;
    RegisterAsyncTask rtask;
    boolean foundFace = false;
    GestureDetector gestureDetector = null;
    static int glassWidth = 0;
    String mCurrentPhotoPath;
    File photoFile = null;
    boolean isFromGallery = false;
    String imgPath = "";
    SharedPreferences sharedpreferences;
    boolean gettingImage = false;
    int framecount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glass);


        imageView = (ImageView) findViewById(R.id.imageView);
        pb = (ProgressBar) findViewById(R.id.pro);
        gestureDetector = new GestureDetector(
                new SwipeGestureDetector());
        sharedpreferences = getSharedPreferences("FreezeFramePrefs", Context.MODE_PRIVATE);

        context = this;
        //if(!gettingImage)
            getImage(this);
    }


    public static void drawFrames() {
        imageView.setImageResource(R.drawable.glasseseleven);
        imageView.requestLayout();
        int center = eyeLevelLX + ((eyeLevelRX - eyeLevelLX) / 2);
        int w = ((eyeLevelRX - eyeLevelLX) / 2) * 4;
        Double h = w * .315;

        imageView.getLayoutParams().width = w;
        int temp = h.intValue();
        if ((h - temp) > .5)
            temp = temp + 1;
        imageView.getLayoutParams().height = temp;
        width = w;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        int tempHeight = temp / 2;
        params.setMargins(center - (w / 2), eyeLevelRY - ((Double) (tempHeight * .80)).intValue(), 0, 0);


        //if (cheekL - cheekR > 6)
        if(eyeLevelLY - eyeLevelRY > 6){
            System.out.println("Here at left higher");
           /* //imageView.setPivotX(center);
            int heightDiff = cheekL - cheekR;
            int widthApart = eyeLevelRX - eyeLevelLX;
            System.out.println("Width: " + widthApart + " height: " + heightDiff);
            double rotate = Math.atan(((float)heightDiff) / widthApart);
            rotate = Math.toDegrees(rotate);
            System.out.println("Rotate by: " + rotate + " : " + (360 - rotate));
            imageView.setRotation(360 - (float)rotate);
            params.setMargins(center - (w/2), ((eyeLevelRY + eyeLevelLY)/2) - ((Double) (tempHeight * .70)).intValue(), 0, 0);
        */
            //int heightDiff = cheekL - cheekR;
            int heightDiff = cheekL - cheekR;
            int widthApart = eyeLevelRX - eyeLevelLX;
            double rotate = Math.atan(((float) heightDiff) / widthApart);
            rotate = Math.toDegrees(rotate);
            System.out.println("Rotate by: " + rotate + " : " + (360 - rotate));
            imageView.setRotation(360 - (float) rotate);

            Double offby = w - (w * Math.cos(Math.toRadians(rotate)));
            //Double val = eyeLevelLX + ((eyeLevelRX-eyeLevelRX)/2.0) - (w/4.0) - offby;
            //Double val = eyeLevelLX + ((eyeLevelRX-eyeLevelRX)/2.0) - (w * Math.sin(90 - rotate))/4;
            //Double val = eyeLevelLX + ((eyeLevelRX-eyeLevelLX)/2.0) - ((w * Math.cos(rotate))/2.0) - offby;
            System.out.println("The new height is: " + ((w * Math.cos(rotate))) + " The overall width is: " + w + " Cos(Theta) is: " + Math.cos(Math.toRadians(rotate)));
            Double val = eyeLevelLX + ((eyeLevelRX - eyeLevelLX) / 2.0) - ((w * Math.cos(Math.toRadians(rotate))) / 2.0) - offby;
            int roundedVal;
            if (val - val.intValue() >= .5)
                roundedVal = val.intValue() + 1;
            else
                roundedVal = val.intValue();
            params.setMargins(roundedVal, ((eyeLevelRY + eyeLevelLY) / 2) - ((Double) (tempHeight * .80)).intValue(), 0, 0);
        }
        //else if (cheekR - cheekL > 6)
        else if(eyeLevelRY - eyeLevelLY > 6){
            System.out.println("Here at Right higher");
            //int heightDiff = cheekR - cheekL;
            int heightDiff = eyeLevelRY - eyeLevelLY;
            int widthApart = eyeLevelRX - eyeLevelLX;
            double rotate = Math.atan(((float) heightDiff) / widthApart);
            rotate = Math.toDegrees(rotate);
            System.out.println("Rotate by: " + rotate + " : " + (360 - rotate));
            imageView.setRotation((float) rotate);

            Double offby = w - w * Math.sin(Math.toRadians(90 - rotate));
            //Double val = eyeLevelLX + ((eyeLevelRX-eyeLevelRX)/2.0) - (w/4.0) - offby;
            //Double val = eyeLevelLX + ((eyeLevelRX-eyeLevelRX)/2.0) - (w * Math.sin(90 - rotate))/4;

            Double val = eyeLevelLX + ((eyeLevelRX - eyeLevelLX) / 2.0) - ((w * Math.sin(Math.toRadians(90 - rotate))) / 2.0) - offby;
            System.out.println("The left x is: " + eyeLevelLX + " Margin is: " + val + " offby is: " + offby);
            int roundedVal;
            if (val - val.intValue() >= .5)
                roundedVal = val.intValue() + 1;
            else
                roundedVal = val.intValue();
            params.setMargins(roundedVal, ((eyeLevelLY + eyeLevelRY) / 2) - ((Double) (tempHeight * .80)).intValue(), 0, 0);


            //params.setMargins(center - (w / 2), ((eyeLevelLY + eyeLevelRY) / 2) - ((Double) (tempHeight * .70)).intValue(), 0, 0);
        }
    }


    public void getImage(Context context) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Pictures Option");
        myAlertDialog.setMessage("Select Picture Mode");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, 79);
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {

                        System.out.println("Error in creating file");
                    }

                    if (photoFile != null) {
                        System.out.println("Photo Created Successfully");
                        Uri testUri = Uri.fromFile(photoFile);
                        if(testUri ==  null)
                            System.out.println("Test URI is null");
                        else
                            System.out.println("Test URI is NOT null");

                        sharedpreferences.edit().putString("photoPath", mCurrentPhotoPath).commit();
                        //sharedpreferences.edit().putString("gettingImage", "true").commit();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, testUri);
                        startActivityForResult(takePictureIntent, 76);
                    }
                }


            }
        });
        myAlertDialog.show();
    }

    public void setGlasses() {

        if(selectedImage != null) {

            try {


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                String myPath = getRealPathFromURI(selectedImage);
                //BitmapFactory.decodeFile(selectedImage.getPath(), options);
                BitmapFactory.decodeFile(myPath, options);
                System.out.println("Image Path is: " + selectedImage.getPath());
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;




                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                System.out.println("Height: " + imageHeight + " Width: " + imageWidth);
                System.out.println("Screen Height: " + height + " Screen Width: " + width);

                if(imageHeight > 1.5 * height || imageWidth > 1.5 * width) {
                    bitmap = readBitmap(selectedImage);
                    System.out.println("Image Larger than this");
                }
                else {
                    System.out.println("Image is Okay");
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bitmap = readBitmap(selectedImage);
            ExifInterface exif = null;
            File finalFile = null;

            if(isFromGallery) {
            String realPath;

            Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            realPath = cursor.getString(idx);

            finalFile = new File(realPath);
            }
            else {
                finalFile = photoFile;
                //Insert Into Gallery
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put("_data", getRealPathFromURI(selectedImage));
                ContentResolver cr = getContentResolver();
                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
            try {
                exif = new ExifInterface(finalFile.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
                rotation = 90;
            else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
                rotation = 180;
            else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
                rotation = 270;
            else
                rotation = 0;
            int rotationInDegrees = rotation;


            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.preRotate(rotationInDegrees);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            FaceDetector detector = MainActivity.detector;

            if (!detector.isOperational()) {

                detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();
            }
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Face> faces = detector.detect(frame);

            if (faces.size() == 0) {

                System.out.println("No Faces Here");
                detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .build();
                frame = new Frame.Builder().setBitmap(bitmap).build();
                faces = detector.detect(frame);

            }

            if (faces.size() == 0) {

                foundFace = false;
            } else {
                foundFace = true;
                System.out.println("There are faces");
                CustomView overlay = (CustomView) findViewById(R.id.customView);
                overlay.setContent(bitmap, faces);

                detector.release();
            }
        }

    }

    private static int rotationDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;
        else
            return 0;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 76:
                if (resultCode == RESULT_OK) {
                    //selectedImage = imageReturnedIntent.getData();
                    try {
                        mCurrentPhotoPath = sharedpreferences.getString("photoPath", "");
                        sharedpreferences.edit().remove("photoPath").commit();
                        photoFile = new File(new URI(mCurrentPhotoPath));

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    if(photoFile.exists())
                    {
                        System.out.println("Photo Exists");
                        selectedImage = Uri.fromFile(photoFile);
                    }
                    else
                        System.out.println("Photo Does Not Exist");
                }

                break;
            case 79:
                if (resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    isFromGallery = true;
                }
                break;
        }

            pb.setVisibility(View.VISIBLE);
            rtask = new RegisterAsyncTask();
            rtask.execute();

    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            setGlasses();
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (foundFace)
                pb.setVisibility(View.GONE);
            else {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "Face Not Found", Toast.LENGTH_SHORT).show();
                getImage(context);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
    }

    private void onRightSwipe() {
        nextPair();
    }

    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    GlassActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    GlassActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
            }
            return false;
        }


    }

    public void nextPair()
    {
        /*imageView.setImageResource(R.drawable.glassesten);
        imageView.requestLayout();
        Double height = width * .281;
        int temp = height.intValue();
        if ((height - temp) > .5)
            temp = temp + 1;
        imageView.getLayoutParams().height = temp;*/

        imageView.setImageBitmap(MainActivity.frames.get(framecount).getBitmap());
        imageView.requestLayout();
        Double height = width * MainActivity.frames.get(framecount).getRatio();
        int temp = height.intValue();
        if ((height - temp) > .5)
            temp = temp + 1;
        imageView.getLayoutParams().height = temp;
        framecount++;
        framecount = framecount % MainActivity.frames.size();


    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        if(mCurrentPhotoPath == null)
            System.out.println("Photo path is null");
        else
            System.out.println("photo path is NOT null");
        return image;
    }


    public Bitmap readBitmap(Uri selectedImage) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        AssetFileDescriptor fileDescriptor =null;
        try {
            fileDescriptor = this.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
