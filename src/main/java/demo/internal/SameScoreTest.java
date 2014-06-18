/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.lineup.data.ADoubleFunction;
import org.caleydo.vis.lineup.data.DoubleInferrers;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.ARow;
import org.caleydo.vis.lineup.model.DoubleRankColumnModel;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;

import demo.IModelBuilder;
import demo.RankTableDemo;

/**
 * @author Samuel Gratzl
 *
 */
public class SameScoreTest implements IModelBuilder {
	@Override
	public void apply(RankTableModel table) throws Exception {
		// table.add(new RankRankColumnModel());
		table.add(new DoubleRankColumnModel(new ADoubleFunction<IRow>() {
			@Override
			public double applyPrimitive(IRow in) {
				return ((SimpleRow) in).value;
			}
		}, GLRenderers.drawText("Float", VAlign.CENTER), new Color("#ffb380"), new Color("#ffe6d5"),
				new PiecewiseMapping(0, Float.NaN), DoubleInferrers.MEAN));

		Random r = new Random(200);
		List<IRow> rows = new ArrayList<>(100);
		for (int i = 0; i < 100; ++i)
			rows.add(new SimpleRow(Math.round(r.nextFloat() * 10) / 10.f));
		table.addData(rows);
	}

	@Override
	public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model) {
		return Collections.singleton(new RankRankColumnModel());
	}

	static class SimpleRow extends ARow {

		private final float value;
		public SimpleRow(float v) {
			this.value = v;
		}

	}

	public static void main(String[] args) {
		GLSandBox.main(args, RankTableDemo.class, "Same Score", new SameScoreTest());
	}
}
