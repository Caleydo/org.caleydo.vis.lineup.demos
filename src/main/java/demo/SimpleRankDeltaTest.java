/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 ******************************************************************************/
package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.lineup.data.AFloatFunction;
import org.caleydo.vis.lineup.data.FloatInferrers;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.ARow;
import org.caleydo.vis.lineup.model.FloatRankColumnModel;
import org.caleydo.vis.lineup.model.IRow;
import org.caleydo.vis.lineup.model.RankDeltaRankColumnModel;
import org.caleydo.vis.lineup.model.RankRankColumnModel;
import org.caleydo.vis.lineup.model.RankTableModel;
import org.caleydo.vis.lineup.model.mapping.PiecewiseMapping;

import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public class SimpleRankDeltaTest implements IModelBuilder {
	@Override
	public void apply(RankTableModel table) throws Exception {
		table.add(new RankRankColumnModel());
		final FloatRankColumnModel a = new FloatRankColumnModel(new AFloatFunction<IRow>() {
			@Override
			public float applyPrimitive(IRow in) {
				return ((SimpleRow) in).value;
			}
		}, GLRenderers.drawText("Float", VAlign.CENTER), new Color("#ffb380"), new Color("#ffe6d5"),
				new PiecewiseMapping(0, Float.NaN), FloatInferrers.MEAN);
		final FloatRankColumnModel b = new FloatRankColumnModel(new AFloatFunction<IRow>() {
			@Override
			public float applyPrimitive(IRow in) {
				return ((SimpleRow) in).value2;
			}
		}, GLRenderers.drawText("Float2", VAlign.CENTER), new Color("#ffb380"), new Color("#ffe6d5"),
				new PiecewiseMapping(0, Float.NaN), FloatInferrers.MEAN);
		RankDeltaRankColumnModel delta = new RankDeltaRankColumnModel();

		table.add(delta);
		delta.add(a);
		delta.add(b);

		Random r = new Random(200);
		List<IRow> rows = new ArrayList<>(100);
		for (int i = 0; i < 100; ++i) {
			final float v1 = Math.round(r.nextFloat() * 10) / 10.f;
			final float v2 = v1 + (r.nextBoolean() ? 0.02f : -0.02f);
			rows.add(new SimpleRow(v1, v2));
		}
		table.addData(rows);
	}

	@Override
	public Iterable<? extends ARankColumnModel> createAutoSnapshotColumns(RankTableModel table, ARankColumnModel model) {
		return Collections.singleton(new RankRankColumnModel());
	}

	static class SimpleRow extends ARow {

		private final float value;
		private final float value2;

		public SimpleRow(float v, float v2) {
			this.value = v;
			this.value2 = v2;
		}

	}

	public static void main(String[] args) {
		GLSandBox.main(args, RankTableDemo.class, "Rank Delta Score", new SimpleRankDeltaTest());
	}
}
