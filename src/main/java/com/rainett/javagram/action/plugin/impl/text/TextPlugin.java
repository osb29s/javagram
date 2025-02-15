package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Text;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.plugin.UpdateTypePlugin;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Plugin for handling {@link Text} annotations.
 * <p>
 * This plugin provides the necessary components for processing updates related to {@code Text}
 * annotations. It supplies:
 * <ul>
 *   <li>An {@link UpdateTypeResolver} to determine if an update contains textual content.</li>
 *   <li>An {@link ActionUpdateMatcher} to match an update against the {@code Text} criteria.</li>
 *   <li>An {@link AnnotationComparator} to compare multiple {@code Text} annotations by their
 *   properties.</li>
 * </ul>
 */
@Component
public class TextPlugin implements UpdateTypePlugin<Text> {

    /**
     * Returns the annotation type supported by this plugin.
     *
     * @return {@code Text.class} as the supported annotation type.
     */
    @Override
    public Class<Text> getAnnotationType() {
        return Text.class;
    }

    /**
     * Creates and returns an {@link UpdateTypeResolver} for {@link Text} annotations.
     * <p>
     * This resolver checks if a Telegram update contains a message with text.
     * </p>
     *
     * @return a new {@link TextResolver} instance.
     */
    @Override
    @Bean("textResolver")
    public UpdateTypeResolver<Text> getUpdateTypeMatcher() {
        return new TextResolver();
    }

    /**
     * Creates and returns an {@link ActionUpdateMatcher} for {@link Text} annotations.
     * <p>
     * This matcher uses textual criteria defined in the {@code Text} annotation to decide whether
     * an update should trigger the corresponding action.
     * </p>
     *
     * @return a new {@link TextUpdateMatcher} instance.
     */
    @Override
    @Bean("textMatcher")
    public ActionUpdateMatcher<Text> getActionMatcher() {
        return new TextUpdateMatcher();
    }

    /**
     * Creates and returns an {@link AnnotationComparator} for {@link Text} annotations.
     * <p>
     * The comparator is used to compare {@code Text} annotations, prioritizing properties such as
     * exact match, prefix, suffix, and substring conditions.
     * </p>
     *
     * @return a new {@link TextComparator} instance.
     */
    @Override
    @Bean("textComparator")
    public AnnotationComparator<Text> getAnnotationComparator() {
        return new TextComparator();
    }
}
