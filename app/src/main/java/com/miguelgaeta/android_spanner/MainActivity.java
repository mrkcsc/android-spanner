package com.miguelgaeta.android_spanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.widget.TextView;

import com.miguelgaeta.spanner.SpanHelpers;
import com.miguelgaeta.spanner.Spanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView holder = (TextView) findViewById(R.id.spannable_string_holder);

        final SpannableString a =
            new Spanner("Hey this is totally @{1121212} cool *miguel*!")
                .addReplacementStrategy(new Spanner.OnMatchListener() {
                    @Override
                    public Spanner.Replacement call(String match) {
                        return new Spanner.Replacement(match, SpanHelpers.createBoldSpan());
                    }
                }, "*", "*")
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
