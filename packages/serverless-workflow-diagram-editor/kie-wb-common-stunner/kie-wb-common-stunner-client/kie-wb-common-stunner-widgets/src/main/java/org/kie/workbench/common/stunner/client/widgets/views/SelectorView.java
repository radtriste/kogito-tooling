/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.client.widgets.views;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.Event;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLSelectElement;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class SelectorView implements IsElement {

    @Inject
    @DataField("selector-root")
    private HTMLDivElement selectorContainer;

    @Inject
    @DataField("selector-input")
    private HTMLSelectElement selectorInput;

    private Selector<?> selector;

    public SelectorView init(final Selector<?> selector) {
        this.selector = selector;
        return this;
    }

    public SelectorView add(final String text,
                            final String value) {
        selectorInput.add(newOption(text, value));
        return this;
    }

    public String getValue() {
        return selectorInput.value;
    }

    public SelectorView clear() {
        while (selectorInput.length > 0) {
            selectorInput.remove(0);
        }
        return this;
    }

    public SelectorView setValue(final String value) {
        selectorInput.value = value;
        return this;
    }

    @PreDestroy
    public void destroy() {
        selector = null;
    }

    @EventHandler("selector-input")
    private void onValueChanged(@ForEvent("change") final Event event) {
        selector.onValueChanged();
    }

    private static HTMLOptionElement newOption(final String text,
                                               final String value) {
        final HTMLOptionElement option = (HTMLOptionElement) DomGlobal.document.createElement("option");
        option.textContent = text;
        option.value = value;
        return option;
    }
}
