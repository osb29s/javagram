package com.rainett.javagram.action.container;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.BotAction;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * A collector for {@link Action} instances. Implementations of this interface are responsible
 * for retrieving, grouping, and organizing action beans (typically from a Spring application
 * context) based on a specific annotation. The grouping key is the annotation type.
 */
public interface ActionCollector {

    /**
     * Collects {@link Action} instances and groups them by an annotation type.
     * The grouping key is determined by the first annotation on the
     * action that is meta-annotated with a specific marker - {@link BotAction}.
     *
     * @return a map where each key is an annotation type and the corresponding value is a
     * list of {@link Action} instances associated with that annotation.
     */
    Map<Class<? extends Annotation>, List<Action>> collectActions();
}
