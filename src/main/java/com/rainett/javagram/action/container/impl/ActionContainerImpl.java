package com.rainett.javagram.action.container.impl;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.container.ActionCollector;
import com.rainett.javagram.action.container.ActionContainer;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import com.rainett.javagram.exceptions.ActionNotFoundException;
import com.rainett.javagram.exceptions.UnknownUpdateTypeException;
import jakarta.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Implementation of the {@link ActionContainer} that holds and matches actions based on the
 * update received.
 * <p>
 * At startup, it collects all actions via the {@link ActionCollector} and groups them by an
 * annotation type. When an update arrives, it resolves the update’s type (using
 * {@link UpdateTypeResolver} instances) and then selects an appropriate action from the
 * corresponding group by checking which action matches the update via
 * an {@link ActionUpdateMatcher}.
 * </p>
 */
@Slf4j
@Service
public class ActionContainerImpl implements ActionContainer {
    private final ActionCollector actionCollector;
    private final List<UpdateTypeResolver<?>> updateTypeResolvers;
    private final Map<Class<? extends Annotation>, ActionUpdateMatcher<?>> actionMatchers;
    private Map<Class<? extends Annotation>, List<Action>> actions;

    public ActionContainerImpl(ActionCollector actionCollector,
                               List<UpdateTypeResolver<?>> updateTypeResolvers,
                               List<ActionUpdateMatcher<?>> actionUpdateMatchers) {
        this.actionCollector = actionCollector;
        this.updateTypeResolvers = updateTypeResolvers;
        this.actionMatchers = actionUpdateMatchers.stream()
                .collect(Collectors.toMap(ActionUpdateMatcher::getAnnotationType,
                        Function.identity()));
    }

    /**
     * Finds the matching {@link Action} for the given update.
     *
     * @param update the update to handle; must not be {@code null}
     * @return the matching action
     * @throws IllegalStateException if no matching action is found or if there are no actions
     * for the resolved type
     */
    @Override
    public Action findByUpdate(Update update) {
        Class<? extends Annotation> annotationType = resolveUpdateType(update);
        List<Action> actionsByAnnotationType = actions.get(annotationType);
        return getMatchingAction(actionsByAnnotationType, update, annotationType);
    }

    /**
     * Initializes the container by collecting and grouping actions from the application context.
     * This method is invoked after dependency injection is complete.
     */
    @PostConstruct
    private void init() {
        actions = actionCollector.collectActions();
        log.info("Actions collected successfully: {}", actions);
    }

    /**
     * Resolves the update type by testing the update against available
     * {@link UpdateTypeResolver} instances.
     *
     * @param update the update instance; must not be {@code null}
     * @return the annotation class representing the update type
     * @throws UnknownUpdateTypeException if no matching update type is found for the update
     */
    private Class<? extends Annotation> resolveUpdateType(Update update) {
        return updateTypeResolvers.stream()
                .filter(matcher -> matcher.test(update))
                .findFirst()
                .orElseThrow(() -> new UnknownUpdateTypeException("Unknown update type: "
                                                                  + update))
                .getAnnotationType();
    }

    /**
     * Retrieves a matching action from the provided list by filtering on whether an action
     * supports the given update.
     *
     * @param actionsByAnnotationType the list of actions united by a specific annotation type;
     *                                may be {@code null} or empty
     * @param update                  the update instance to match against
     * @param annotationType          the annotation class representing the update type
     * @return the matching action
     * @throws ActionNotFoundException if no matching action is found
     * @throws IllegalStateException   if there are no action matchers for the annotation type
     */
    private Action getMatchingAction(List<Action> actionsByAnnotationType,
                                     Update update,
                                     Class<? extends Annotation> annotationType) {
        if (actionsByAnnotationType == null || actionsByAnnotationType.isEmpty()) {
            throw new ActionNotFoundException("No actions available for update: " + update);
        }
        ActionUpdateMatcher<?> matcher = actionMatchers.get(annotationType);
        if (matcher == null) {
            throw new IllegalStateException("No action matcher registered for annotation type: "
                                            + annotationType.getSimpleName());
        }
        return actionsByAnnotationType.stream()
                .filter(action -> matcher.test(action, update))
                .findFirst()
                .orElseThrow(() -> new ActionNotFoundException("No actions available for "
                                                                + "update: " + update));
    }
}
