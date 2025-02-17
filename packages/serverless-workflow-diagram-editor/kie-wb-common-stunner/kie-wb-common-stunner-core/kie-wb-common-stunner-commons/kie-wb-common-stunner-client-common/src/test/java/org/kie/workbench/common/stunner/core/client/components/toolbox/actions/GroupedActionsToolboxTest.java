/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.core.client.components.toolbox.actions;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.shape.Shape;
import org.kie.workbench.common.stunner.core.definition.shape.ShapeGlyph;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GroupedActionsToolboxTest {

    private static final String E_UUID = "e1";
    private static final String DEFINITION_ID = "def1";
    private static final String ACTION1_TITLE = "action1";
    private static final String ACTION2_TITLE = "action2";
    private static final String ACTION3_TITLE = "action3";

    @Mock
    private ToolboxAction<AbstractCanvasHandler> action1;

    @Mock
    private ToolboxAction<AbstractCanvasHandler> action2;

    @Mock
    private ToolboxAction<AbstractCanvasHandler> action3;

    @Mock
    private ActionsToolboxView<?> view;

    @Mock
    private AbstractCanvasHandler canvasHandler;

    @Mock
    private AbstractCanvas canvas;

    @Mock
    private Element<?> element;

    @Mock
    private Shape<?> shape;

    private HashMap<ToolboxAction<AbstractCanvasHandler>, String> connectorActions;
    private HashMap<ToolboxAction<AbstractCanvasHandler>, String> nodeActions;
    private ShapeGlyph glyph1;
    private ShapeGlyph glyph2;
    private ShapeGlyph glyph3;
    private GroupedActionsToolbox<ActionsToolboxView<?>> tested;

    @Before
    public void setup() throws Exception {
        glyph1 = ShapeGlyph.create();
        glyph1.setDefinitionId("d1");
        glyph2 = ShapeGlyph.create();
        glyph2.setDefinitionId("d2");
        glyph3 = ShapeGlyph.create();
        glyph3.setDefinitionId("d3");
        connectorActions = new HashMap<>();
        nodeActions = new HashMap<>();
        connectorActions.put(action1, DEFINITION_ID);
        nodeActions.put(action2, DEFINITION_ID);
        nodeActions.put(action3, DEFINITION_ID);

        when(canvasHandler.getCanvas()).thenReturn(canvas);
        when(canvasHandler.getAbstractCanvas()).thenReturn(canvas);
        when(element.getUUID()).thenReturn(E_UUID);
        when(canvas.getShape(eq(E_UUID))).thenReturn(shape);
        when(action1.getGlyph(eq(canvasHandler),
                              anyString())).thenReturn(glyph1);
        when(action1.getTitle(eq(canvasHandler),
                              anyString())).thenReturn(ACTION1_TITLE);
        when(action2.getGlyph(eq(canvasHandler),
                              anyString())).thenReturn(glyph2);
        when(action2.getTitle(eq(canvasHandler),
                              anyString())).thenReturn(ACTION2_TITLE);
        when(action3.getTitle(eq(canvasHandler),
                              anyString())).thenReturn(ACTION3_TITLE);

        tested = new GroupedActionsToolbox<>(() -> canvasHandler,
                                             element,
                                             view);

        tested.setConnectorActions(connectorActions);
        tested.setNodeActions(nodeActions);
    }

    @Test
    public void testGetters() {
        assertEquals(E_UUID,
                     tested.getElementUUID());
        assertEquals(canvas,
                     tested.getCanvas());
        assertEquals(shape,
                     tested.getShape());
        assertEquals(3,
                     tested.size());
        assertEquals(1,
                     tested.getConnectorSize());
        assertEquals(2,
                     tested.getNodeSize(DEFINITION_ID));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testInit() {
        tested.init();
        verify(view,
               times(1)).init(eq(tested));
    }

    @Test
    public void testShow() {
        tested.show();
        verify(view,
               times(1)).show();
    }

    @Test
    public void testShowOnlyOnce() {
        tested.show();
        tested.show();
        verify(view,
               times(1)).show();
    }

    @Test
    public void testHide() {
        tested.hide();
        verify(view,
               times(1)).hide();
    }

    @Test
    public void testDestroy() {
        tested.destroy();
        verify(view,
               times(1)).destroy();
    }

    @Test
    public void testHideAndDestroy() {
        tested.hideAndDestroy();
        verify(view, times(1)).hideAndDestroy();
    }
}
