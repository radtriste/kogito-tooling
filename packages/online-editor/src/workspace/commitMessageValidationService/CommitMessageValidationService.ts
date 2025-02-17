/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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

export type CommitMessageValidation = {
  result: boolean;
  reasons?: string[];
};

export class CommitMessageValidationService {
  constructor(private readonly args: { commitMessageValidationServiceUrl: string }) {}

  public async validateCommitMessage(commitMessage: string): Promise<CommitMessageValidation> {
    try {
      const response = await fetch(this.args.commitMessageValidationServiceUrl, {
        method: "POST",
        body: commitMessage,
      });

      if (response.ok) {
        return (await response.json()) as CommitMessageValidation;
      } else {
        return {
          result: false,
          reasons: [
            `Commit message validator ${this.args.commitMessageValidationServiceUrl} is not accessible. ([HTTP ${
              response.status
            }]: ${await response.text()})`,
          ],
        };
      }
    } catch (e) {
      return {
        result: false,
        reasons: [`Commit message validator ${this.args.commitMessageValidationServiceUrl} is not accessible.`],
      };
    }
  }
}
