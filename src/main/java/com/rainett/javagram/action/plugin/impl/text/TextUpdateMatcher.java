package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.actionmatcher.AbstractActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Text;
import java.util.List;
import java.util.function.BiPredicate;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Matches a Telegram {@link Update} based on textual criteria provided by the {@link Text}
 * annotation.
 * <p>
 * This matcher uses a collection of predicates to determine if the message text from the update
 * meets any of the criteria specified in the {@code Text} annotation. The predicates check for:
 * <ul>
 *   <li>An exact match (if {@code text.equals()} is provided).</li>
 *   <li>A prefix match (if {@code text.startsWith()} is provided).</li>
 *   <li>A suffix match (if {@code text.endsWith()} is provided).</li>
 *   <li>A substring match (if {@code text.contains()} is provided).</li>
 *   <li>A non-empty message (as a fallback).</li>
 * </ul>
 * </p>
 */
public class TextUpdateMatcher extends AbstractActionUpdateMatcher<Text> {
    private static final List<BiPredicate<Text, String>> PREDICATES = initPredicates();

    /**
     * Determines whether the provided update's text matches any criteria defined in the
     * {@link Text} annotation.
     *
     * @param annotation the {@link Text} annotation containing matching criteria
     * @param update     the Telegram update to evaluate
     * @return {@code true} if the update's message text satisfies at least one predicate;
     * {@code false} otherwise
     */
    @Override
    protected boolean match(Text annotation, Update update) {
        if (update.getMessage() == null || update.getMessage().getText() == null) {
            return false;
        }
        String messageText = update.getMessage().getText();
        return PREDICATES.stream()
                .anyMatch(predicate -> predicate.test(annotation, messageText));
    }

    /**
     * Initializes the list of predicates used for matching the update text.
     * <p>
     * The predicates are defined as follows:
     * <ul>
     *   <li><b>Exact match:</b> If {@code text.equals()} is non-empty, checks if the
     *   message text exactly matches it.</li>
     *   <li><b>Prefix match:</b> If {@code text.startsWith()} is non-empty, checks if the
     *   message text starts with it.</li>
     *   <li><b>Suffix match:</b> If {@code text.endsWith()} is non-empty, checks if the
     *   message text ends with it.</li>
     *   <li><b>Substring match:</b> If {@code text.contains()} is non-empty, checks if
     *   the message text contains it.</li>
     *   <li><b>Fallback:</b> Matches any non-empty message.</li>
     * </ul>
     * </p>
     *
     * @return a list of predicates for text matching
     */
    private static List<BiPredicate<Text, String>> initPredicates() {
        return List.of(
                (text, messageText) ->
                        !text.equals().isEmpty() && messageText.equals(text.equals()),
                (text, messageText) ->
                        !text.startsWith().isEmpty() && messageText.startsWith(text.startsWith()),
                (text, messageText) ->
                        !text.endsWith().isEmpty() && messageText.endsWith(text.endsWith()),
                (text, messageText) ->
                        !text.contains().isEmpty() && messageText.contains(text.contains()),
                (text, messageText) ->
                        !messageText.isEmpty()
        );
    }

    /**
     * Returns the annotation type that this matcher supports.
     *
     * @return {@code Text.class}, indicating that this matcher is used for the {@link Text}
     * annotation.
     */
    @Override
    public Class<Text> getAnnotationType() {
        return Text.class;
    }
}
