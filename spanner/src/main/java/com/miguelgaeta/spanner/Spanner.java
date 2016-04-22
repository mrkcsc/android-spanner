package com.miguelgaeta.spanner;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Miguel Gaeta on 4/20/16.
 *
 * A powerful spannable string builder that supports arbitrary substitutions.
 */
public class Spanner {

    private String sourceString = "";
    private final List<MatchStrategy> matchStrategies = new ArrayList<>();
    private final List<Replacement> replacements = new ArrayList<>();

    public Spanner(final Context context, final int stringResId) {
        this.sourceString = context.getString(stringResId);
    }

    public Spanner(final Context context, final int stringResId, Object... args) {
        this.sourceString = String.format(context.getString(stringResId), args);
    }

    public Spanner(final String sourceString) {
        this.sourceString = sourceString;
    }

   public Spanner(final String sourceString, Object... args) {
        this.sourceString = String.format(sourceString, args);
    }

    @SuppressWarnings("unused")
    public Spanner addReplacementStrategy(final OnMatchListener onMatchListener, final String start) {
        return addReplacementStrategy(onMatchListener, start, null);
    }

    public Spanner addReplacementStrategy(final OnMatchListener onMatchListener, final String start, final String end) {
        return addReplacementStrategy(onMatchListener, start, end, true);
    }

    public Spanner addReplacementStrategy(final OnMatchListener onMatchListener, final String start, final String end, boolean endRequired) {
        return addReplacementStrategy(onMatchListener, start, end, endRequired, false);
    }

    public Spanner addReplacementStrategy(final OnMatchListener onMatchListener, final String start, final String end, final boolean endRequired, final boolean endWithWhitespaceOrEOL) {
        matchStrategies.add(new MatchStrategy(onMatchListener, start, end, endRequired, endWithWhitespaceOrEOL));

        return this;
    }

    @SuppressWarnings("unused")
    public Spanner addMarkdownStrategy() {
        addMarkdownBoldStrategy();

        addReplacementStrategy(new OnMatchListener() {
            @Override
            public Replacement call(String match) {
                return new Replacement(match, SpanHelpers.createItalicSpan());
            }
        }, "*", "*");

        addReplacementStrategy(new OnMatchListener() {
            @Override
            public Replacement call(String match) {
                return new Replacement(match, SpanHelpers.createStrikethroughSpan());
            }
        }, "~~", "~~");

        addReplacementStrategy(new OnMatchListener() {
            @Override
            public Replacement call(String match) {
                return new Replacement(match, SpanHelpers.createUnderlineSpan());
            }
        }, "__", "__");

        addReplacementStrategy(new OnMatchListener() {
            @Override
            public Replacement call(String match) {
                return new Replacement(match, SpanHelpers.createItalicSpan());
            }
        }, "_", "_", true, true);

        return this;
    }

    public Spanner addMarkdownBoldStrategy() {
        addReplacementStrategy(new OnMatchListener() {
            @Override
            public Replacement call(String match) {
                return new Replacement(match, SpanHelpers.createBoldItalicSpan());
            }
        }, "***", "***");

        addReplacementStrategy(new OnMatchListener() {
            @Override
            public Replacement call(String match) {
                return new Replacement(match, SpanHelpers.createBoldSpan());
            }
        }, "**", "**");

        return this;
    }

    public SpannableString toSpannableString() {

        for (final MatchStrategy matchStrategy : matchStrategies) {
            int startIndex = 0;

            do {
                startIndex = sourceString.indexOf(matchStrategy.start, startIndex);

                if (startIndex != -1) {
                    final int startIndexOffset = matchStrategy.start.length();

                    if (matchStrategy.end == null) {

                        int endIndex = startIndex + startIndexOffset;
                        final Replacement replacement = matchStrategy.onMatchListener.call(matchStrategy.start);

                        startIndex = computeStartIndexWithSpans(startIndex, startIndexOffset, endIndex, replacement);

                    } else {

                        int endIndex = sourceString.indexOf(matchStrategy.end, startIndex + startIndexOffset);
                        final boolean isEOLMatch = endIndex == -1 && !matchStrategy.endRequired;

                        if (isEOLMatch) {
                            endIndex = sourceString.length();
                        }

                        if (matchStrategy.endWithWhitespaceOrEOL && endIndex != -1) {

                            if (endIndex != (sourceString.length() - 1) && !Character.isWhitespace(sourceString.charAt(endIndex + 1))) {
                                endIndex = -1;
                            }
                        }

                        if (endIndex != -1) {

                            final String match = sourceString.substring(startIndex + startIndexOffset, endIndex);
                            final Replacement replacement = matchStrategy.onMatchListener.call(match);

                            if (!isEOLMatch) {
                                endIndex += matchStrategy.end.length();
                            }

                            startIndex = computeStartIndexWithSpans(startIndex, startIndexOffset, endIndex, replacement);

                        } else {
                            startIndex = -1;
                        }
                    }
                }

            } while (startIndex != -1);
        }

        return buildSpannableString(sourceString, replacements);
    }

    private int computeStartIndexWithSpans(final int startIndex, final int startIndexOffset, final int endIndex, final Replacement replacement) {

        // Replace match with user provided replacement.
        sourceString = new StringBuilder(sourceString).replace(startIndex, endIndex, replacement.replacementString).toString();

        // Update the new end index location.
        final int endIndexUpdated = startIndex + replacement.replacementString.length();

        final int offset = (endIndex - startIndex) - (endIndexUpdated - startIndex);

        if (offset != 0) {

            for (final Replacement existingReplacement : replacements) {

                if (existingReplacement.start > startIndex) {
                    existingReplacement.start -= startIndexOffset;

                    if (existingReplacement.start > endIndexUpdated) {
                        existingReplacement.start -= offset - startIndexOffset;
                    }
                }

                if (existingReplacement.end > startIndex) {
                    existingReplacement.end -= startIndexOffset;

                    if (existingReplacement.end > endIndexUpdated) {
                        existingReplacement.end -= offset - startIndexOffset;
                    }
                }
            }
        }

        replacement.start = startIndex;
        replacement.end = endIndexUpdated;

        replacements.add(replacement);

        return endIndexUpdated;
    }

    /**
     * Given a source string and a corresponding list of replacement objects,
     * transform into a spannable string spans applied from each replacement.
     *
     * Assumes the source string has been formatted with string replacements
     * during the computation step.
     *
     * @param sourceString Source string with replacements.
     * @param replacements Replacement objects with desired spans to apply at start and end indices.
     *
     * @return Source string with spans applied.
     */
    private static SpannableString buildSpannableString(final String sourceString, final Collection<Replacement> replacements) {
        final SpannableString spannableString = new SpannableString(sourceString);

        try {
            for (final Replacement replacement : replacements) {
                for (final CharacterStyle characterStyle : replacement.replacementSpans) {
                    spannableString.setSpan(characterStyle, replacement.start, replacement.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.i("Spanner", "Span cannot be applied, index out of bounds.", e);
        }

        return spannableString;
    }

    @SuppressWarnings("unused")
    public interface OnMatchListener {

        Replacement call(final String match);
    }

    /**
     * Represents a desired match strategy with a callback
     * to allow the user to return a replacement string
     * along with desired spans to apply to it.
     *
     * TODO: This could be made simpler by being represented as a regex.
     */
    private static class MatchStrategy {

        final OnMatchListener onMatchListener;
        final String start;
        final String end;
        final boolean endRequired;
        final boolean endWithWhitespaceOrEOL;

        private MatchStrategy(final OnMatchListener onMatchListener, final String start, final String end, final boolean endRequired, final boolean endWithWhitespaceOrEOL) {
            this.onMatchListener = onMatchListener;
            this.start = start;
            this.end = end;
            this.endRequired = endRequired;
            this.endWithWhitespaceOrEOL = endWithWhitespaceOrEOL;
        }
    }

    @SuppressWarnings("unused")
    public static class Replacement {

        final String replacementString;
        final List<CharacterStyle> replacementSpans;

        int start;
        int end;

        public Replacement(final String replacementString, final List<CharacterStyle> replacementSpans) {
            this.replacementString = replacementString;
            this.replacementSpans = replacementSpans;
        }

        public Replacement(final String replacementString, CharacterStyle... spanStyles) {
            this(replacementString, Arrays.asList(spanStyles));
        }

        public Replacement(final String replacementString) {
            this(replacementString, Collections.<CharacterStyle>emptyList());
        }
    }
}
