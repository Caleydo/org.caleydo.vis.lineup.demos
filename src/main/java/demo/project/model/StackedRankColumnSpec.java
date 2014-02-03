/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.List;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel.Alignment;

import com.google.common.primitives.Floats;

/**
 * @author Samuel Gratzl
 *
 */
public class StackedRankColumnSpec extends AMultiRankRankColumnSpec {
	private Alignment alignment;
	private boolean compressed;
	private int singleAlignment;

	private List<Float> weights;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);

		StackedRankColumnModel m = (StackedRankColumnModel) model;
		m.setAlignment(singleAlignment);
		m.setAlignment(alignment);
		m.setCompressed(compressed);
		m.setWeights(Floats.toArray(weights));
	}

	@Override
	public void save(ARankColumnModel model) {
		StackedRankColumnModel m = (StackedRankColumnModel) model;
		this.alignment = m.getAlignment();
		this.compressed = m.isCompressed();
		this.singleAlignment = m.getSingleAlignment();
		weights = Floats.asList(m.getWeights());
		super.save(model);
	}
}
