package com.miguelgaeta.spanner.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class ImageSpan extends android.text.style.ImageSpan {

    public ImageSpan(final Context context, final Bitmap bitmap, final int width, final int height, final boolean drawableDensityIndependent) {
        this(getDrawable(context, bitmap, width, height, drawableDensityIndependent));
    }

    public ImageSpan(final Context context, final Bitmap bitmap, final int width, final int height) {
        this(context, bitmap, width, height, true);
    }

    public ImageSpan(final Context context, final Bitmap bitmap) {
        super(context, bitmap);
    }

    public ImageSpan(final Context context, final Bitmap bitmap, final int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
    }

    public ImageSpan(final Drawable bitmap) {
        super(bitmap);
    }

    public ImageSpan(final Drawable d, final int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public ImageSpan(final Drawable drawable, final String source) {
        super(drawable, source);
    }

    public ImageSpan(final Drawable drawable, final String source, final int verticalAlignment) {
        super(drawable, source, verticalAlignment);
    }

    public ImageSpan(final Context context, final Uri uri) {
        super(context, uri);
    }

    public ImageSpan(final Context context, final Uri uri, final int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public ImageSpan(final Context context, final int resourceId) {
        super(context, resourceId);
    }

    public ImageSpan(final Context context, final int resourceId, final int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    private static Drawable getDrawable(final Context context, final Bitmap bitmap, final int width, final int height, final boolean drawableDensityIndependent) {
        final Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        if (drawableDensityIndependent) {
            drawable.setBounds(0, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics()));
        } else {
            drawable.setBounds(0, 0, width, height);
        }

        return drawable;
    }
}
