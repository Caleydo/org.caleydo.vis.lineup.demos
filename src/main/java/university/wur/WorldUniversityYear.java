/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.wur;

import static demo.RankTableDemo.toDouble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.lineup.data.ADoubleFunction;
import org.caleydo.vis.lineup.data.DoubleInferrers;
import org.caleydo.vis.lineup.data.IDoubleSetterFunction;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;
import org.caleydo.vis.lineup.model.StarsRankColumnModel;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;
import org.caleydo.vis.lineup.model.mixin.IDataBasedColumnMixin;

import com.google.common.base.Function;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldUniversityYear {
	public static final int COL_QSSTARS = 0;
	public static final int COL_overall = 1;
	public static final int COL_academic = 2;
	public static final int COL_employer = 3;
	public static final int COL_faculty = 4;
	public static final int COL_international = 5;
	public static final int COL_internationalstudents = 6;
	public static final int COL_citations = 7;
	public static final int COL_arts = 8;
	public static final int COL_engineering = 9;
	public static final int COL_life = 10;
	public static final int COL_natural = 11;
	public static final int COL_social = 12;

	private double qsstars;
	private double overall;
	private double academic;
	private double employer;
	private double faculty;
	private double international;
	private double internationalstudents;
	private double citations;
	private double arts;
	private double engineering;
	private double life;
	private double natural;
	private double social;

	public WorldUniversityYear(String[] l) {
		qsstars = toDouble(l, 2);
		overall = toDouble(l, 3);
		academic = toDouble(l, 4);
		employer = toDouble(l, 5);
		faculty = toDouble(l, 6);
		international = toDouble(l, 7);
		internationalstudents = toDouble(l, 8);
		citations = toDouble(l, 9);
		arts = toDouble(l, 10);
		engineering = toDouble(l, 11);
		life = toDouble(l, 12);
		natural = toDouble(l, 13);
		social = toDouble(l, 14);
	}

	public double get(int index) {
		switch (index) {
		case COL_academic:
			return academic;
		case COL_arts:
			return arts;
		case COL_citations:
			return citations;
		case COL_employer:
			return employer;
		case COL_engineering:
			return engineering;
		case COL_faculty:
			return faculty;
		case COL_international:
			return international;
		case COL_internationalstudents:
			return internationalstudents;
		case COL_life:
			return life;
		case COL_natural:
			return natural;
		case COL_overall:
			return overall;
		case COL_QSSTARS:
			return qsstars;
		case COL_social:
			return social;
		}
		return Float.NaN;
	}

	public void set(int index, double value) {
		switch (index) {
		case COL_academic:
			academic = value;
			break;
		case COL_arts:
			arts = value;
			break;
		case COL_citations:
			citations = value;
			break;
		case COL_employer:
			employer = value;
			break;
		case COL_engineering:
			engineering = value;
			break;
		case COL_faculty:
			faculty = value;
			break;
		case COL_international:
			international = value;
			break;
		case COL_internationalstudents:
			internationalstudents = value;
			break;
		case COL_life:
			life = value;
			break;
		case COL_natural:
			natural = value;
			break;
		case COL_overall:
			overall = value;
			break;
		case COL_QSSTARS:
			qsstars = value;
			break;
		case COL_social:
			social = value;
			break;
		}
	}

	public static StackedRankColumnModel addYear(RankTableModel table, String title,
			Function<IRow, WorldUniversityYear> year, boolean addStars, boolean asIs) {

		// * Academic reputation (40%)
		// * Employer reputation (10%)
		// * Faculty/student ratio (20%)
		// * Citations per faculty (20%)
		// * International faculty ratio (5%)
		// * International student ratio (5%)
		// Color[] light = StephenFewColorPalette.getAsAWT(EBrightness.LIGHT);
		// Color[] dark = StephenFewColorPalette.getAsAWT(EBrightness.MEDIUM);
		// stacked.add(col(year, COL_academic, "Academic reputation", dark[1], light[1]));
		// stacked.add(col(year, COL_employer, "Employer reputation", dark[2], light[2]));
		// stacked.add(col(year, COL_faculty, "Faculty/student ratio", dark[3], light[3]));
		// stacked.add(col(year, COL_citations, "Citations per faculty", dark[4], light[4]));
		// stacked.add(col(year, COL_international, "International faculty ratio", dark[5], light[5]));
		// stacked.add(col(year, COL_internationalstudents, "International student ratio", dark[6], light[6]));
		List<ARankColumnModel> cols = new ArrayList<>();
		cols.add(createfor(COL_academic, year));
		cols.add(createfor(COL_employer, year));
		cols.add(createfor(COL_faculty, year));
		cols.add(createfor(COL_citations, year));
		cols.add(createfor(COL_international, year));
		cols.add(createfor(COL_internationalstudents, year));

		final StackedRankColumnModel stacked;
		if (asIs) {
			stacked = null;
			for (ARankColumnModel col : cols)
				table.add(col);
		} else {
			stacked = new StackedRankColumnModel();
			stacked.setTitle(title);
			table.add(stacked);
			for (ARankColumnModel col : cols)
				stacked.add(col);

			stacked.setWeights(new float[] { 40, 10, 20, 20, 5, 5 });
			stacked.setWidth(380);
		}
		if (addStars) {
			table.add(createfor(COL_QSSTARS, year));
		}
		return stacked;
	}

	/**
	 * @param col
	 * @param substring
	 * @param yearGetter
	 * @return
	 */
	public static ARankColumnModel createfor(int subindex, Function<IRow, WorldUniversityYear> year) {
		switch (subindex) {
		case COL_academic:
			return col(year, COL_academic, "Academic reputation", "#FC9272", "#FEE0D2");
		case COL_arts:
			return col(year, COL_arts, "Arts & Humanities", "#FFD92F", "#FFFFCC");
		case COL_citations:
			return col(year, COL_citations, "Citations per faculty", "#C994C7", "#E7E1EF");
		case COL_employer:
			return col(year, COL_employer, "Employer reputation", "#9ECAE1", "#DEEBF7");
		case COL_engineering:
			return col(year, COL_engineering, "Engineering & Technology", "#8DA0CB", "#ECE2F0");
		case COL_faculty:
			return col(year, COL_faculty, "Faculty/student ratio", "#A1D99B", "#E5F5E0");
		case COL_international:
			return col(year, COL_international, "International faculty ratio", "#FDBB84", "#FEE8C8");
		case COL_internationalstudents:
			return col(year, COL_internationalstudents, "International student ratio", "#DFC27D", "#F6E8C3");
		case COL_life:
			return col(year, COL_life, "Life Sciences & Medicine", "#E78AC3", "#FDE0DD");
		case COL_natural:
			return col(year, COL_natural, "Natural Sciences", "#A6D854", "#F7FCB9");
		case COL_overall:
			return null;
		case COL_QSSTARS:
			return new StarsRankColumnModel(new ValueGetter(year, COL_QSSTARS), GLRenderers.drawText("QS Stars",
					VAlign.CENTER), new Color("#FECC5C"), new Color("#FFFFB2"), 6);
		case COL_social:
			return null;
		}
		return null;
	}

	public static void addSpecialYear(RankTableModel table, Function<IRow, WorldUniversityYear> year) {

		ARankColumnModel c;
		c = createfor(COL_arts, year);
		table.add(c);
		c.hide();
		c = createfor(COL_engineering, year);
		table.add(c);
		c.hide();
		c = createfor(COL_life, year);
		table.add(c);
		c.hide();
		c = createfor(COL_natural, year);
		table.add(c);
		c.hide();
	}

	private static DoubleRankColumnModel col(Function<IRow, WorldUniversityYear> year, int col, String text,
			String color, String bgColor) {
		return col(year, col, text, new Color(color), new Color(bgColor));
	}

	private static DoubleRankColumnModel col(Function<IRow, WorldUniversityYear> year, int col, String text,
			Color color, Color bgColor) {
		return new DoubleRankColumnModel(new ValueGetter(year, col), GLRenderers.drawText(text, VAlign.CENTER),
 color,
				bgColor, percentage(), DoubleInferrers.MEDIAN);
	}

	protected static PiecewiseMapping percentage() {
		return new PiecewiseMapping(0, 100);
	}

	public static Map<String, WorldUniversityYear[]> readData(int... years) throws IOException {
		Map<String, WorldUniversityYear[]> data = new LinkedHashMap<>();
		for (int i = 0; i < years.length; ++i) {
			String year = String.format("wur%4d.txt", years[i]);
			try (BufferedReader r = new BufferedReader(new InputStreamReader(
					WorldUniversityYear.class.getResourceAsStream(year), Charset.forName("UTF-8")))) {
				String line;
				r.readLine(); // header
				while ((line = r.readLine()) != null) {
					String[] l = line.split("\t");
					String school = l[1].trim();

					WorldUniversityYear universityYear = new WorldUniversityYear(l);
					if (!data.containsKey(school)) {
						data.put(school, new WorldUniversityYear[years.length]);
					}
					data.get(school)[i] = universityYear;
				}
			}
		}
		return data;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> readCountries() throws IOException {
		Map<String, String> result = new TreeMap<>();

		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				WorldUniversityYear.class.getResourceAsStream("countries.txt"), Charset.forName("UTF-8")))) {
			String line;
			r.readLine();
			while ((line = r.readLine()) != null) {
				String[] l = line.split(";");
				String school = l[0].trim();
				String country = l[1].trim();

				result.put(school, country);
			}
		}
		return result;
	}

	static class ValueGetter extends ADoubleFunction<IRow> implements IDoubleSetterFunction<IRow> {
		private final Function<IRow, WorldUniversityYear> year;
		private final int subindex;

		public ValueGetter(Function<IRow, WorldUniversityYear> year, int column) {
			this.year = year;
			this.subindex = column;
		}

		@Override
		public double applyPrimitive(IRow in) {
			WorldUniversityYear y = year.apply(in);
			if (y == null)
				return Double.NaN;
			return y.get(subindex);
		}

		@Override
		public void set(IRow in, double value) {
			WorldUniversityYear y = year.apply(in);
			if (y == null)
				return;
			y.set(subindex, value);
		}
	}

	/**
	 * @param input
	 * @param i
	 * @return
	 */
	public static String apply(IDataBasedColumnMixin input) {
		Function<IRow, ?> f = input.getData();
		if (f instanceof ValueGetter) {
			final ValueGetter value = (ValueGetter) f;
			return value.year.toString() + " " + value.subindex;
		}
		return null;
	}
}
