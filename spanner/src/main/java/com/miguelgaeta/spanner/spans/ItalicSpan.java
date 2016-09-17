package com.miguelgaeta.spanner.spans;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class ItalicSpan extends StyleSpan {

    public ItalicSpan(int style) {
        super(Typeface.ITALIC);
    }
}
