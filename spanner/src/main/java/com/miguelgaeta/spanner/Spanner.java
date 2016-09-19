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
import java.util.Iterator;
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
        final StringBuilder spannable = new StringBuilder(sourceString);

        for (final MatchStrategy matchStrategy : matchStrategies) {

            final Matcher matcher = matchStrategy.pattern.matcher(spannable);

            int totalOffset = 0;

            while (matcher.find()) {

                final int startIndex = matcher.start() - totalOffset;
                final int endIndex = matcher.end() - totalOffset;

                final Replacement replacement = matchStrategy.onMatchListener.call(matcher);
                final String replacementString = replacement.replacementString;

                spannable.replace(startIndex, endIndex, replacementString);

                final int offset = endIndex - startIndex - replacementString.length();

                replacement.start = startIndex;
                replacement.end = endIndex - offset;

                totalOffset += offset;

                updateExistingReplacements(spannable, replacements, startIndex, endIndex, offset);

                if (replacement.replacementString.length() > 0) {
                    replacements.add(replacement);
                }
            }
        }

        return buildSpannableString(spannable.toString(), replacements);
    }

    /**
     * Given the current spannable string, replacements and a match
     * start and end index (before replacement applied) with
     * the replacement offset, attempt to shift
     * any existing replacements that may come after it
     * and re-establish the indices of replacements within it.
     */
    private static void updateExistingReplacements(final StringBuilder spannable, final List<Replacement> replacements, final int startIndex, final int endIndex, final int offset) {
        if (offset == 0) {
            return;
        }

        final Iterator<Replacement> existingReplacementIterator = replacements.iterator();

        while (existingReplacementIterator.hasNext()) {
            final Replacement existingReplacement = existingReplacementIterator.next();

            if (existingReplacement.start > endIndex) {
                existingReplacement.start -= offset;
                existingReplacement.end -= offset;

            } else if (existingReplacement.start >= startIndex) {

                // Pull down the replacement string.
                final String replacementString = spannable.substring(startIndex, endIndex - offset);

                // Look for the existing replacement string within it to shift it's indices.
                final int shiftedStartIndex = replacementString.indexOf(existingReplacement.replacementString);

                if (shiftedStartIndex == -1) {
                    existingReplacementIterator.remove();
                } else {
                    existingReplacement.start = shiftedStartIndex;
                    existingReplacement.end = shiftedStartIndex + existingReplacement.replacementString.length();
                }
            }
        }
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
                for (final CharacterStyle characterStyle : replacement.replacementSpans) {
                    spannableString.setSpan(characterStyle, replacement.start, replacement.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            Log.i("Spanner", "Span cannot be applied, index out of bounds.", e);
        }

        return spannableString;
    }

    /**
     * Called when a match is found given the
     * provided match strategy pattern.
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
     * Represents a replacement for a match on the
     * input string.  Contains a user provided
     * replacement string and spans.
     *
     * Internally maintains a start and end
     * index of replaced match.
     */
    public static class Replacement {

        final String replacementString;
        final Collection<CharacterStyle> replacementSpans;

        int start;
        int end;

        public Replacement(final String replacementString, final Collection<CharacterStyle> replacementSpans) {
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
