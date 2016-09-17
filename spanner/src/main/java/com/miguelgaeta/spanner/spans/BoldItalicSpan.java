package com.miguelgaeta.spanner.spans;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class BoldItalicSpan extends StyleSpan {

    public BoldItalicSpan(int style) {
        super(Typeface.BOLD_ITALIC);
    }
}
