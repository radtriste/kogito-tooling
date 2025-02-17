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

package org.kie.workbench.common.stunner.client.widgets.notification;

import java.util.Collection;
import java.util.Optional;

import org.kie.workbench.common.stunner.core.rule.RuleViolation;
import org.kie.workbench.common.stunner.core.validation.DiagramElementViolation;

public final class ValidationFailedNotification extends AbstractNotification<Collection<DiagramElementViolation<RuleViolation>>> {

    private final Collection<DiagramElementViolation<RuleViolation>> violations;

    @SuppressWarnings("unchecked")
    ValidationFailedNotification(final Collection<DiagramElementViolation<RuleViolation>> source,
                                 final NotificationContext context,
                                 final String message,
                                 final Type type) {
        super(context,
              type,
              message);
        this.violations = source;
    }

    @Override
    public Optional<Collection<DiagramElementViolation<RuleViolation>>> getSource() {
        return Optional.of(violations);
    }

}
