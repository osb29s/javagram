package com.rainett.javagram.keyboard;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Builder for inline keyboard.
 */
public class InlineKeyboardBuilder {
    private final List<List<InlineKeyboardButton>> keyboard;

    /**
     * Creates a new instance of InlineKeyboardBuilder.
     */
    public InlineKeyboardBuilder() {
        keyboard = new ArrayList<>();
    }

    /**
     * Creates a new RowBuilder.
     * @return a new RowBuilder
     */
    public RowBuilder row() {
        return new RowBuilder(this);
    }

    /**
     * Builds the inline keyboard.
     * @return the built inline keyboard
     */
    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    private void addRow(List<InlineKeyboardButton> row) {
        keyboard.add(row);
    }

    /**
     * Builder for inline keyboard rows.
     */
    public static class RowBuilder {
        private final InlineKeyboardBuilder parent;
        private final List<InlineKeyboardButton> row = new ArrayList<>();
        private static final String DELIMITER = ":";

        /**
         * Creates a new RowBuilder instance with the specified parent.
         * @param parent the parent InlineKeyboardBuilder
         */
        public RowBuilder(InlineKeyboardBuilder parent) {
            this.parent = parent;
        }

        /**
         * Adds a button to the row.
         * Uses separated callback key and data, which are combined using the DELIMITER.
         * Combined string is used as callback_data.
         * @param text button text
         * @param key callback key
         * @param data callback data
         * @return the current RowBuilder
         */
        public RowBuilder button(String text, String key, String data) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(key + DELIMITER + data);
            row.add(button);
            return this;
        }

        /**
         * Adds a button to the row.
         * This button doesn't have callback data, only callback key.
         * @param text button text
         * @param key callback key
         * @return the current RowBuilder
         */
        public RowBuilder button(String text, String key) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(key);
            row.add(button);
            return this;
        }

        /**
         * Ends the row and adds it to the parent.
         * @return the parent InlineKeyboardBuilder
         */
        public InlineKeyboardBuilder endRow() {
            parent.addRow(row);
            return parent;
        }
    }
}