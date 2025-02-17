/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.client.widgets.presenters.session.impl;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.dom.client.Style;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.stunner.client.widgets.presenters.session.SessionPresenter;
import org.kie.workbench.common.stunner.core.client.canvas.event.selection.CanvasFocusedShapeEvent;

import static org.jboss.errai.common.client.dom.DOMUtil.removeAllChildren;
import static org.jboss.errai.common.client.dom.DOMUtil.removeFromParent;

// TODO: i18n.
@Dependent
@Templated
public class SessionPresenterView
        implements SessionPresenter.View {

    @Inject
    @DataField
    private HTMLDivElement canvasPanel;

    @Inject
    @DataField
    private HTMLDivElement sessionContainer;

    private ScrollType scrollType = ScrollType.AUTO;

    void onCanvasFocusedSelectionEvent(final @Observes CanvasFocusedShapeEvent event) {
        getElement().scrollLeft = event.getX();
        getElement().scrollLeft = event.getY();
    }

    @Override
    public ScrollType getContentScrollType() {
        return scrollType;
    }

    @Override
    public SessionPresenterView setCanvasWidget(final IsElement widget) {
        canvasPanel.appendChild(widget.getElement());
        return this;
    }

    @Override
    public void setContentScrollType(final ScrollType type) {
        final CSSStyleDeclaration style = sessionContainer.style;
        switch (type) {
            case AUTO:
                style.overflow = Style.Overflow.AUTO.getCssName();
                break;
            case CUSTOM:
                style.overflow = Style.Overflow.HIDDEN.getCssName();
        }
    }

    @Override
    public SessionPresenterView showError(final String message) {
        return this;
    }

    @Override
    public SessionPresenter.View showWarning(final String message) {
        return this;
    }

    @Override
    public SessionPresenterView showMessage(final String message) {
        return this;
    }

    public HTMLElement getElement() {
        return sessionContainer;
    }

    public void destroy() {
        removeAllChildren(getElement());
        removeFromParent(getElement());
        removeAllChildren(getElement());
        removeFromParent(getElement());
    }
}
