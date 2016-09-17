package com.miguelgaeta.spanner.spans;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings("DefaultFileTemplate")
class _ColorCompat {

    static int getColor(final Context context, final int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorResId);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(colorResId);
        }
    }

    static int getColorAttrResId(final Resources.Theme theme, final int colorAttrResId) {
        final TypedValue typedValue = new TypedValue();

        theme.resolveAttribute(colorAttrResId, typedValue, true);

        return typedValue.data;
    }
}
