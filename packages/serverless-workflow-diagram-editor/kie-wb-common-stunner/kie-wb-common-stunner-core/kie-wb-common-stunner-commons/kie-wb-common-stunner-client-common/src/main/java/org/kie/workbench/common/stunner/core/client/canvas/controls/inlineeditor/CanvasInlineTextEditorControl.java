/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.core.client.canvas.controls.inlineeditor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.jboss.errai.ui.client.local.api.IsElement;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.components.views.FloatingView;
import org.kie.workbench.common.stunner.core.graph.Element;

@Dependent
@InlineTextEditorBox
@Default
public class CanvasInlineTextEditorControl
        extends AbstractCanvasInlineTextEditorControl {

    private final FloatingView<IsElement> floatingView;
    private final TextEditorBox<AbstractCanvasHandler, Element> textEditorBox;

    @Inject
    public CanvasInlineTextEditorControl(final FloatingView<IsElement> floatingView,
                                         final @InlineTextEditorBox TextEditorBox<AbstractCanvasHandler, Element> textEditorBox) {
        this.floatingView = floatingView;
        this.textEditorBox = textEditorBox;
    }

    @PostConstruct
    protected void initParameters() {
        isMultiline = true;
        borderOffsetX = 2d;
        borderOffsetY = 2d;
        underBoxOffset = 2d;
        topBorderOffset = -2d;
        fontSizeCorrection = 4d;
        maxInnerLeftBoxWidth = 190d;
        maxInnerLeftBoxHeight = 190d;
        maxInnerTopBoxWidth = 190d;
        maxInnerTopBoxHeight = 190d;
        scrollBarOffset = 13d;
        paletteOffsetX = 0d;
        innerBoxOffsetY = -1.1d;
    }

    @Override
    protected FloatingView<IsElement> getFloatingView() {
        return floatingView;
    }

    @Override
    protected TextEditorBox<AbstractCanvasHandler, Element> getTextEditorBox() {
        return textEditorBox;
    }
}
