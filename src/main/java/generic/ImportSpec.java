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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.caleydo.core.io.MatrixDefinition;
import org.caleydo.core.util.color.Color;
import org.caleydo.core.util.logging.Logger;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
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
@XmlRootElement
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

	@XmlSeeAlso({ IntegerColumnSpec.class, StringColumnSpec.class, DoubleColumnSpec.class, DateColumnSpec.class,
			CategoricalColumnSpec.class })
	public static abstract class ColumnSpec {
		protected int col;
		protected Color color = Color.LIGHT_GRAY;
		protected Color bgColor = new Color(0.95f, .95f, .95f);

		/**
		 * @param col
		 *            setter, see {@link col}
		 */
		public void setCol(int col) {
			this.col = col;
		}

		public ColumnSpec useCol(int col) {
			setCol(col);
			return this;
		}

		/**
		 * @return the col, see {@link #col}
		 */
		@XmlAttribute
		public int getCol() {
			return col;
		}

		@XmlAttribute
		public String getColor() {
			return color.getHEX();
		}

		public void setColor(String color) {
			this.color = new Color(color);
		}

		@XmlAttribute
		public String getBGColor() {
			return bgColor.getHEX();
		}

		public void setBGColor(String color) {
			this.bgColor = new Color(color);
		}

		public abstract Object parse(String[] vals);
		public abstract ARankColumnModel create(String[] headers);
	}

	public static class DoubleColumnSpec extends ColumnSpec {
		@XmlAttribute
		protected double mappingMin = Double.NaN;
		@XmlAttribute
		protected double mappingMax = Double.NaN;
		@XmlAttribute
		protected EInferer inferer = EInferer.NaN;

		@Override
		public Object parse(String[] vals) {
			return RankTableDemo.toDouble(vals, col);
		}

		@Override
		public ARankColumnModel create(String[] headers) {
			PiecewiseMapping mapping = new PiecewiseMapping(mappingMin, mappingMax);
			return new DoubleRankColumnModel(new DoubleGetter(col), drawText(headers[col], VAlign.CENTER), color,
					bgColor, mapping, inferer.toInferer());
		}
	}

	public static class DateColumnSpec extends ColumnSpec {
		@XmlTransient
		private final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		private DateMode mode = DateMode.DATE_TIME;

		@XmlAttribute
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
		@XmlAttribute
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

		public StringColumnSpec() {
		}
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
		@XmlElement
		private Set<String> categories = new HashSet<>();

		public CategoricalColumnSpec() {
		}
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
