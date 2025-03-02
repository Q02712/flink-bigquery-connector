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

import com.google.api.gax.rpc.HeaderProvider;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Map;

public class UserAgentHeaderProvider implements HeaderProvider, Serializable {

	private static final long serialVersionUID = 1L;
	private final String userAgent;

	public UserAgentHeaderProvider(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public Map<String, String> getHeaders() {
		return ImmutableMap.of("user-agent", userAgent);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserAgentHeaderProvider)) {
			return false;
		}
		UserAgentHeaderProvider that = (UserAgentHeaderProvider) o;
		return Objects.equal(userAgent, that.userAgent);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userAgent);
	}
}
