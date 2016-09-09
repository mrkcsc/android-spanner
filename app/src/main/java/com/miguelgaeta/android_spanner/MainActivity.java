package com.miguelgaeta.android_spanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.widget.TextView;

import com.miguelgaeta.spanner.SpanHelpers;
import com.miguelgaeta.spanner.Spanner;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_STRING_0 = "*zack@discordapp.com* created *[test](/monitors#841376/edit)*.\n\nThe monitor:  \n\n- is named: test.  \n- triggers when *apns.pushes* over is *> 200.0* on average during the *last 5m*.  \n- does not notify on missing data.  \n- does not automatically resolve.  \n- includes the message: \"test @slack-devops\"  \n- does not renotify.  \n- notifies recipients when @{1121212} definition is modified.  \n- requires a full window of data - includes triggering tags in notification title.  \n;";
    private static final String TEST_STRING_1 = "Hey this is totally @{1121212} cool *miguel*!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView holder = (TextView) findViewById(R.id.spannable_string_holder);

        final SpannableString a =
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

        if (holder != null) {
            holder.setText(a);
        }
    }
}
