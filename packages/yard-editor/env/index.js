/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const { varsWithName, composeEnv } = require("@kie-tools/build-env");

module.exports = composeEnv([require("@kie-tools/build-env/env")], {
  vars: varsWithName({}),
  get env() {
    return {
      yardEditor: {
        dev: {
          port: 9009,
        },
      },
    };
  },
});
