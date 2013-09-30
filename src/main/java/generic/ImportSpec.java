/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package generic;

import static org.caleydo.core.view.opengl.layout2.renderer.GLRenderers.drawText;
import generic.GenericRow.DateGetter;
import generic.GenericRow.DoubleGetter;
import generic.GenericRow.IntGetter;
import generic.GenericRow.StringGetter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.caleydo.core.io.MatrixDefinition;
import org.caleydo.core.util.color.Color;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.vis.lineup.data.DoubleInferrers;
import org.caleydo.vis.lineup.data.IDoubleInferrer;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.CategoricalRankColumnModel;
import org.caleydo.vis.lineup.model.DateRankColumnModel;
import org.caleydo.vis.lineup.model.DateRankColumnModel.DateMode;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.IntegerRankColumnModel;
import org.caleydo.vis.lineup.model.StringRankColumnModel;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;

import demo.RankTableDemo;

/**
 * @author Samuel Gratzl
 *
 */
public class ImportSpec extends MatrixDefinition {
	private static final Logger log = Logger.create(ImportSpec.class);

	private List<ColumnSpec> columns = new ArrayList<>();
	private String label;

	/**
	 *
	 */
	public ImportSpec() {
		setColumnOfRowIds(-1);
	}

	/**
	 * @param label
	 *            setter, see {@link label}
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the label, see {@link #label}
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the columns, see {@link #columns}
	 */
	public List<ColumnSpec> getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            setter, see {@link columns}
	 */
	public void setColumns(List<ColumnSpec> columns) {
		this.columns = columns;
	}

	public static abstract class ColumnSpec {
		protected int col;
		protected Color color = Color.LIGHT_GRAY;
		protected Color bgColor = new Color(0.95f, .95f, .95f);

		/**
		 * @param col
		 *            setter, see {@link col}
		 */
		public ColumnSpec setCol(int col) {
			this.col = col;
			return this;
		}

		/**
		 * @param color
		 *            setter, see {@link color}
		 */
		public void setColor(Color color) {
			this.color = color;
		}

		/**
		 * @param bgColor
		 *            setter, see {@link bgColor}
		 */
		public void setBgColor(Color bgColor) {
			this.bgColor = bgColor;
		}

		public abstract Object parse(String[] vals);
		public abstract ARankColumnModel create(String[] headers);
	}

	public static class DoubleColumnSpec extends ColumnSpec {
		protected PiecewiseMapping mapping = new PiecewiseMapping(Double.NaN, Double.NaN);
		protected IDoubleInferrer inferer = DoubleInferrers.fix(Double.NaN);

		/**
		 * @param mapping
		 *            setter, see {@link mapping}
		 */
		public void setMapping(PiecewiseMapping mapping) {
			this.mapping = mapping;
		}

		/**
		 * @param inferer
		 *            setter, see {@link inferer}
		 */
		public void setInferer(IDoubleInferrer inferer) {
			this.inferer = inferer;
		}

		@Override
		public Object parse(String[] vals) {
			return RankTableDemo.toDouble(vals, col);
		}

		@Override
		public ARankColumnModel create(String[] headers) {
			return new DoubleRankColumnModel(new DoubleGetter(col), drawText(headers[col], VAlign.CENTER), color,
					bgColor, mapping, inferer);
		}
	}

	public static class DateColumnSpec extends ColumnSpec {
		private SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		private DateMode mode = DateMode.DATE_TIME;

		public String getPattern() {
			return parser.toPattern();
		}

		public void setPattern(String pattern) {
			parser.applyPattern(pattern);
		}
		/**
		 * @param mode
		 *            setter, see {@link mode}
		 */
		public void setMode(DateMode mode) {
			this.mode = mode;
		}

		/**
		 * @return the mode, see {@link #mode}
		 */
		public DateMode getMode() {
			return mode;
		}

		@Override
		public Object parse(String[] vals) {
			String value = vals[col];
			if (StringUtils.isBlank(value))
				return null;
			try {
				return parser.parse(value);
			} catch (ParseException e) {
				log.error("can't parse: " + value, e);
				return null;
			}
		}

		@Override
		public ARankColumnModel create(String[] headers) {
			return new DateRankColumnModel(drawText(headers[col], VAlign.CENTER), new DateGetter(col), color, bgColor,
					mode);
		}
	}

	public static class IntegerColumnSpec extends ColumnSpec {

		@Override
		public Object parse(String[] vals) {
			String v = vals[col].trim();
			if (v.isEmpty() || "-".equals(v))
				return -1;
			return new Integer(v);
		}

		@Override
		public ARankColumnModel create(String[] headers) {
			return new IntegerRankColumnModel(drawText(headers[col], VAlign.CENTER), new IntGetter(col), color,
					bgColor, NumberFormat.getInstance(Locale.ENGLISH));
		}
	}

	public static class StringColumnSpec extends ColumnSpec {

		/**
		 * @param col
		 */
		public StringColumnSpec(int col) {
			setCol(col);
		}

		@Override
		public Object parse(String[] vals) {
			return vals[col];
		}

		@Override
		public ARankColumnModel create(String[] headers) {
			return new StringRankColumnModel(drawText(headers[col], VAlign.CENTER), new StringGetter(col), color,
					bgColor);
		}
	}

	public static class CategoricalColumnSpec extends ColumnSpec {
		private Set<String> categories;

		/**
		 * @param set
		 */
		public CategoricalColumnSpec(Set<String> set) {
			this.categories = set;
		}


		@Override
		public Object parse(String[] vals) {
			return vals[col];
		}

		@Override
		public ARankColumnModel create(String[] headers) {
			Map<String, String> map = new TreeMap<>();
			for (String s : categories)
				map.put(s, s);
			return new CategoricalRankColumnModel<String>(drawText(headers[col], VAlign.CENTER), new StringGetter(col),
					map, color, bgColor, "");
		}
	}
}
