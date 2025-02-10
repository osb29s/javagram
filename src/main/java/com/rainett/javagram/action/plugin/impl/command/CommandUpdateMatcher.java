package com.rainett.javagram.action.plugin.impl.command;

import com.rainett.javagram.action.actionmatcher.AbstractActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Command;
import com.rainett.javagram.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * This class is responsible for matching updates based on the {@link Command} annotation.
 * Extends the {@link AbstractActionUpdateMatcher} class and implements
 * the {@link com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher} interface.
 */
@RequiredArgsConstructor
public class CommandUpdateMatcher extends AbstractActionUpdateMatcher<Command> {
    private final BotConfig botConfig;

    /**
     * Matches the update based on the {@link Command} annotation.
     * First, it checks if update is indeed a command.
     * Then, it checks if the command matches the expected command specified in the annotation.
     * @param annotation action's command annotation
     * @param update received Telegram update
     * @return {@code true} if the update matches the annotation; {@code false} otherwise
     */
    @Override
    protected boolean match(Command annotation, Update update) {
        return isCommand(update) && commandMatches(annotation, update);
    }

    /**
     * The annotation type supported by this matcher.
     * @return the annotation type
     */
    @Override
    public Class<Command> getAnnotationType() {
        return Command.class;
    }

    /**
     * Checks whether the command specified in the annotation matches the command
     * extracted from the update's message.
     *
     * @param annotation the {@code Command} annotation holding the expected command value.
     * @param update     the update containing the message.
     * @return {@code true} if the extracted command matches the expected command; {@code false}
     * otherwise.
     */
    private boolean commandMatches(Command annotation, Update update) {
        String expectedCommand = annotation.value();
        String updateText = update.getMessage().getText();
        String extractedCommand = extractCommand(updateText);
        return expectedCommand.equals(extractedCommand);
    }

    /**
     * Extracts and normalizes the command from the update text.
     * <p>
     * The command is assumed to be the first token of the message text. If this token contains an
     * '@' mention (e.g. {@code /command@BotUsername}) and the mention matches the configured bot
     * username,
     * the mention is stripped away.
     * </p>
     *
     * @param text the full text of the update message.
     * @return the normalized command string.
     */
    private String extractCommand(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String[] tokens = text.trim().split("\\s+");
        String commandToken = tokens[0];
        if (commandToken.contains("@")) {
            String[] parts = commandToken.split("@", 2);
            if (parts.length == 2 && parts[1].equalsIgnoreCase(botConfig.getUsername())) {
                commandToken = parts[0];
            }
        }
        return commandToken;
    }

    /**
     * Determines whether the update contains a valid command.
     *
     * @param update the update to check.
     * @return {@code true} if the update has a message and that message is a command.
     */
    private static boolean isCommand(Update update) {
        return update.hasMessage() && update.getMessage().isCommand();
    }
}
