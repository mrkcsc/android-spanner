package com.miguelgaeta.spanner.spans;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.view.View;

import static com.miguelgaeta.spanner.spans._ColorCompat.getColor;
import static com.miguelgaeta.spanner.spans._ColorCompat.getColorAttrResId;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class ClickableSpan extends android.text.style.ClickableSpan {

    private final View.OnClickListener onClickListener;
    private final boolean underline;
    private final int color;

    public ClickableSpan(final View.OnClickListener onClickListener, final int color, final boolean underline) {
        this.onClickListener = onClickListener;
        this.underline = underline;
        this.color = color;
    }

    public ClickableSpan(final View.OnClickListener onClickListener, final int color) {
        this(onClickListener, color, false);
    }

    public ClickableSpan(final View.OnClickListener onClickListener, final Context context, final int colorResId, final boolean underline) {
        this(onClickListener, getColor(context, colorResId), underline);
    }

    public ClickableSpan(final View.OnClickListener onClickListener, final Context context, final int colorResId) {
        this(onClickListener, getColor(context, colorResId));
    }

    public ClickableSpan(final View.OnClickListener onClickListener, final Resources.Theme theme, final int colorAttrResId, final boolean underline) {
        this(onClickListener, getColorAttrResId(theme, colorAttrResId), underline);
    }

    public ClickableSpan(final View.OnClickListener onClickListener, final Resources.Theme theme, final int colorAttrResId) {
        this(onClickListener, getColorAttrResId(theme, colorAttrResId), false);
    }

    @Override
    public void updateDrawState(final TextPaint drawState) {
        drawState.setUnderlineText(underline);
        drawState.setColor(color);
    }

    @Override
    public void onClick(View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }
}
