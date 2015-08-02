package pl.metawired.gallery;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private static final int PICTURE_REQUEST_CODE = 101;

    private ImageGridAdapter adapter;
    private DrawerLayout drawerParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setupList();
        setupButton();
    }

    private void setupList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.image_recycler_view);
        int gridSize = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridSize = 4;
        }
        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(gridSize, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new ImageGridAdapter(this);
        new PicturesAsyncTask(this).execute();
        recyclerView.setAdapter(adapter);
    }

    private void setupButton() {
        View cameraButton = findViewById(R.id.photo_fab);
        ViewCompat.setElevation(cameraButton, 10);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICTURE_REQUEST_CODE);
            }
        });
    }

    private void setupActionBar() {
        setContentView(R.layout.activity_gallery);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        drawerParent = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupRoundedImage();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            setupNavigation(menuItem);
                            return true;
                        }
                    });
        }
    }

    private void setupNavigation(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_home_id) {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
            if (fragmentByTag != null) {
                getSupportFragmentManager().beginTransaction().remove(fragmentByTag).commit();
            }
        } else if (menuItem.getItemId() == R.id.menu_settings_id) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content,
                    new SettingsFragment(), SettingsFragment.TAG).commit();
        }
        menuItem.setChecked(true);
        drawerParent.closeDrawers();
    }

    private void setupRoundedImage() {
        final Resources resources = getResources();
        final ImageView image = (ImageView) findViewById(R.id.head_image);
        (new AsyncTask<Void, Void, RoundedBitmapDrawable>() {
            @Override
            protected RoundedBitmapDrawable doInBackground(Void... params) {
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.head);
                RoundedBitmapDrawable roundedBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(resources, bitmap);
                roundedBitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
                return roundedBitmapDrawable;
            }

            @Override
            protected void onPostExecute(RoundedBitmapDrawable roundedBitmapDrawable) {
                image.setImageDrawable(roundedBitmapDrawable);
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_REQUEST_CODE) {
            new PicturesAsyncTask(this).execute();
        }
    }

    public void setImages(List<PhotoItem> images) {
        adapter.setData(images);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerParent.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
