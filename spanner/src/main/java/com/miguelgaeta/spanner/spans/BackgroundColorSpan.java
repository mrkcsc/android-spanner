package com.miguelgaeta.spanner.spans;

import android.content.Context;
import android.content.res.Resources;

import static com.miguelgaeta.spanner.spans._ColorCompat.getColor;
import static com.miguelgaeta.spanner.spans._ColorCompat.getColorAttrResId;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class BackgroundColorSpan extends android.text.style.BackgroundColorSpan {

    public BackgroundColorSpan(final Resources.Theme theme, final int colorAttrResId) {
        this(getColorAttrResId(theme, colorAttrResId));
    }

    public BackgroundColorSpan(final Context context, final int colorResId) {
        this(getColor(context, colorResId));
    }

    public BackgroundColorSpan(int color) {
        super(color);
    }
}
