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

package org.kie.workbench.common.stunner.client.widgets.presenters;

import org.jboss.errai.ui.client.local.api.IsElement;
import org.kie.workbench.common.stunner.core.client.canvas.CanvasHandler;
import org.kie.workbench.common.stunner.core.client.command.CanvasCommandManager;

/**
 * A generic editor type for instances of type <code>T</code>. It provides viewer features and additionally,
 * at least, must provide a command manager instance that allows the authoring for the diagram/canvas.
 * @param <T> The instance type supported.
 * @param <H> The handler type.
 * @param <V> The view type.
 * @param <C> The callback type.
 */
public interface Editor<T, H extends CanvasHandler, V extends IsElement, C extends Viewer.Callback>
        extends Viewer<T, H, V, C> {

    /**
     * Retuns a command manager instance availabe to use for authoring purposes.
     */
    CanvasCommandManager<H> getCommandManager();
}
