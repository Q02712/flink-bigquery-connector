/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.flink.bigquery.common;

import com.google.cloud.bigquery.TableInfo;
import com.google.cloud.bigquery.storage.v1.ReadSession;

public class ReadSessionResponse {

	private final ReadSession readSession;
	private final TableInfo readTableInfo;

	public ReadSessionResponse(ReadSession readSession, TableInfo readTableInfo) {
		this.readSession = readSession;
		this.readTableInfo = readTableInfo;
	}

	public ReadSession getReadSession() {
		return readSession;
	}

	public TableInfo getReadTableInfo() {
		return readTableInfo;
	}
}
