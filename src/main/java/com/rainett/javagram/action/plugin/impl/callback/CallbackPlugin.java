package com.rainett.javagram.action.plugin.impl.callback;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Callback;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.plugin.UpdateTypePlugin;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Plugin for handling {@link Callback} annotations.
 */
@Component
@RequiredArgsConstructor
public class CallbackPlugin implements UpdateTypePlugin<Callback> {
    @Override
    public Class<Callback> getAnnotationType() {
        return Callback.class;
    }

    @Override
    @Bean("callbackResolver")
    public UpdateTypeResolver<Callback> getUpdateTypeMatcher() {
        return new CallbackResolver();
    }

    @Override
    @Bean("callbackMatcher")
    public ActionUpdateMatcher<Callback> getActionMatcher() {
        return new CallbackUpdateMatcher();
    }

    @Override
    @Bean("callbackComparator")
    public AnnotationComparator<Callback> getAnnotationComparator() {
        return new CallbackComparator();
    }
}
