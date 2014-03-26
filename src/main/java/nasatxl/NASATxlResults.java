/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package nasatxl;

import static demo.RankTableDemo.toDouble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.lineup.data.DoubleInferrers;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.ARow;
import org.caleydo.vis.lineup.model.CategoricalRankColumnModel;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;

import demo.RankTableDemo;
import demo.RankTableDemo.IModelBuilder;
import demo.ReflectionData;
import demo.ReflectionDoubleData;

/**
 * @author Samuel Gratzl
 *
 */
public class NASATxlResults implements IModelBuilder {

	@Override
	public void apply(RankTableModel table) throws Exception {
		List<NASATxlTest> rows = readData();
		table.addData(rows);

		Map<Integer, String> taskMetaData = new LinkedHashMap<>();
		for (int i = 1; i <= 13; ++i)
			taskMetaData.put(i, String.format("Task %d", i));

		table.add(new CategoricalRankColumnModel<Integer>(GLRenderers.drawText("Task", VAlign.CENTER),
				new ReflectionData<>(field("task"), Integer.class), taskMetaData));

		ReflectionDoubleData data = new ReflectionDoubleData(field("time"));
		DoubleRankColumnModel f = new DoubleRankColumnModel(data, GLRenderers.drawText("Execution Time (s)",
				VAlign.CENTER), new Color("#FFD92F"), new Color("#FFFFCC"), new PiecewiseMapping(0, Float.NaN),
				DoubleInferrers.MEDIAN);
		f.setWidth(150);
		table.add(f);

		table.add(col(
				"mental_demand",
				"Mental Demand\nHow much mental and perceptual activity was required? Was the task easy or demanding, simple or complex?",
				"#FC9272", "#FEE0D2"));
		table.add(col(
				"physical_demand",
				"Physical Demand\nHow much physical activity was required? Was the task easy or demanding, slack or strenuous?",
				"#9ECAE1", "#DEEBF7"));
		table.add(col(
				"temporal_demand",
				"Temporal Demand\nHow much time pressure did you feel due to the pace at which the tasks or task elements occurred? Was the pace slow or rapid?",
				"#A1D99B", "#E5F5E0"));
		table.add(col(
				"performance",
				"Overall Performance\nHow successful were you in performing the task? How satisfied were you with your performance?",
				"#C994C7", "#E7E1EF"));
		table.add(col(
				"effort",
				"Effort\nHow hard did you have to work (mentally and physically) to accomplish your level of performance?",
				"#FDBB84", "#FEE8C8"));
		table.add(col(
				"frustration",
				"Frustration Level\nHow irritated, stresses, and annoyed versus content, relaxed, and complacent did you feel during the task?",
				"#DFC27D", "#F6E8C3"));
	}

	@Override
	public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model) {
		Collection<ARankColumnModel> ms = new ArrayList<>(2);
		ms.add(new RankRankColumnModel());
		ARankColumnModel desc = find(table, "Task");
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

	protected static List<NASATxlTest> readData() throws IOException {
		List<NASATxlTest> rows = new ArrayList<>();
		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				NASATxlResults.class.getResourceAsStream("nasaresult.csv"), Charset.forName("UTF-8")))) {
			String line;
			r.readLine();
			while ((line = r.readLine()) != null) {
				String[] l = line.split("\t");
				NASATxlTest row = new NASATxlTest();
				// row.rank = Integer.parseInt(l[0]);
				row.task = Integer.parseInt(l[0]);
				row.time = toDouble(l, 2);
				row.mental_demand = toDouble(l, 3);
				row.physical_demand = toDouble(l, 4);
				row.temporal_demand = toDouble(l, 5);
				row.performance = toDouble(l, 6);
				row.effort = toDouble(l, 7);
				row.frustration = toDouble(l, 8);
				rows.add(row);
			}
		}
		return rows;
	}

	private static Field field(String name) throws NoSuchFieldException, SecurityException {
		return NASATxlTest.class.getDeclaredField(name);
	}

	private DoubleRankColumnModel col(String field, String label, String color, String bgColor)
			throws NoSuchFieldException, SecurityException {
		ReflectionDoubleData data = new ReflectionDoubleData(field(field));
		DoubleRankColumnModel f = new DoubleRankColumnModel(data, GLRenderers.drawText(label, VAlign.CENTER),
 new Color(
				color), new Color(bgColor), mapping(), DoubleInferrers.MEDIAN);
		f.setWidth(150);
		return f;
	}

	protected PiecewiseMapping mapping() {
		PiecewiseMapping m = new PiecewiseMapping(0, 100);
		return m;
	}

	static class NASATxlTest extends ARow {
		public int task;
		public double mental_demand;
		public double physical_demand;
		public double temporal_demand;
		public double performance;
		public double effort;
		public double frustration;
		public double time;

		@Override
		public String toString() {
			return "Task " + task;
		}
	}

	public static void main(String[] args) {
		// dump();
		GLSandBox.main(args, RankTableDemo.class, "UserStudy NASATxlResults", new NASATxlResults());
	}
}
