/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.sw.client.shapes;

import org.jboss.errai.ui.client.local.spi.TranslationService;

import static org.kie.workbench.common.stunner.sw.resources.i18n.SWConstants.SHAPE_END;

public class EndShape extends ServerlessWorkflowShape<EndShapeView> {

    public EndShape(TranslationService translationService) {
        super(new EndShapeView(translationService.getTranslation(SHAPE_END)).asAbstractShape(), translationService);
        getShape().getShapeView().setController(this);
    }
}