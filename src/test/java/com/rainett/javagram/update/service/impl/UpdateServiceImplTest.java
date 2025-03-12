package com.rainett.javagram.update.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.container.ActionContainer;
import com.rainett.javagram.exceptions.ActionNotFoundException;
import com.rainett.javagram.exceptions.UnknownUpdateTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Update;

class UpdateServiceImplTest {
    @Mock
    private ActionContainer actionContainer;

    @Mock
    private Action action;

    @Mock
    private DefaultAction defaultAction;

    @InjectMocks
    private UpdateServiceImpl updateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleUpdate_Success() {
        Update update = mock(Update.class);
        when(actionContainer.findByUpdate(update)).thenReturn(action);
        doNothing().when(action).run(update);

        updateService.handleUpdate(update);

        verify(actionContainer, times(1)).findByUpdate(update);
        verify(action, times(1)).run(update);
    }

    @Test
    void handleUpdate_ActionNotFound() {
        Update update = mock(Update.class);
        when(actionContainer.findByUpdate(update)).thenThrow(
                new ActionNotFoundException("Action not found"));

        updateService.handleUpdate(update);

        verify(actionContainer, times(1)).findByUpdate(update);
        verify(action, never()).run(any());
    }

    @Test
    void handleUpdate_UnknownUpdateType() {
        Update update = mock(Update.class);
        when(actionContainer.findByUpdate(update)).thenThrow(
                new UnknownUpdateTypeException("Unknown update type"));

        updateService = new UpdateServiceImpl(actionContainer, null);
        updateService.handleUpdate(update);

        verify(actionContainer, times(1)).findByUpdate(update);
        verify(action, never()).run(any());
    }

    @Test
    void handleUpdate_DefaultAction() {
        Update update = mock(Update.class);
        when(actionContainer.findByUpdate(update)).thenThrow(
                new UnknownUpdateTypeException("Unknown update type"));
        updateService = new UpdateServiceImpl(actionContainer, defaultAction);

        updateService.handleUpdate(update);

        verify(actionContainer, times(1)).findByUpdate(update);
        verify(defaultAction, times(1)).run(any());
    }

    @Test
    void handleUpdate_GenericException() {
        Update update = mock(Update.class);
        when(actionContainer.findByUpdate(update)).thenThrow(
                new RuntimeException("Unexpected error"));

        updateService.handleUpdate(update);

        verify(actionContainer, times(1)).findByUpdate(update);
        verify(action, never()).run(any());
    }
}
