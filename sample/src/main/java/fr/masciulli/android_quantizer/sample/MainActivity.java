package fr.masciulli.android_quantizer.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import fr.masciulli.android_quantizer.lib.ColorQuantizer;

public class MainActivity extends ActionBarActivity {

    private ImageView mImageView;
    private View mView1;
    private View mView2;
    private View mView3;
    private View mView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;

        mImageView = (ImageView)findViewById(R.id.image);
        mView1 = findViewById(R.id.view1);
        mView2 = findViewById(R.id.view2);
        mView3 = findViewById(R.id.view3);
        mView4 = findViewById(R.id.view4);

        mImageView.setImageDrawable(getResources().getDrawable(R.drawable.sample));

        Bitmap bitmapToQuantize = BitmapFactory.decodeResource(getResources(), R.drawable.sample, options);

        ArrayList<Integer> quantizedColors = new ColorQuantizer().load(bitmapToQuantize).quantize().getQuantizedColors();

        mView1.setBackgroundColor(quantizedColors.get(0));
        mView2.setBackgroundColor(quantizedColors.get(1));
        mView3.setBackgroundColor(quantizedColors.get(2));
        mView4.setBackgroundColor(quantizedColors.get(3));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
