/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dashbuilder.dataprovider;

import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.json.DataSetDefJSONMarshallerExt;

/**
 * An enumeration of the available data set provider types.
 */
public interface DataSetProviderType<T extends DataSetDef> {

    StaticProviderType STATIC = new StaticProviderType();

    ExternalProviderType EXTERNAL = new ExternalProviderType();

    /**
     * The type's name. It must be unique and do not clash with other existing types.
     */
    String getName();

    /**
     * Create a brand new {@link DataSetDef} instance for this given provider type.
     */
    default T createDataSetDef() {
        return (T) new DataSetDef();
    }

    /**
     * A marshaller interface used during the JSON read/write of {@link DataSetDef} instances.
     */
    default DataSetDefJSONMarshallerExt<T> getJsonMarshaller() {
        return null;
    }
}
