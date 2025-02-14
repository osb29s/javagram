package com.rainett.javagram.action.plugin.impl.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.javagram.action.annotations.Command;
import com.rainett.javagram.config.BotConfig;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class CommandUpdateMatcherTest {
    private static final BotConfig BOT_CONFIG = new BotConfig();
    private final CommandUpdateMatcher commandUpdateMatcher = new CommandUpdateMatcher(BOT_CONFIG);

    static Stream<JsonTestCase> jsonTestCases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = CommandUpdateMatcher.class
                .getResourceAsStream("/commandUpdateMatcherTests.json");
        List<JsonTestCase> testCases = mapper.readValue(is, new TypeReference<>() {
        });
        return testCases.stream();
    }

    @BeforeAll
    static void beforeAll() {
        BOT_CONFIG.setUsername("@testbot");
    }

    @ParameterizedTest
    @MethodSource("jsonTestCases")
    void testTextUpdateMatcher(JsonTestCase testCase) {
        Command annotation = new CommandImpl(testCase.command);
        Message message = new Message();
        message.setText(testCase.text);
        Update update = new Update();
        update.setMessage(message);

        boolean result = commandUpdateMatcher.match(annotation, update);

        assertEquals(testCase.expected, result, "Failed for annotation: "
                                                + annotation + " and update: " + testCase.text);
    }

    @Test
    void returnsTextAnnotationType() {
        assertEquals(Command.class, commandUpdateMatcher.getAnnotationType());
    }

    @Data
    private static class JsonTestCase {
        private String command;
        private String text;
        private boolean expected;
    }

    @AllArgsConstructor
    @ToString
    private static class CommandImpl implements Command {
        private String value;

        @Override
        public String value() {
            return value;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Command.class;
        }
    }
}
