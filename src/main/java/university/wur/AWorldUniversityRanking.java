/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package university.wur;

import java.util.ArrayList;
import java.util.Collection;

import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.lineup.model.ACompositeRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.ARow;
import org.caleydo.vis.lineup.model.CategoricalRankColumnModel;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.OrderColumn;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;
import org.caleydo.vis.lineup.model.StringRankColumnModel;
import org.caleydo.vis.lineup.model.mixin.IDataBasedColumnMixin;

import com.google.common.base.Function;

import demo.RankTableDemo.IModelBuilder;
import demo.ReflectionData;
import demo.project.model.ACompositeColumnSpec;
import demo.project.model.ARankColumnSpec;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public abstract class AWorldUniversityRanking implements IModelBuilder {
	protected final RankTableSpec tableSpec;

	public AWorldUniversityRanking(RankTableSpec spec) {
		this.tableSpec = spec;
	}

	protected void parseSpec(RankTableModel table, Collection<String> collection) throws NoSuchFieldException, SecurityException {
		for (ARankColumnSpec col : tableSpec.getColumn()) {
			ARankColumnModel c = createFor(col, collection);
			if (c == null)
				continue;
			table.add(c);
			if (col instanceof ACompositeColumnSpec) {
				for (ARankColumnSpec child : ((ACompositeColumnSpec) col))
					((ACompositeRankColumnModel) c).add(createFor(child, collection));
			}
			col.load(c);
		}
		for (ARankColumnSpec col : tableSpec.getPool()) {
			ARankColumnModel c = createFor(col, collection);
			if (c == null)
				continue;
			table.add(c);
			if (col instanceof ACompositeColumnSpec) {
				for (ARankColumnSpec child : ((ACompositeColumnSpec) col))
					((ACompositeRankColumnModel) c).add(createFor(child, collection));
			}
			col.load(c);
			c.hide();
		}
	}

	/**
	 * @param col
	 * @param collection
	 * @param headers
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	private ARankColumnModel createFor(ARankColumnSpec col, Collection<String> collection) throws NoSuchFieldException,
			SecurityException {
		ARankColumnModel c = RankTableSpec.create(col);
		if (c != null)
			return c;
		String d = col.getData();
		switch (d) {
		case "countries":
			return createCountries(collection);
		case "name":
			return createSchoolName();
		default:
			int year = Integer.parseInt(d.substring(0, 1));
			return WorldUniversityYear.createfor(Integer.parseInt(d.substring(2)), new YearGetter(year));
		}
	}

	protected static ARankColumnModel createSchoolName() {
		final ARankColumnModel label = new StringRankColumnModel(GLRenderers.drawText("School Name", VAlign.CENTER),
				StringRankColumnModel.DEFAULT).setWidth(300);
		return label;
	}

	protected CategoricalRankColumnModel<String> createCountries(Collection<String> collection)
			throws NoSuchFieldException, SecurityException {
		return CategoricalRankColumnModel.createSimple(GLRenderers.drawText("Country", VAlign.CENTER),
				new ReflectionData<>(UniversityRow.class.getDeclaredField("country"), String.class), collection);
	}

	public static Function<IDataBasedColumnMixin, String> DATA_CREATOR = new Function<IDataBasedColumnMixin, String>() {
		@Override
		public String apply(IDataBasedColumnMixin input) {
			if (input instanceof StringRankColumnModel)
				return "name";
			if (input instanceof CategoricalRankColumnModel<?>)
				return "country";
			return WorldUniversityYear.apply(input);
		}
	};

	protected static StackedRankColumnModel addYear(int rankColWidth, RankTableModel table,
			String title, YearGetter year) {
		table.add(new OrderColumn());
		table.add(new RankRankColumnModel().setWidth(rankColWidth));
		table.add(createSchoolName().setCollapsed(true));
		StackedRankColumnModel model = WorldUniversityYear.addYear(table, title, year, false, false);
		model.orderByMe();
		return model;
	}

	@Override
	public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model) {
		Collection<ARankColumnModel> ms = new ArrayList<>(2);
		ms.add(new RankRankColumnModel());
		ARankColumnModel desc = find(table, "School Name");
		if (desc != null)
			ms.add(desc.clone().setCollapsed(true));
		return ms;
	}

	private static ARankColumnModel find(RankTableModel table, String name) {
		for (ARankColumnModel model : table.getColumns()) {
			if (model.getLabel().equals(name))
				return model;
		}
		return null;
	}

	static class YearGetter implements Function<IRow, WorldUniversityYear> {
		private final int year;

		public YearGetter(int year) {
			this.year = year;
		}

		@Override
		public WorldUniversityYear apply(IRow in) {
			UniversityRow r = (UniversityRow) in;
			return r.years[year];
		}

		@Override
		public String toString() {
			return "" + year;
		}
	}

	static class UniversityRow extends ARow {
		public String schoolname;
		private String country;

		public WorldUniversityYear[] years;

		/**
		 * @param school
		 * @param country
		 * @param size
		 */
		public UniversityRow(String school, WorldUniversityYear[] years, String country) {
			this.schoolname = school;
			this.years = years;
			this.country = country;
		}

		/**
		 * @return the country, see {@link #country}
		 */
		public String getCountry() {
			return country;
		}

		@Override
		public String toString() {
			return schoolname;
		}
	}
}
