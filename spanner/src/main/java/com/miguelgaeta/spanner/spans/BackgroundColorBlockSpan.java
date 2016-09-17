package com.miguelgaeta.spanner.spans;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.LineBackgroundSpan;

import static com.miguelgaeta.spanner.spans._ColorCompat.getColor;
import static com.miguelgaeta.spanner.spans._ColorCompat.getColorAttrResId;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class BackgroundColorBlockSpan extends CharacterStyle implements LineBackgroundSpan {

    private final int color;
    private final RectF rect = new RectF();

    public BackgroundColorBlockSpan(final Resources.Theme theme, final int colorAttrResId) {
        this(getColorAttrResId(theme, colorAttrResId));
    }

    public BackgroundColorBlockSpan(final Context context, final int colorResId) {
        this(getColor(context, colorResId));
    }

    public BackgroundColorBlockSpan(int color) {
        this.color = color;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        final int paintColor = p.getColor();

        rect.set(left, top, right, bottom);

        p.setColor(color);
        c.drawRect(rect, p);
        p.setColor(paintColor);
    }

    @Override
    public void updateDrawState(TextPaint tp) {

    }
}
