package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.actionmatcher.AbstractActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TextUpdateMatcher extends AbstractActionUpdateMatcher<Text> {
    private static final List<CaseMatcher> CASE_MATCHERS = List.of(
            new EqualsMatcher(),
            new RegexMatcher()
    );

    @Override
    protected boolean match(Text annotation, Update update) {
        String messageText = update.getMessage().getText();
        for (CaseMatcher caseMatcher : CASE_MATCHERS) {
            if (caseMatcher.isCase(annotation)) {
                return caseMatcher.test(annotation, messageText);
            }
        }
        return matchGeneral(annotation, messageText);
    }

    @Override
    public Class<Text> getAnnotationType() {
        return Text.class;
    }

    private static boolean matchGeneral(Text text, String messageText) {
        return collectGeneralMatchers(text).stream()
                .allMatch(matcher -> matcher.test(text, messageText));
    }

    private static List<BiPredicate<Text, String>> collectGeneralMatchers(Text text) {
        List<BiPredicate<Text, String>> generalMatchers = new ArrayList<>();
        if (!text.startsWith().isEmpty()) {
            generalMatchers.add((t, s) -> s.startsWith(t.startsWith()));
        }
        if (!text.endsWith().isEmpty()) {
            generalMatchers.add((t, s) -> s.endsWith(t.endsWith()));
        }
        if (!text.contains().isEmpty()) {
            generalMatchers.add((t, s) -> s.contains(t.contains()));
        }
        return generalMatchers;
    }

    private interface CaseMatcher {
        boolean test(Text text, String messageText);

        boolean isCase(Text text);
    }

    private static class EqualsMatcher implements CaseMatcher {
        @Override
        public boolean test(Text text, String messageText) {
            return messageText.equals(text.equals());
        }

        @Override
        public boolean isCase(Text text) {
            return !text.equals().isEmpty();
        }
    }

    private static class RegexMatcher implements CaseMatcher {
        @Override
        public boolean test(Text text, String messageText) {
            return messageText.matches(text.regex());
        }

        @Override
        public boolean isCase(Text text) {
            return !text.regex().isEmpty();
        }
    }
}
