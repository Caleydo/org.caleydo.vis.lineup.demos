/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.wur;

import static university.wur.WorldUniversityYear.COL_QSSTARS;
import static university.wur.WorldUniversityYear.COL_academic;
import static university.wur.WorldUniversityYear.COL_citations;
import static university.wur.WorldUniversityYear.COL_employer;
import static university.wur.WorldUniversityYear.COL_faculty;
import static university.wur.WorldUniversityYear.COL_international;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;

import demo.RankTableDemo;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldUniversityRanking2013 extends AWorldUniversityRanking {

	/**
	 * @param spec
	 */
	public WorldUniversityRanking2013(RankTableSpec spec) {
		super(spec);
	}

	@Override
	public void apply(RankTableModel table) throws Exception {
		Map<String, String> countries = WorldUniversityYear.readCountries();

		Map<String, WorldUniversityYear[]> data = WorldUniversityYear.readData(2013);
		countries.keySet().retainAll(data.keySet());

		List<UniversityRow> rows = new ArrayList<>(data.size());
		for (Map.Entry<String, WorldUniversityYear[]> entry : data.entrySet()) {
			rows.add(new UniversityRow(entry.getKey(), entry.getValue(), countries.get(entry.getKey())));
		}
		table.addData(rows);
		data = null;

		if (tableSpec == null) {
			RankRankColumnModel rank = new RankRankColumnModel();
			rank.setWidth(40);
			table.add(rank);
			table.add(createSchoolName());
			table.add(createCountries(countries.values()));

			WorldUniversityYear.addYear(table, "World University Ranking", new YearGetter(0), true, false).orderByMe();

			// WorldUniversityYear.addSpecialYear(table, new YearGetter(0));
		} else
			parseSpec(table, countries.values());
	}


	public static void dump() throws IOException {
		Map<String, String> countries = WorldUniversityYear.readCountries();

		Map<String, WorldUniversityYear[]> data = WorldUniversityYear.readData(2012);
		countries.keySet().retainAll(data.keySet());
		final char SEP = '\t';
		try (PrintWriter w = new PrintWriter(new File("wur2013_summary.csv"), "UTF-8")) {
			w.append("School name").append(SEP).append("Country");

			w.append(SEP).append("Academic reputation");
			w.append(SEP).append("Employer reputation");
			w.append(SEP).append("Faculty/student ratio");
			w.append(SEP).append("Citations per faculty");
			w.append(SEP).append("International faculty ratio");
			w.append(SEP).append("International student ratio");
			w.append(SEP).append("QS Stars");
			w.println();

			for (Map.Entry<String, WorldUniversityYear[]> entry : data.entrySet()) {
				w.append(entry.getKey()).append(SEP).append(Objects.toString(countries.get(entry.getKey()), ""));

				for (WorldUniversityYear y : entry.getValue()) {
					w.append(SEP).append(toString(y.get(COL_academic)));
					w.append(SEP).append(toString(y.get(COL_employer)));
					w.append(SEP).append(toString(y.get(COL_faculty)));
					w.append(SEP).append(toString(y.get(COL_citations)));
					w.append(SEP).append(toString(y.get(COL_academic)));
					w.append(SEP).append(toString(y.get(COL_international)));
					w.append(SEP).append(toString(y.get(COL_QSSTARS)));
				}
				w.println();
			}
		}
	}

	private static CharSequence toString(double f) {
		if (Double.isNaN(f))
			return "";
		return Double.toString(f);
	}

	public static void main(String[] args) {
		// dump();
		GLSandBox.main(args, RankTableDemo.class, "world university ranking 2012,2011 and 2010",
				new WorldUniversityRanking2013(null));
	}
}
