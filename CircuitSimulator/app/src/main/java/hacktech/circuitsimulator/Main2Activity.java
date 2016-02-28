package hacktech.circuitsimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        File imgFile = new  File("/sdcard/Circuit Simulator/cam_image.jpg");

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.image);
            myImage.setImageBitmap(myBitmap);
        }
    }
}
