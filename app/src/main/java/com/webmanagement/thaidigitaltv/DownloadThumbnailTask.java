package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 20/10/2557.
 */

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.ImageView;


/**
 *
 * @brief AsyncTask to down load thumbnail of content
 *
 */
public class DownloadThumbnailTask extends AsyncTask<Void, Void, Bitmap>
{

    final static String TAG = "DownloadThumbnailTask";

    // working url queue.
    static HashSet<Uri> mWorkingUri = new HashSet<Uri>();
    private static int mThumbWidth = 0;
    private static int mThumbHeight = 0;

    // maintain only one android http client.
    static AndroidHttpClient mHttpClient = AndroidHttpClient.newInstance("AllShare Sample 1/1");

    private Uri mUrl = null;
    private WeakReference<ImageView> mIconViewReference = null;

    public DownloadThumbnailTask(ImageView imageView, Uri uri)
    {

        if (imageView != null)
        {
            if (mThumbWidth == 0 || mThumbHeight == 0)
            {
                mThumbWidth = (int) imageView.getContext().getResources()
                        .getDimension(R.dimen.thumbnail_width);
                mThumbHeight = (int) imageView.getContext().getResources()
                        .getDimension(R.dimen.thumbnail_height);
            }
        }
        if (uri != null && imageView != null)
        {
            mIconViewReference = new WeakReference<ImageView>(imageView);
            mUrl = uri;
            if (mWorkingUri.contains(mUrl) == true)
            {
                // no need to download image.
                mUrl = null;
            }
            else
            {
                mWorkingUri.add(mUrl);
            }
        }
    }

    public Uri getUri()
    {

        return mUrl;
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(Void... params)
    {

        if (mUrl == null)
            return null;

        Bitmap bitmap;
        bitmap = BitmapCache.getBitmapCache().get(mUrl.toString());

        if (bitmap != null)
            return bitmap; // lucky! already cached icon.

        try
        {
            HttpGet getRequest = new HttpGet(mUrl.toString());
            HttpResponse response = mHttpClient.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK)
            {
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                InputStream inputStream = null;
                try
                {
                    inputStream = entity.getContent();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null)
                    {
                        if (bitmap.getWidth() > mThumbWidth || bitmap.getHeight() > mThumbHeight)
                        {
                            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, mThumbWidth, mThumbHeight, true);
                            bitmap.recycle();
                            bitmap = bitmap2;
                        }
                    }

                    BitmapCache.getBitmapCache().put(mUrl.toString(), bitmap);
                    return bitmap;
                }
                catch (OutOfMemoryError err)
                {
                    BitmapCache.getBitmapCache().clear();
                    err.printStackTrace();
                }
                finally
                {
                    if (inputStream != null)
                    {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        }
        catch (IOException e)
        {
            return null;
        }

        mWorkingUri.remove(mUrl);

        return null;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap)
    {

        try
        {

            if (isCancelled() || mUrl == null)
            {
                return;
            }
            else
            {
                if (mIconViewReference != null)
                {
                    bitmap = BitmapCache.getBitmapCache().get(mUrl.toString());
                    ImageView imageView = mIconViewReference.get();
                    if (imageView != null )
                    {
                        imageView.setImageBitmap(bitmap);
                        imageView.invalidate();
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}// end of inner class IconDownloaderTask
