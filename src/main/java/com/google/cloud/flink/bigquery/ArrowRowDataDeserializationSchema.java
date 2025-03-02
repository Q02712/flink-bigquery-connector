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
package com.google.cloud.flink.bigquery;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.util.Collector;

import com.google.cloud.flink.bigquery.arrow.util.ArrowSchemaConverter;
import com.google.cloud.flink.bigquery.arrow.util.ArrowToRowDataConverter;

public class ArrowRowDataDeserializationSchema implements DeserializationSchema<RowData>, Serializable {

	public static final long serialVersionUID = 1L;
	public TypeInformation<RowData> typeInfo;
	@SuppressWarnings("rawtypes")
	public DeserializationSchema<VectorSchemaRoot> nestedSchema;
	public ArrowToRowDataConverter runtimeConverter;
	List<GenericRowData> rowDataList;

	public static Schema arrowSchema;

	public ArrowRowDataDeserializationSchema(RowType rowType, TypeInformation<RowData> typeInfo) {
		this.typeInfo = typeInfo;
		arrowSchema = ArrowSchemaConverter.convertToSchema(rowType);
		this.nestedSchema = ArrowDeserializationSchema.forGeneric(arrowSchema, typeInfo);
		this.runtimeConverter = ArrowToRowDataConverter.createRowConverter(rowType);
	}

	@Override
	public TypeInformation<RowData> getProducedType() {
		return typeInfo;
	}

	@Override
	public void open(InitializationContext context) throws Exception {
		this.nestedSchema.open(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserialize(@Nullable byte[] message, Collector<RowData> out) throws IOException {
		if (message == null) {
			throw new FlinkBigQueryException("Deserializing message is empty");
		}
		try {
			VectorSchemaRoot root = nestedSchema.deserialize(message);
			List<GenericRowData> rowdatalist = (List<GenericRowData>) runtimeConverter.convert(root);
			for (int i = 0; i < rowdatalist.size(); i++) {
				out.collect(rowdatalist.get(i));
			}
			ArrowDeserializationSchema.close();
		} catch (Exception ex) {
			throw new FlinkBigQueryException("Error while deserializing Arrow type", ex);
		}
	}

	public static Schema getArrowSchema() {
		return arrowSchema;
	}

	@Override
	public RowData deserialize(@Nullable byte[] message) throws IOException {
		if (message == null) {
			return null;
		}
		RowData rowData;
		try {
			VectorSchemaRoot root = nestedSchema.deserialize(message);
			rowData = (RowData) runtimeConverter.convert(root);
		} catch (Exception ex) {
			throw new FlinkBigQueryException("Error while deserializing Arrow type", ex);
		}
		return rowData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ArrowRowDataDeserializationSchema that = (ArrowRowDataDeserializationSchema) o;
		return nestedSchema.equals(that.nestedSchema) && typeInfo.equals(that.typeInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nestedSchema, typeInfo);
	}

	@Override
	public boolean isEndOfStream(RowData nextElement) {
		return false;
	}
}
