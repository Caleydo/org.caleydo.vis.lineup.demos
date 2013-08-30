/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import java.util.Objects;

import org.caleydo.vis.lineup.data.AFloatFunction;
import org.caleydo.vis.lineup.model.ARow;
import org.caleydo.vis.lineup.model.IRow;

import com.google.common.base.Function;

/**
 * @author Samuel Gratzl
 *
 */
public class GenericRow extends ARow {
	private final Object[] data;

	/**
	 *
	 */
	public GenericRow(Object[] data) {
		this.data = data;
	}

	public float getFloat(int index) {
		Object r = get(index);
		if (r instanceof Float)
			return ((Float) r).floatValue();
		return Float.NaN;
	}

	public Object get(int index) {
		if (index < 0 || index >= data.length)
			return null;
		return data[index];
	}

	public int getInt(int index) {
		Object r = get(index);
		if (r instanceof Integer)
			return ((Integer) r).intValue();
		return -1;
	}

	public String getString(int index) {
		return Objects.toString(get(index), "");
	}

	public static class FloatGetter extends AFloatFunction<IRow> {
		private final int index;

		public FloatGetter(int index) {
			this.index = index;
		}

		@Override
		public float applyPrimitive(IRow in) {
			return ((GenericRow) in).getFloat(index);
		}
	}

	public static class StringGetter implements Function<IRow, String> {
		private final int index;

		public StringGetter(int index) {
			this.index = index;
		}

		@Override
		public String apply(IRow in) {
			return ((GenericRow) in).getString(index);
		}
	}

	public static class IntGetter implements Function<IRow, Integer> {
		private final int index;

		public IntGetter(int index) {
			this.index = index;
		}

		@Override
		public Integer apply(IRow in) {
			return ((GenericRow) in).getInt(index);
		}
	}
}
