package pl.metawired.gallery;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class DetailActivity extends AppCompatActivity {

    public static String KEY = "IMG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupImmersion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loadImage();
    }

    private void setupImmersion() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void loadImage() {
        final String location = (String) getIntent().getExtras().get(KEY);
        final ImageView imageView = (ImageView) findViewById(R.id.fullImage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Glide.with(DetailActivity.this)
                .load(location)
                .override(width, height)
                .crossFade()
                .into(imageView);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT)) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
            View layout = findViewById(R.id.fullImage_layout);
            Snackbar.make(layout, R.string.imession_warning, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
