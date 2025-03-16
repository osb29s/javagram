package com.rainett.javagram.action.container.impl;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.BotAction;
import com.rainett.javagram.action.comparator.ActionComparatorService;
import com.rainett.javagram.action.container.ActionCollector;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link ActionCollector} interface that collects actions
 * from the Spring application context. Actions are grouped and sorted based on the annotation
 * that is meta-annotated with {@link BotAction}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActionCollectorImpl implements ActionCollector {
    private final ApplicationContext context;
    private final ActionComparatorService actionComparatorService;

    /**
     * Collects all beans annotated with {@link BotAction} from the application context,
     * filters out those that are not instances of {@link Action}, groups them by the action type,
     * and sorts each group using a comparator provided by {@link ActionComparatorService}.
     *
     * @return a map where the key is the annotation type and the value is the list of actions
     * associated with that annotation.
     */
    @Override
    public Map<Class<? extends Annotation>, List<Action>> collectActions() {
        Map<Class<? extends Annotation>, List<Action>> actions = context
                .getBeansWithAnnotation(BotAction.class)
                .values()
                .stream()
                .filter(this::isAction)
                .map(this::castToAction)
                .collect(Collectors.groupingBy(this::extractAnnotationType));

        actions.forEach(this::sortActions);
        return actions;
    }

    /**
     * Checks if the provided object is an instance of {@link Action}.
     *
     * @param object the object to check.
     * @return true if the object is an instance of {@link Action}, false otherwise.
     */
    private boolean isAction(Object object) {
        return object instanceof Action;
    }

    /**
     * Casts the provided object to {@link Action}.
     *
     * @param object the object to cast.
     * @return the cast {@link Action} instance.
     */
    private Action castToAction(Object object) {
        return (Action) object;
    }

    /**
     * Sorts the list of actions based on the comparator provided by the
     * {@link ActionComparatorService}.
     *
     * @param annotationType the annotation type that identifies the action group.
     * @param actions        the list of actions to sort.
     */
    private void sortActions(Class<? extends Annotation> annotationType, List<Action> actions) {
        Comparator<Action> comparator =
                actionComparatorService.getActionComparator(annotationType);
        actions.sort(comparator.reversed());
    }

    /**
     * Extracts an annotation type from the provided action.
     * Iterates over all action's annotations and searches for the one
     * that is meta-annotated with {@link BotAction}.
     * @param action the action to extract the annotation type from
     * @return the annotation type
     * @throws IllegalStateException if no annotation is found
     */
    private Class<? extends Annotation> extractAnnotationType(Action action) {
        Objects.requireNonNull(action, "Action must not be null");
        Class<?> targetClass = action.getClass();
        while (targetClass != null && targetClass != Object.class) {
            for (Annotation annotation : targetClass.getAnnotations()) {
                if (AnnotationUtils.findAnnotation(annotation.annotationType(), BotAction.class) != null) {
                    return annotation.annotationType();
                }
            }
            targetClass = targetClass.getSuperclass();
        }
        throw new IllegalStateException(
                String.format("Cannot find required annotation for action [%s].",
                        action.getClass().getName())
        );
    }
}
