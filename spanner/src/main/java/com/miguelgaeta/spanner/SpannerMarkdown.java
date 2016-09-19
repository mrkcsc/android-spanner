package com.miguelgaeta.spanner;

import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;

import com.miguelgaeta.spanner.spans.BoldItalicSpan;
import com.miguelgaeta.spanner.spans.BoldSpan;
import com.miguelgaeta.spanner.spans.ItalicSpan;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Miguel Gaeta on 9/17/16.
 */
@SuppressWarnings({"DefaultFileTemplate", "WeakerAccess"})
public class SpannerMarkdown {

    private static final String REGEX_CODE_BLOCK = "((```)([\\s\\S]*?)(```))";
    private static final String REGEX_CODE_INLINE = "((`)([\\s\\S]*?)(`))";
    private static final String REGEX_CODE = REGEX_CODE_BLOCK + "|" + REGEX_CODE_INLINE;
    private static final String REGEX_UNDERLINE = "(__)([\\s\\S]*?)(__)";
    private static final String REGEX_BOLD_ITALIC = "(\\*\\*\\*)([\\s\\S]*?)(\\*\\*\\*)";
    private static final String REGEX_BOLD = "(\\*\\*)([\\s\\S]*?)(\\*\\*)";
    private static final String REGEX_ITALIC = "((\\*)([\\s\\S]*?)(\\*))|((_)([\\s\\S]*?)(_)($|\\s))";
    private static final String REGEX_STRIKE_THROUGH = "(~~)([\\s\\S]*?)(~~)";

    // TODO: At least one middle char.

    private static final Pattern PATTERN_CODE_BLOCK = Pattern.compile(REGEX_CODE_BLOCK);
    private static final Pattern PATTERN_UNDERLINE = Pattern.compile(REGEX_UNDERLINE);
    private static final Pattern PATTERN_BOLD_ITALIC = Pattern.compile(REGEX_BOLD_ITALIC);
    private static final Pattern PATTERN_BOLD = Pattern.compile(REGEX_BOLD);
    private static final Pattern PATTERN_ITALIC = Pattern.compile(REGEX_ITALIC);
    private static final Pattern PATTERN_STRIKE_THROUGH = Pattern.compile(REGEX_STRIKE_THROUGH);

    private static final Spanner.MatchStrategy STRATEGY_UNDERLINE = new Spanner.MatchStrategy(PATTERN_UNDERLINE, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(2), new UnderlineSpan());
        }
    });

    private static final Spanner.MatchStrategy STRATEGY_BOLD_ITALIC = new Spanner.MatchStrategy(PATTERN_BOLD_ITALIC, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(2), new BoldItalicSpan());
        }
    });

    private static final Spanner.MatchStrategy STRATEGY_BOLD = new Spanner.MatchStrategy(PATTERN_BOLD, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(2), new BoldSpan());
        }
    });

    private static final Spanner.MatchStrategy STRATEGY_ITALIC = new Spanner.MatchStrategy(PATTERN_ITALIC, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            final String replacement;
            if (matcher.group(1) != null) {
                replacement = matcher.group(3);
            } else {
                replacement = matcher.group(7) + matcher.group(9);
            }

            return new Spanner.Replacement(replacement, new ItalicSpan());
        }
    });

    private static final Spanner.MatchStrategy STRATEGY_STRIKE_THROUGH = new Spanner.MatchStrategy(PATTERN_STRIKE_THROUGH, new Spanner.OnMatchListener() {
        @Override
        public Spanner.Replacement call(Matcher matcher) {
            return new Spanner.Replacement(matcher.group(2), new StrikethroughSpan());
        }
    });

    public static Spanner.MatchStrategy forCodeInline() {

        return null;
    }

    public static Spanner.MatchStrategy forCode(final OnCodeBlockMatchListener onMatchListener) {
        return new Spanner.MatchStrategy(REGEX_CODE, new Spanner.OnMatchListener() {
            @Override
            public Spanner.Replacement call(Matcher matcher) {
                final boolean block = matcher.group(1) != null;

                final String replacement;
                if (block) {
                    replacement = "\n" + matcher.group(3) + "\n";
                } else {
                    replacement = matcher.group(7);
                }

                return new Spanner.Replacement(replacement, onMatchListener.getStyles(block));
            }
        });
    }

    public static Spanner.MatchStrategy forUnderline() {
        return STRATEGY_UNDERLINE;
    }

    public static Spanner.MatchStrategy forBoldItalic() {
        return STRATEGY_BOLD_ITALIC;
    }

    public static Spanner.MatchStrategy forBold() {
        return STRATEGY_BOLD;
    }

    public static Spanner.MatchStrategy forItalic() {
        return STRATEGY_ITALIC;
    }

    public static Spanner.MatchStrategy forStrikeThrough() {
        return STRATEGY_STRIKE_THROUGH;
    }

    public static Spanner.MatchStrategy forLinks() {
        // TODO: Links

        return null;
    }

    /**
     * TODO
     */
    public interface OnCodeBlockMatchListener {

        Collection<CharacterStyle> getStyles(final boolean block);
    }
}
