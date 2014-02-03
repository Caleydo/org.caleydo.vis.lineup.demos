/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.wur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;

import demo.RankTableDemo;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class WorldUniversityRanking2012 extends AWorldUniversityRanking {
	/**
	 * @param spec
	 */
	public WorldUniversityRanking2012(RankTableSpec spec) {
		super(spec);
	}

	@Override
	public void apply(RankTableModel table) throws Exception {
		Map<String, String> countries = WorldUniversityYear.readCountries();

		Map<String, WorldUniversityYear[]> data = WorldUniversityYear.readData(2012);
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
		} else {
			parseSpec(table, countries.values());
		}
	}

	public static void main(String[] args) {
		// dump();
		GLSandBox.main(args, RankTableDemo.class, "world university ranking 2012,2011 and 2010",
				new WorldUniversityRanking2012(null));
	}
}
