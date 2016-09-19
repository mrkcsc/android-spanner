package com.miguelgaeta.spanner.spans;

import android.text.TextPaint;
import android.view.View;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration", "DefaultFileTemplate"})
public class ClickableSpan extends android.text.style.ClickableSpan {

    private final View.OnClickListener onClickListener;
    private final boolean underline;

    public ClickableSpan(final View.OnClickListener onClickListener, final boolean underline) {
        this.onClickListener = onClickListener;
        this.underline = underline;
    }

    public ClickableSpan(final View.OnClickListener onClickListener) {
        this(onClickListener, true);
    }

    @Override
    public void updateDrawState(final TextPaint drawState) {
        drawState.setUnderlineText(underline);
    }

    @Override
    public void onClick(View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }
}
