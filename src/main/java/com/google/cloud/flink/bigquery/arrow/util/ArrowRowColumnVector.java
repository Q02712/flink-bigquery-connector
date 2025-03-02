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

import org.apache.arrow.vector.complex.StructVector;
import org.apache.flink.table.data.ColumnarRowData;
import org.apache.flink.table.data.vector.ColumnVector;
import org.apache.flink.table.data.vector.RowColumnVector;
import org.apache.flink.table.data.vector.VectorizedColumnBatch;
import org.apache.flink.util.Preconditions;

/** Arrow column vector for Row. */

public final class ArrowRowColumnVector implements RowColumnVector {

	/**
	 * Container which is used to store the sequence of row values of a column to
	 * read.
	 */
	private final StructVector structVector;

	private final ColumnVector[] fieldColumns;

	public ArrowRowColumnVector(StructVector structVector, ColumnVector[] fieldColumns) {
		this.structVector = Preconditions.checkNotNull(structVector, "StructVector is null");
		this.fieldColumns = Preconditions.checkNotNull(fieldColumns, "ColumnVector array is null");
	}

	@Override
	public ColumnarRowData getRow(int i) {
		VectorizedColumnBatch vectorizedColumnBatch = new VectorizedColumnBatch(fieldColumns);
		return new ColumnarRowData(vectorizedColumnBatch, i);
	}

	@Override
	public boolean isNullAt(int i) {
		return structVector.isNull(i);
	}
}
