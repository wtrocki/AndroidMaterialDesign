package pl.metawired.gallery;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 */
public class PicturesAsyncTask extends AsyncTask<Void, Void, List<PhotoItem>> {

    private GalleryActivity activity;

    public PicturesAsyncTask(GalleryActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<PhotoItem> doInBackground(Void... params) {
        // FIXME - we should use android database (cursor method here, instead of traversing file elements)
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File path2 = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        List<PhotoItem> result = new ArrayList<>();
        fetchPhotoItems(path, result);
        fetchPhotoItems(path2, result);
        return result;
    }

    private List<PhotoItem> fetchPhotoItems(File path, List<PhotoItem> result) {
        if (path.exists()) {
            Resources resources = activity.getResources();
            // array of valid image file extensions
            String[] imageTypes = resources.getStringArray(R.array.extensions);
            List<FilenameFilter> filters = new ArrayList<>(imageTypes.length + 1);
            for (final String type : imageTypes) {
                filters.add(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith("." + type);
                    }
                });
            }

            Collection<File> allMatchingFiles = listFiles(
                    path, filters, -1);
            for (File f : allMatchingFiles) {
                result.add(new PhotoItem(f.toString()));
            }
        }
        return result;
    }

    public Collection<File> listFiles(File directory,
                                      List<FilenameFilter> filter, int recurse) {
        Vector<File> files = new Vector<>();
        File[] entries = directory.listFiles();

        if (entries != null) {
            for (File entry : entries) {
                for (FilenameFilter filefilter : filter) {
                    if (filefilter.accept(directory, entry.getName())) {
                        files.add(entry);
                    }
                }
                if (!isThumbnailsFolder(entry)) {
                    if ((recurse <= -1) || (recurse > 0 && entry.isDirectory())) {
                        recurse--;
                        files.addAll(listFiles(entry, filter, recurse));
                        recurse++;
                    }
                }
            }
        }
        return files;
    }

    private boolean isThumbnailsFolder(File entry) {
        // FIXME - Ugly hack to skip thumbnails :)
        return entry.getName().contentEquals(".thumbnails");
    }

    @Override
    protected void onPostExecute(List<PhotoItem> images) {
        activity.setImages(images);
    }
}