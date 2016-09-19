package com.miguelgaeta.android_spanner;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.miguelgaeta.spanner.Spanner;
import com.miguelgaeta.spanner.SpannerMarkdown;
import com.miguelgaeta.spanner.spans.BackgroundColorBlockSpan;
import com.miguelgaeta.spanner.spans.BackgroundColorSpan;
import com.miguelgaeta.spanner.spans.BoldSpan;
import com.miguelgaeta.spanner.spans.ClickableSpan;
import com.miguelgaeta.spanner.spans.ForegroundColorSpan;
import com.miguelgaeta.spanner.spans.MonospaceSpan;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_STRING_0 = "~~*zack@discordapp.com*~~ created *[test](/monitors#841376/edit)*.\n\nThe monitor:  \n\n- is named: test.  \n- triggers `a code block` when *apns.pushes* over is *> 200.0* ***on average*** during the *last 5m*.  \n- does ```for(int i=0; i < 100; i++) {\n    log.e(\"good stuff\");\n**test**\n}``` not notify on missing data.  \n- does not automatically resolve.  \n- includes the message: \"test @slack-devops\"  \n- does not renotify.  \n- [notifies recipients](http://www.google.com) when <@1121212> definition _is_ modified.  \n- requires a full window of data - includes triggering tags in notification title.  \n; _end_";
    private static final String TEST_STRING_1 = "Hey this _is_ totally @{1121212} cool *miguel*!";
    private static final String TEST_STRING_2 = "**one****two**";
    private static final String TEST_STRING_3 = "**one** ***two***";
    private static final String TEST_STRING_4 = "~~de~~ dasas```d```asdh this is \\~~hi friend\\~~ for (int i = 0; i < 10; i++) { }``` ~~hi friend~~";
    private static final String TEST_STRING_5 = "~~de~~ dasasasdh this is \\~~hi friend\\~~ for (int i = 0; i < 10; i++) { } ~~hi friend~~";


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
                .toSpannableString();3
                */

        Log.e("Test", "Start");

        final Spannable spanner = new Spanner(TEST_STRING_4)
            .replace(
                SpannerMarkdown.forCode(new SpannerMarkdown.OnCodeBlockMatchListener() {
                    @Override
                    public Collection<CharacterStyle> getSpans(final boolean block) {
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
            .replace(new Spanner.MatchStrategy(SpannerMarkdown.PATTERN_LINK, new Spanner.OnMatchListener() {
                @Override
                public Spanner.Replacement call(final Matcher matcher) {
                    final String linkTitle = matcher.group(SpannerMarkdown.LINK_GROUP_TITLE);
                    final String linkUrl = matcher.group(SpannerMarkdown.LINK_GROUP_URL);

                    return new Spanner.Replacement(linkTitle,
                        new ForegroundColorSpan(Color.BLUE),
                        new ClickableSpan(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl)));
                            }
                        }, true));
                }
            }))
            .replace(
                SpannerMarkdown.forUnderline(),
                SpannerMarkdown.forBoldItalic(),
                SpannerMarkdown.forBold(),
                SpannerMarkdown.forItalic(),
                SpannerMarkdown.forStrikeThrough())
            .replace(new Spanner.MatchStrategy("<@([0-9]+?)>", new Spanner.OnMatchListener() {
                @Override
                public Spanner.Replacement call(Matcher matcher) {
                    return new Spanner.Replacement("mrkcsc", new BoldSpan(), new ForegroundColorSpan(Color.RED));
                }
            }))
            .toSpannable();

        Log.e("Test", "End");

        if (holder != null) {
            holder.setMovementMethod(LinkMovementMethod.getInstance());
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
    }
}
