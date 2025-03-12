package com.rainett.javagram.keyboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

class InlineKeyboardBuilderTest {
    @Test
    void testInlineKeyboardBuilder() {
        InlineKeyboardBuilder builder = new InlineKeyboardBuilder();
        InlineKeyboardMarkup markup = builder
                .row()
                .button("Next", "next-page", "42")
                .button("Previous", "prev-page")
                .endRow()
                .build();

        assertNotNull(markup, "InlineKeyboardMarkup should not be null");
        assertNotNull(markup.getKeyboard(), "Keyboard list should not be null");

        List<List<InlineKeyboardButton>> keyboard = markup.getKeyboard();
        assertEquals(1, keyboard.size(), "There should be exactly one row");

        List<InlineKeyboardButton> row = keyboard.get(0);
        assertEquals(2, row.size(), "Row should contain 2 buttons");

        InlineKeyboardButton button1 = row.get(0);
        assertEquals("Next", button1.getText(), "First button text should be 'Next'");
        assertEquals("next-page:42", button1.getCallbackData(),
                "First button callback data should be 'next-page:42'");

        InlineKeyboardButton button2 = row.get(1);
        assertEquals("Previous", button2.getText(), "Second button text should be 'Previous'");
        assertEquals("prev-page", button2.getCallbackData(),
                "Second button callback data should be 'prev-page'");
    }
}