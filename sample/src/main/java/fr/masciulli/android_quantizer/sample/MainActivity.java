package fr.masciulli.android_quantizer.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import fr.masciulli.android_quantizer.lib.ColorQuantizer;

public class MainActivity extends ActionBarActivity {

    private ImageView mImageView;
    private View mView1;
    private View mView2;
    private View mView3;
    private View mView4;
    private SpinnerAdapter mSpinnerAdapter;
    private int[] mImageResources = new int[]{
            R.drawable.cupcake,
            R.drawable.strawberry,
            R.drawable.parrot,
            R.drawable.oranges
    };

    private ActionBar.OnNavigationListener mNavigationCallback = new ActionBar.OnNavigationListener() {
        @Override
        public boolean onNavigationItemSelected(int index, long itemId) {
            load(mImageResources[index]);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
                R.layout.simple_spinner_dropdown_item);

        getSupportActionBar().setListNavigationCallbacks(mSpinnerAdapter, mNavigationCallback);

        mImageView = (ImageView)findViewById(R.id.image);
        mView1 = findViewById(R.id.view1);
        mView2 = findViewById(R.id.view2);
        mView3 = findViewById(R.id.view3);
        mView4 = findViewById(R.id.view4);
    }

    private void load(int resource) {

        mImageView.setImageDrawable(getResources().getDrawable(resource));

        // Step 1 : create a Bitmap from a resource or anything else as usual
        // You can downsample it using BitmapFactory.Options it so that the quantization process is quicker

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;

        Bitmap bitmapToQuantize = BitmapFactory.decodeResource(getResources(), resource, options);

        // Step 2 : create a ColorQuantizer. Give it your bitmap using the load() method, and launch quantization using quantize().
        // Finally, retrieve quantize colors.

        ArrayList<Integer> quantizedColors = new ColorQuantizer().load(bitmapToQuantize).quantize().getQuantizedColors();

        mView1.setBackgroundColor(quantizedColors.get(0));
        mView2.setBackgroundColor(quantizedColors.get(1));
        mView3.setBackgroundColor(quantizedColors.get(2));
        mView4.setBackgroundColor(quantizedColors.get(3));
    }

}
