package com.rainett.javagram.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExceptionsTest {
    @Test
    void actionNotFoundException_storesMessage() {
        var ex = new ActionNotFoundException("msg");
        assertEquals("msg", ex.getMessage());
    }

    @Test
    void unknownUpdateTypeException_storesMessage() {
        var ex = new UnknownUpdateTypeException("msg");
        assertEquals("msg", ex.getMessage());
    }
}
