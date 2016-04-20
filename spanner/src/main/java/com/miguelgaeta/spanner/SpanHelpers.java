package com.miguelgaeta.spanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Miguel Gaeta on 4/20/16.
 *
 * Collection of convenience span initializer.
 */
@SuppressWarnings("unused")
public class SpanHelpers {

    public static CharacterStyle createMonospaceSpan() {
        return new TypefaceSpan("monospace");
    }

    public static CharacterStyle createRelativeSizeSpan(float proportion) {
        return new RelativeSizeSpan(proportion);
    }

    public static CharacterStyle createClickableSpan(final View.OnClickListener onClick, final Context context, final int colorResId, final boolean underline) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return createClickableSpan(onClick, context.getColor(colorResId), underline);
        } else {
            //noinspection deprecation
            return createClickableSpan(onClick, context.getResources().getColor(colorResId), underline);
        }
    }

    public static CharacterStyle createClickableSpan(final View.OnClickListener onClick, final int color, final boolean underline) {
        return new ClickableSpan() {

            @Override
            public void onClick(View view) {

                if (onClick != null) {
                    onClick.onClick(view);
                }
            }

            @Override
            public void updateDrawState(final TextPaint drawState) {
                drawState.setUnderlineText(underline);
                drawState.setColor(color);
            }
        };
    }

    public static CharacterStyle createForegroundColorSpan(final Context context, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return createForegroundColorSpan(context.getColor(colorResId));
        } else {
            //noinspection deprecation
            return createForegroundColorSpan(context.getResources().getColor(colorResId));
        }
    }

    public static CharacterStyle createForegroundColorSpan(int color) {
        return new ForegroundColorSpan(color);
    }

    public static CharacterStyle createBoldSpan() {
        return new StyleSpan(Typeface.BOLD);
    }

    public static CharacterStyle createBoldItalicSpan() {
        return new StyleSpan(Typeface.BOLD_ITALIC);
    }

    public static CharacterStyle createItalicSpan() {
        return new StyleSpan(Typeface.ITALIC);
    }

    public static CharacterStyle createStrikethroughSpan() {
        return new StrikethroughSpan();
    }

    public static CharacterStyle createUnderlineSpan() {
        return new UnderlineSpan();
    }

    public static CharacterStyle createBackgroundColorSpan(final Context context, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return createBackgroundColorSpan(context.getColor(colorResId));
        } else {
            //noinspection deprecation
            return createBackgroundColorSpan(context.getResources().getColor(colorResId));
        }
    }

    public static CharacterStyle createBackgroundColorSpan(int color) {
        return new BackgroundColorSpan(color);
    }

    public static CharacterStyle createImageSpan(final Context context, final Bitmap bitmap) {
        return new ImageSpan(context, bitmap);
    }

    public static CharacterStyle createImageSpan(final Context context, final Bitmap bitmap, final int width, final int height) {
        return createImageSpan(context, bitmap, width, height, true);
    }

    public static CharacterStyle createImageSpan(final Context context, final Bitmap bitmap, final int width, final int height, final boolean drawableDensityIndependent) {
        final BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        if (drawableDensityIndependent) {
            drawable.setBounds(0, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics()));
        } else {
            drawable.setBounds(0, 0, width, height);
        }

        return createImageSpan(drawable);
    }

    public static CharacterStyle createImageSpan(final Drawable drawable) {
        return new ImageSpan(drawable);
    }
}
