/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import java.util.Date;
import java.util.Objects;

import org.caleydo.vis.lineup.data.ADoubleFunction;
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

	public double getDouble(int index) {
		Object r = get(index);
		if (r instanceof Double)
			return ((Double) r).doubleValue();
		return Double.NaN;
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

	public Date getDate(int index) {
		Object r = get(index);
		if (r instanceof Date)
			return ((Date) r);
		return null;
	}

	public String getString(int index) {
		return Objects.toString(get(index), "");
	}

	public static interface IndexedGetter {
		int getIndex();
	}

	public static class DoubleGetter extends ADoubleFunction<IRow> implements IndexedGetter {
		private final int index;

		public DoubleGetter(int index) {
			this.index = index;
		}

		/**
		 * @return the index, see {@link #index}
		 */
		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public double applyPrimitive(IRow in) {
			return ((GenericRow) in).getDouble(index);
		}
	}

	public static class StringGetter implements Function<IRow, String>, IndexedGetter {
		private final int index;

		public StringGetter(int index) {
			this.index = index;
		}

		/**
		 * @return the index, see {@link #index}
		 */
		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public String apply(IRow in) {
			return ((GenericRow) in).getString(index);
		}
	}

	public static class IntGetter implements Function<IRow, Integer>, IndexedGetter {
		private final int index;

		public IntGetter(int index) {
			this.index = index;
		}

		/**
		 * @return the index, see {@link #index}
		 */
		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public Integer apply(IRow in) {
			return ((GenericRow) in).getInt(index);
		}
	}

	public static class DateGetter implements Function<IRow, Date>, IndexedGetter {
		private final int index;

		public DateGetter(int index) {
			this.index = index;
		}

		/**
		 * @return the index, see {@link #index}
		 */
		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public Date apply(IRow in) {
			return ((GenericRow) in).getDate(index);
		}
	}
}
