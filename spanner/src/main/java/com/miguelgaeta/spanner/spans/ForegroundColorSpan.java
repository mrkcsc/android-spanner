package com.miguelgaeta.spanner.spans;

import android.content.Context;
import android.content.res.Resources;

import static com.miguelgaeta.spanner.spans._ColorCompat.getColor;
import static com.miguelgaeta.spanner.spans._ColorCompat.getColorAttrResId;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class ForegroundColorSpan extends android.text.style.ForegroundColorSpan {

    public ForegroundColorSpan(final Resources.Theme theme, final int colorAttrResId) {
        this(getColorAttrResId(theme, colorAttrResId));
    }

    public ForegroundColorSpan(final Context context, final int colorResId) {
        this(getColor(context, colorResId));
    }

    public ForegroundColorSpan(int color) {
        super(color);
    }
}
