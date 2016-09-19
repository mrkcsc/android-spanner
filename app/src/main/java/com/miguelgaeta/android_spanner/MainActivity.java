package com.miguelgaeta.android_spanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import com.miguelgaeta.spanner.Spanner;
import com.miguelgaeta.spanner.SpannerMarkdown;
import com.miguelgaeta.spanner.spans.BackgroundColorBlockSpan;
import com.miguelgaeta.spanner.spans.BackgroundColorSpan;
import com.miguelgaeta.spanner.spans.ForegroundColorSpan;
import com.miguelgaeta.spanner.spans.MonospaceSpan;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_STRING_0 = "*zack@discordapp.com* created *[test](/monitors#841376/edit)*.\n\nThe monitor:  \n\n- is named: test.  \n- triggers `a code block` when *apns.pushes* over is *> 200.0* on average during the *last 5m*.  \n- does ```for(int i=0; i < 100; i++) {\n    log.e(\"good stuff\");\n}``` not notify on missing data.  \n- does not automatically resolve.  \n- includes the message: \"test @slack-devops\"  \n- does not renotify.  \n- notifies recipients when @{1121212} definition _is_ modified.  \n- requires a full window of data - includes triggering tags in notification title.  \n; _end_";
    private static final String TEST_STRING_1 = "Hey this _is_ totally @{1121212} cool *miguel*!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView holder = (TextView) findViewById(R.id.spannable_string_holder);

        /*
        final SpannableString spanner =
            new Spanner(TEST_STRING_0)
                .addMarkdownStrategy()
                .addReplacementStrategy(new Spanner.OnMatchListener() {
                    @Override
                    public Spanner.Replacement call(String match) {
                        return new Spanner.Replacement("Sam",
                            SpanHelpers.createForegroundColorSpan(MainActivity.this, android.R.color.holo_red_dark),
                            SpanHelpers.createBoldItalicSpan());
                    }
                }, "@{", "}")
                .toSpannableString();
                */

        final Spannable spanner = new Spanner(TEST_STRING_0)
            .replace(
                SpannerMarkdown.forCode(new SpannerMarkdown.OnCodeBlockMatchListener() {
                    @Override
                    public Collection<CharacterStyle> getStyles(final boolean block) {
                        final CharacterStyle background;
                        if (block) {
                            background = new BackgroundColorBlockSpan(Color.DKGRAY);
                        } else {
                            background = new BackgroundColorSpan(Color.DKGRAY);
                        }

                        return Arrays.asList(
                            new MonospaceSpan(),
                            new RelativeSizeSpan(0.75f),
                            new ForegroundColorSpan(Color.GRAY), background);
                    }
            }))
            .replace(
                SpannerMarkdown.forUnderline(),
                SpannerMarkdown.forBoldItalic(),
                SpannerMarkdown.forBold(),
                SpannerMarkdown.forItalic(),
                SpannerMarkdown.forStrikeThrough())
            /*
            .replace(new Spanner.MatchStrategy("triggers|requires", new Spanner.OnMatchListener() {
                @Override
                public Spanner.Replacement call(Matcher matcher) {
                    return new Spanner.Replacement("\n" + "hi" + "\n", new BackgroundColorBlockSpan(Color.RED));
                }
            }))
            */
            .toSpannable();

        if (holder != null) {
            holder.setText(spanner);
        }

        final String baby = "asd *** ddf *** sdfdsf \n" +
            "\n" +
            "***ads\n" +
            "\n" +
            "ad***";

        final String start = "***";
        final String end = "***";

        final String regex = "(^|\\s)((" + Pattern.quote(start) + ")([\\s\\S]*?)(" + Pattern.quote(end) + "))($|\\s)";

        /*
        new Spanner.OnMatchListener() {
                    @Override
                    public Spanner.Replacement call(Matcher matcher) {
                        Log.e("Test", matcher.group());

                        return new Spanner.Replacement(matcher.group(), SpanHelpers.createBoldSpan(),
                            new LineBackgroundSpan() {

                            private int mBackgroundColor = Color.RED;
                            private RectF mBgRect = new RectF();


                            @Override
                            public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
                                final int paintColor = p.getColor();
                                // Draw the background
                                Log.e("Test", "T: " + lnum);


                                mBgRect.set(left,
                                    top,
                                    right,
                                    bottom);
                                p.setColor(mBackgroundColor);
                                //c.drawRect(mBgRect, p);
                                c.drawRoundRect(mBgRect, 5, 5, p);

                                p.setColor(paintColor);
                            }
                        });
                    }
                }
         */
    }
}
