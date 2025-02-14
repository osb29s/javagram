package com.rainett.javagram.update.controller;

import com.rainett.javagram.update.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * REST controller for receiving Telegram updates.
 * <p>
 * This controller exposes endpoints to accept Telegram update requests.
 * The POST endpoint processes an incoming update and delegates handling to {@link UpdateService}.
 * </p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "bot.path")
@RequestMapping("/")
public class UpdateController {
    private final UpdateService updateService;

    /**
     * Endpoint to receive and process Telegram updates.
     *
     * @param update the update received from Telegram
     * @return HTTP 202 (Accepted) if the update was processed successfully;
     *         HTTP 500 (Internal Server Error) in case of failure.
     */
    @PostMapping
    public ResponseEntity<Void> onUpdateReceived(@RequestBody Update update) {
        log.info("Received update: {}", update);
        try {
            updateService.handleUpdate(update);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            log.error("Error processing update: {}", update, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
