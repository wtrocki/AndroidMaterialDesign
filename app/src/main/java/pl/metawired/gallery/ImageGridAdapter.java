package pl.metawired.gallery;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ImageViewHolder> {

    private List<PhotoItem> data;
    private GalleryActivity galleryActivity;

    private final static int IMAGE_SIZE = 170;
    private static int imageSizeInPixels;

    static {
        imageSizeInPixels = (int) (IMAGE_SIZE * Resources.getSystem().getDisplayMetrics().density);
    }


    public ImageGridAdapter(GalleryActivity galleryActivity) {

        this.galleryActivity = galleryActivity;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_element, viewGroup, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(v);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder imageViewHolder, int i) {
        if (data != null) {
            final PhotoItem photoItem = data.get(i);
            Glide.with(galleryActivity)
                    .load(photoItem.getFullImageUri())
                    .crossFade()
                    .override(imageSizeInPixels, imageSizeInPixels)
                    .into(imageViewHolder.photo);
            imageViewHolder.photo.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(galleryActivity, DetailActivity.class);
                    intent.putExtra(DetailActivity.KEY, photoItem.getFullImageUri());
                    startWithAnimation(intent, imageViewHolder);
                }
            });
        }
    }

    private void startWithAnimation(Intent intent, ImageViewHolder imageViewHolder) {
        if (Build.VERSION.SDK_INT >= 21) {
            imageViewHolder.photo.setTransitionName("photoTransition");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    galleryActivity, imageViewHolder.photo, "photoTransition");
            galleryActivity.startActivity(intent, options.toBundle());
        } else {
            galleryActivity.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    public void setData(List<PhotoItem> data) {
        this.data = data;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView photo;

        ImageViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.imageCardView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
        }
    }
}