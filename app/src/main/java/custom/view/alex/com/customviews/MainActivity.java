package custom.view.alex.com.customviews;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;

import java.util.ArrayList;
import java.util.List;

import custom.view.alex.com.customviews.ui.Rotatable;
import custom.view.alex.com.customviews.ui.RotatableRelativeLayout;


public class MainActivity extends ActionBarActivity {

    /* The views which implements the interface of Rotatable and want to listener the device's orientation */
    private List<Rotatable> mOrientationViews = new ArrayList<>();

    private RotatableRelativeLayout layout = null;
    private MyOrientationEventListener listener = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RotatableRelativeLayout) findViewById(R.id.layout);
        if (!mOrientationViews.contains(layout)) {
            mOrientationViews.add(layout);
        }
        listener = new MyOrientationEventListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        listener.disable();
    }

    /**
     * This class can listener the device's orientation. and then call the {@link #notifyOrientationChange(int)} to notify
     * the client. Just for test.
     */
    private class MyOrientationEventListener extends OrientationEventListener {

        private int mLastOrientation = -1;
        private static final int ORIENTATION_HYSTERESIS = 5;

        private MyOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == ORIENTATION_UNKNOWN) {
                return;
            }
            int newOrientation = roundOrientation(orientation, 0);
            if (mLastOrientation != newOrientation) {
                mLastOrientation = newOrientation;
                notifyOrientationChange(mLastOrientation);
            }
        }

        private int roundOrientation(int orientation, int orientationHistory) {
            boolean changeOrientation;
            if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
                changeOrientation = true;
            } else {
                int dist = Math.abs(orientation - orientationHistory);
                dist = Math.min(dist, 360 - dist);
                changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
            }
            if (changeOrientation) {
                return ((orientation + 45) / 90 * 90) % 360;
            }
            return orientationHistory;
        }
    };

    private void notifyOrientationChange(int orientation) {
        for (Rotatable rotate: mOrientationViews) {
            rotate.setOrientation(orientation, true);
        }
    }
}
