package demo;
/*******************************************************************************
 * Caleydo - visualization for molecular biology - http://caleydo.org
 *
 * Copyright(C) 2005, 2012 Graz University of Technology, Marc Streit, Alexander
 * Lex, Christian Partl, Johannes Kepler University Linz </p>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 *******************************************************************************/


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.caleydo.core.util.color.Color;
import org.caleydo.core.view.opengl.layout.Column.VAlign;
import org.caleydo.core.view.opengl.layout2.GLSandBox;
import org.caleydo.core.view.opengl.layout2.renderer.GLRenderers;
import org.caleydo.vis.rank.data.AFloatFunction;
import org.caleydo.vis.rank.data.FloatInferrers;
import org.caleydo.vis.rank.model.ARankColumnModel;
import org.caleydo.vis.rank.model.ARow;
import org.caleydo.vis.rank.model.FloatRankColumnModel;
import org.caleydo.vis.rank.model.IRow;
import org.caleydo.vis.rank.model.RankRankColumnModel;
import org.caleydo.vis.rank.model.RankTableModel;
import org.caleydo.vis.rank.model.mapping.PiecewiseMapping;

import demo.RankTableDemo.IModelBuilder;

/**
 * @author Samuel Gratzl
 *
 */
public class SameScoreTest implements IModelBuilder {
	@Override
	public void apply(RankTableModel table) throws Exception {
		// table.add(new RankRankColumnModel());
		table.add(new FloatRankColumnModel(new AFloatFunction<IRow>() {
			@Override
			public float applyPrimitive(IRow in) {
				return ((SimpleRow) in).value;
			}
		}, GLRenderers.drawText("Float", VAlign.CENTER), new Color("#ffb380"), new Color("#ffe6d5"),
				new PiecewiseMapping(0, Float.NaN), FloatInferrers.MEAN));

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
