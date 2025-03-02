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
package com.google.cloud.flink.bigquery.arrow.util;

import org.apache.arrow.vector.BigIntVector;
import org.apache.flink.table.data.vector.LongColumnVector;
import org.apache.flink.util.Preconditions;

/** Arrow column vector for BigInt. */

public final class ArrowBigIntColumnVector implements LongColumnVector {

	/**
	 * Container which is used to store the sequence of bigint values of a column to
	 * read.
	 */
	private final BigIntVector bigIntVector;

	public ArrowBigIntColumnVector(BigIntVector bigIntVector) {
		this.bigIntVector = Preconditions.checkNotNull(bigIntVector, "BigIntVector is null");
	}

	@Override
	public long getLong(int i) {
		return bigIntVector.get(i);
	}

	@Override
	public boolean isNullAt(int i) {
		return bigIntVector.isNull(i);
	}
}
