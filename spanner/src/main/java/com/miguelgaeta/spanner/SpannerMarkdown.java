package com.miguelgaeta.spanner;

import android.graphics.Typeface;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import java.util.regex.Matcher;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SpannerMarkdown {

    private static final String REGEX_UNDERLINE = "(__)([\\s\\S]*?)(__)";
    private static final String REGEX_BOLD_ITALIC = "(\\*\\*\\*)([\\s\\S]*?)(\\*\\*\\*)";
    private static final String REGEX_BOLD = "(\\*\\*)([\\s\\S]*?)(\\*\\*)";
    private static final String REGEX_ITALIC = "((\\*)([\\s\\S]*?)(\\*))|((_)([\\s\\S]*?)(_)($|\\s))";
    private static final String REGEX_STRIKE_THROUGH = "(~~)([\\s\\S]*?)(~~)";

    public static final Spanner.MatchStrategy UNDERLINE = new Spanner.MatchStrategy(REGEX_UNDERLINE, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(), new UnderlineSpan());
        }
    });

    public static final Spanner.MatchStrategy BOLD_ITALIC = new Spanner.MatchStrategy(REGEX_BOLD_ITALIC, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(), new StyleSpan(Typeface.ITALIC));
        }
    });

    public static final Spanner.MatchStrategy BOLD = new Spanner.MatchStrategy(REGEX_BOLD, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(), new StyleSpan(Typeface.BOLD));
        }
    });

    public static final Spanner.MatchStrategy ITALIC = new Spanner.MatchStrategy(REGEX_ITALIC, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(), new StyleSpan(Typeface.ITALIC));
        }
    });

    public static final Spanner.MatchStrategy STRIKE_THROUGH = new Spanner.MatchStrategy(REGEX_STRIKE_THROUGH, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(), new StrikethroughSpan());
        }
    });

    // TODO: Code blocks.
    // TODO: Links
}
