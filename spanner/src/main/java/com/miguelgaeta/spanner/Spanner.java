package com.miguelgaeta.spanner;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Miguel Gaeta on 4/20/16.
 *
 * A powerful spannable string builder that supports arbitrary substitutions.
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration"})
public class Spanner {

    private final String sourceString;
    private final List<MatchStrategy> matchStrategies = new ArrayList<>();

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

    public Spanner replace(final MatchStrategy... matchStrategies) {
        Collections.addAll(this.matchStrategies, matchStrategies);

        return this;
    }

    public Spannable toSpannable() {
        final List<Replacement> replacements = new ArrayList<>();
        final StringBuilder outputString = new StringBuilder(sourceString);

        for (final MatchStrategy matchStrategy : matchStrategies) {

            final Matcher matcher = matchStrategy.pattern.matcher(outputString);

            int offset = 0;

            while (matcher.find()) {
                final int startIndex = matcher.start() - offset;
                final int endIndex = matcher.end() - offset;

                final Replacement replacement = matchStrategy.onMatchListener.call(matcher);
                final String replacementString = replacement.replacementString;

                outputString.replace(startIndex, endIndex, replacementString);

                final int matchOffset = endIndex - startIndex - replacementString.length();

                replacement.start = startIndex;
                replacement.end = endIndex - matchOffset;

                offset += matchOffset;

                replacements.add(replacement);

                if (offset == 0) {
                    continue;
                }

                for (final Replacement existingReplacement : replacements) {

                    if (existingReplacement.start > endIndex) {
                        existingReplacement.start -= offset;
                        existingReplacement.end -= offset;
                    }

                    /*
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
                    */
                }
            }
        }

        return buildSpannableString(outputString.toString(), replacements);
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
    private static Spannable buildSpannableString(final String sourceString, final Collection<Replacement> replacements) {
        final Spannable spannableString = new SpannableString(sourceString);

        try {
            for (final Replacement replacement : replacements) {
                for (final Object characterStyle : replacement.replacementSpans) {
                    spannableString.setSpan(characterStyle, replacement.start, replacement.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.i("Spanner", "Span cannot be applied, index out of bounds.", e);
        }

        return spannableString;
    }

    /**
     * TODO
     */
    public interface OnMatchListener {

        Replacement call(final Matcher matcher);
    }

    /**
     * Represents a desired match strategy with a callback
     * to allow the user to return a replacement string
     * along with desired spans to apply to it.
     */
    public static class MatchStrategy {

        protected final Pattern pattern;
        protected OnMatchListener onMatchListener;

        public MatchStrategy(final String regex, final OnMatchListener onMatchListener) {
            this.pattern = Pattern.compile(regex);
            this.onMatchListener = onMatchListener;
        }

        public MatchStrategy(final Pattern pattern, final OnMatchListener onMatchListener) {
            this.pattern = pattern;
            this.onMatchListener = onMatchListener;
        }
    }

    /**
     * TODO
     */
    public static class Replacement {

        final String replacementString;
        final List<Object> replacementSpans;

        int start;
        int end;

        public Replacement(final String replacementString, final List<Object> replacementSpans) {
            this.replacementString = replacementString;
            this.replacementSpans = replacementSpans;
        }

        public Replacement(final String replacementString, Object... spanStyles) {
            this(replacementString, Arrays.asList(spanStyles));
        }

        public Replacement(final String replacementString) {
            this(replacementString, Collections.<CharacterStyle>emptyList());
        }
    }
}
