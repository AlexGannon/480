package android.com.freezeframe;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class CustomView extends View {

    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;
    Context context;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * Sets the bitmap background and the associated face detections.
     */
    void setContent(Bitmap bitmap, SparseArray<Face> faces) {
        mBitmap = bitmap;
        mFaces = faces;
        this.postInvalidate();
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceAnnotations(canvas, scale);
        }
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();

        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);





        System.out.println("The scale is: " + scale);
        if(imageWidth > viewWidth)
            scale = viewWidth/imageWidth;
        else
            scale = 1;

         double tempWidth = imageWidth * scale;
        double tempHeight = imageHeight * scale;

        int scalledWidth;
        int scalledHeight;

        //Round up the width
        if(tempWidth - ((int)tempWidth) >= .500)
            scalledWidth = ((int)tempWidth) + 1;
        else
        scalledWidth = (int) tempWidth;

        //Round up the height
        if(tempHeight - ((int) tempHeight) >= .500)
            scalledHeight = ((int) tempHeight) + 1;
        else
        scalledHeight = (int) tempHeight;


        if(scalledWidth < viewWidth)
        {
            scale = viewWidth / scalledWidth;

            tempWidth = imageWidth * scale;
            tempHeight = imageHeight * scale;

            //Round up the width
            if(tempWidth - ((int)tempWidth) >= .500)
                scalledWidth = ((int)tempWidth) + 1;
            else
                scalledWidth = (int) tempWidth;

            //Round up the height
            if(tempHeight - ((int) tempHeight) >= .500)
                scalledHeight = ((int) tempHeight) + 1;
            else
                scalledHeight = (int) tempHeight;
        }

        //Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        Rect destBounds = new Rect(0, 0, scalledWidth, scalledHeight);

        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a rectangle around each detected face
     */
    private void drawFaceRectangle(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (int i = 0; i < mFaces.size(); ++i) {
            Face face = mFaces.valueAt(i);
            canvas.drawRect((float)(face.getPosition().x * scale),
                    (float)(face.getPosition().y * scale),
                    (float)((face.getPosition().x + face.getWidth()) * scale),
                    (float)((face.getPosition().y + face.getHeight()) * scale),
                    paint);
        }
    }

    private void drawFaceAnnotations(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (int i = 0; i < mFaces.size(); ++i) {
            Face face = mFaces.valueAt(i);
            for (Landmark landmark : face.getLandmarks()) {
                int cx = (int) (landmark.getPosition().x * scale);
                int cy = (int) (landmark.getPosition().y * scale);

                if(landmark.getType() == Landmark.LEFT_EYE) {
                    GlassActivity.eyeLevelRX = cx;
                    GlassActivity.eyeLevelRY = cy;
                    System.out.println("The eye level is: " + cy + " The x is: " + cx);
                    //canvas.drawCircle(cx, cy, 2, paint);
                }
                else if(landmark.getType() == Landmark.RIGHT_EYE)
                {
                    GlassActivity.eyeLevelLX = cx;
                    GlassActivity.eyeLevelLY = cy;
                    //canvas.drawCircle(cx, cy, 2, paint);
                }
                else if(landmark.getType() == Landmark.RIGHT_CHEEK)
                {
                    GlassActivity.cheekL = cy;
                    //canvas.drawCircle(cx, cy, 6, paint);
                }
                else if(landmark.getType() == Landmark.LEFT_CHEEK)
                {
                    GlassActivity.cheekR = cy;
                    //canvas.drawCircle(cx, cy, 6, paint);
                }




            }
        }
        if(GlassActivity.cheekR - GlassActivity.cheekL > 15)
        {
            //canvas.drawCircle(GlassActivity.eyeLevelLX + ((GlassActivity.eyeLevelRX-GlassActivity.eyeLevelLX)/2), (GlassActivity.eyeLevelRY + GlassActivity.eyeLevelLY)/2, 6, paint);
            System.out.println("Right higher");
        }
        else if(GlassActivity.cheekL - GlassActivity.cheekR  > 15)
        {
            //canvas.drawCircle(GlassActivity.eyeLevelLX + ((GlassActivity.eyeLevelRX-GlassActivity.eyeLevelLX)/2), GlassActivity.eyeLevelLY, 6, paint);
            System.out.println("Left higher");
        }


        System.out.println("Calling draw frames");
        GlassActivity.drawFrames();

    }

}