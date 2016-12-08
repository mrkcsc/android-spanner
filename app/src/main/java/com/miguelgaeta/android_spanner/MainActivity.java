package com.miguelgaeta.android_spanner;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
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
import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMarkdown;
import com.yydcdut.rxmarkdown.callback.OnLinkClickCallback;
import com.yydcdut.rxmarkdown.factory.TextFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_STRING_0 = "~~*zack@discordapp.com*~~ created *[test](/monitors#841376/edit)*.\n\nThe monitor:  \n\n- is named: test.  \n- triggers `a code block` when *apns.pushes* over is *> 200.0* ***on average*** during the *last 5m*.  \n- does ```for(int i=0; i < 100; i++) {\n    log.e(\"good stuff\");\n**test**\n}``` not notify on missing data.  \n- does not automatically resolve.  \n- includes the message: \"test @slack-devops\"  \n- does not renotify.  \n- [notifies recipients](http://www.google.com) when <@1121212> definition _is_ modified.  \n- requires a full window of data - includes triggering tags in notification title.  \n; _end_";
    private static final String TEST_STRING_1 = "Hey this _is_ totally @{1121212} cool *miguel*!";
    private static final String TEST_STRING_2 = "**one****two**";
    private static final String TEST_STRING_3 = "**one** ***two***";
    private static final String TEST_STRING_4 = "~~de~~ dasas```d```asdh this is \\~~hi friend\\~~ for (int i = 0; i < 10; i++) { }``` ~~hi friend~~";
    private static final String TEST_STRING_5 = "~~de~~ dasasasdh this is \\~~hi friend\\~~ for (int i = 0; i < 10; i++) { } ~~hi friend~~";
    private static final String TEST_STRING_6 = "this is a code block\n```\nfor(int i=0; i < 100; i++) {\n    log.e(\"good stuff\");\n    \\\\**test**\n}\n```\n isnt it ***great***!";
    private static final String TEST_STRING_7 = "this is a wumpus ![test](https://cdn.discordapp.com/emojis/233404695966777354.png) so **cool**!";

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

        RxMDConfiguration rxMDConfiguration = new RxMDConfiguration.Builder(this)
            .setDefaultImageSize(100, 100)//default image width & height
            .setBlockQuotesColor(Color.LTGRAY)//default color of block quotes
            .setHeader1RelativeSize(1.6f)//default relative size of header1
            .setHeader2RelativeSize(1.5f)//default relative size of header2
            .setHeader3RelativeSize(1.4f)//default relative size of header3
            .setHeader4RelativeSize(1.3f)//default relative size of header4
            .setHeader5RelativeSize(1.2f)//default relative size of header5
            .setHeader6RelativeSize(1.1f)//default relative size of header6
            .setHorizontalRulesColor(Color.LTGRAY)//default color of horizontal rules's background
            .setInlineCodeBgColor(Color.LTGRAY)//default color of inline code's background
            .setCodeBgColor(Color.LTGRAY)//default color of code's background
            .setTodoColor(Color.DKGRAY)//default color of todo
            .setTodoDoneColor(Color.DKGRAY)//default color of done
            .setUnOrderListColor(Color.BLACK)//default color of unorder list
            .setLinkColor(Color.RED)//default color of link text
            .setLinkUnderline(true)//default value of whether displays link underline
            .setRxMDImageLoader(new OKLoader(this))//default image loader
            .setDebug(true)//default value of debug
            .setOnLinkClickCallback(new OnLinkClickCallback() {//link click callback
                @Override
                public void onLinkClicked(View view, String link) {
                    Log.e("Test", link);
                }
            })
            .build();

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



        /*
        if (holder != null) {
            holder.setMovementMethod(LinkMovementMethod.getInstance());
            holder.setText(spanner);
        }
        */

        Observable.just(null).map(new Func1<Object, Object>() {
            @Override
            public Object call(Object o) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
        })
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });

        RxMarkdown
            .with(TEST_STRING_7, this)
            .factory(TextFactory.create())
            .config(rxMDConfiguration)
            .intoObservable()
            .map(new Func1<CharSequence, CharSequence>() {
                @Override
                public CharSequence call(CharSequence charSequence) {

                    return charSequence;
                }
            })
            //.subscribeOn(Schedulers.computation())
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<CharSequence>() {
                @Override
                public void call(CharSequence charSequence) {
                    if (holder != null) {
                        holder.setText(charSequence);
                    }

                    Log.e("Test", "Finish processing.");
                }
            });

        Log.e("Test", "End");

        if (holder != null) {
            //holder.setText(TextFactory.create().parse(TEST_STRING_7, rxMDConfiguration));
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
