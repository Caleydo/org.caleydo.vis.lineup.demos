/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.usnews;

import static demo.RankTableDemo.toDouble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.lineup.data.ADoubleFunction;
import org.caleydo.vis.lineup.data.DoubleInferrers;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;

import com.google.common.base.Function;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldBestUniversitiesYear {
	public static final int COL_overall = 1;
	public static final int COL_academic = 2;
	public static final int COL_employer = 3;
	public static final int COL_faculty = 4;
	public static final int COL_international = 5;
	public static final int COL_internationalstudents = 6;
	public static final int COL_citations = 7;

	private final double overall;
	private final double academic;
	private final double employer;
	private final double faculty;
	private final double international;
	private final double internationalstudents;
	private final double citations;

	public WorldBestUniversitiesYear(String[] l) {
		// Rank;School;Country;Overall Score;Academic Reputation Score;Employer Reputation Score;Faculty-Student Ratio
		// Score;International Faculty Score;International Students Score;Citations per Faculty Score
		overall = toDouble(l, 3);
		academic = toDouble(l, 4);
		employer = toDouble(l, 5);
		faculty = toDouble(l, 6);
		international = toDouble(l, 7);
		internationalstudents = toDouble(l, 8);
		citations = toDouble(l, 9);
	}

	public double get(int index) {
		switch (index) {
		case COL_academic:
			return academic;
		case COL_citations:
			return citations;
		case COL_employer:
			return employer;
		case COL_faculty:
			return faculty;
		case COL_international:
			return international;
		case COL_internationalstudents:
			return internationalstudents;
		case COL_overall:
			return overall;
		}
		return 0;
	}

	public static void addYear(RankTableModel table, String title, Function<IRow, WorldBestUniversitiesYear> year) {
		final StackedRankColumnModel stacked = new StackedRankColumnModel();
		stacked.setTitle(title);
		table.add(stacked);
		// 40% ACADEMIC REPUTATION from global survey
		// 10% EMPLOYER REPUTATION from global survey
		// 20% CITATIONS PER FACULTY from SciVerse Scopus
		// 20% FACULTY STUDENT Ratio
		// 5% Proportion of INTERNATIONAL STUDENTS
		// 5% Proportion of INTERNATIONAL FACULTY
		stacked.add(col(year, COL_academic, "Academic reputation", "#FC9272", "#FEE0D2"));
		stacked.add(col(year, COL_employer, "Employer reputation", "#9ECAE1", "#DEEBF7"));
		stacked.add(col(year, COL_citations, "Citations per faculty", "#C994C7", "#E7E1EF"));
		stacked.add(col(year, COL_faculty, "Faculty/student ratio", "#A1D99B", "#E5F5E0"));
		stacked.add(col(year, COL_international, "International faculty ratio", "#FDBB84", "#FEE8C8"));
		stacked.add(col(year, COL_internationalstudents, "International student ratio", "#DFC27D", "#F6E8C3"));

		stacked.setWeights(new float[] { 40, 10, 20, 20, 5, 5 });
		stacked.setWidth(300);
	}

	private static DoubleRankColumnModel col(Function<IRow, WorldBestUniversitiesYear> year, int col, String text,
			String color, String bgColor) {
		return new DoubleRankColumnModel(new ValueGetter(year, col), GLRenderers.drawText(text, VAlign.CENTER),
				new Color(color), new Color(bgColor), percentage(), DoubleInferrers.MEDIAN);
	}

	protected static PiecewiseMapping percentage() {
		return new PiecewiseMapping(0, 100);
	}

	public static Map<String, WorldBestUniversitiesYear[]> readData(int... years) throws IOException {
		Map<String, WorldBestUniversitiesYear[]> data = new LinkedHashMap<>();
		for (int i = 0; i < years.length; ++i) {
			String year = String.format("usnews%4d.txt", years[i]);
			try (BufferedReader r = new BufferedReader(new InputStreamReader(
					WorldBestUniversitiesYear.class.getResourceAsStream(year), Charset.forName("UTF-8")))) {
				String line;
				r.readLine(); // header
				while ((line = r.readLine()) != null) {
					if (line.isEmpty())
						continue;
					String[] l = line.split(";");
					String school = l[1];

					WorldBestUniversitiesYear universityYear = new WorldBestUniversitiesYear(l);
					if (!data.containsKey(school)) {
						data.put(school, new WorldBestUniversitiesYear[years.length]);
					}
					data.get(school)[i] = universityYear;
				}
			}
		}
		return data;
	}

	static class ValueGetter extends ADoubleFunction<IRow> {
		private final Function<IRow, WorldBestUniversitiesYear> year;
		private final int subindex;

		public ValueGetter(Function<IRow, WorldBestUniversitiesYear> year, int column) {
			this.year = year;
			this.subindex = column;
		}

		@Override
		public double applyPrimitive(IRow in) {
			WorldBestUniversitiesYear y = year.apply(in);
			if (y == null)
				return Double.NaN;
			return y.get(subindex);
		}
	}
}
