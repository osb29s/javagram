package com.rainett.javagram.update.service.impl;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.container.ActionContainer;
import com.rainett.javagram.exceptions.ActionNotFoundException;
import com.rainett.javagram.exceptions.UnknownUpdateTypeException;
import com.rainett.javagram.update.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Implementation of the {@link UpdateService} interface.
 * <p>
 * This service delegates the processing of Telegram updates to a specific {@link Action}
 * obtained from an {@link ActionContainer}. It logs key events and handles exceptions
 * to ensure that errors are captured and managed appropriately.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {
    private final ActionContainer actionContainer;

    /**
     * Processes an incoming Telegram update.
     * <p>
     * The update is used to determine the appropriate {@link Action} from the
     * {@link ActionContainer}. If an action is found, it is executed; otherwise, a warning
     * is logged. Any exceptions thrown during the process are caught and logged as errors.
     * </p>
     *
     * @param update the Telegram update to process
     */
    @Override
    public void handleUpdate(Update update) {
        try {
            Action botAction = actionContainer.findByUpdate(update);
            log.debug("Executing action: {} for update: {}",
                    botAction.getClass().getSimpleName(), update);
            botAction.run(update);
        } catch (ActionNotFoundException | UnknownUpdateTypeException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.error("Error processing update: {}", update, e);
        }
    }
}
