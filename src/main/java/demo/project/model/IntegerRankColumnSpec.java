/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.IntegerRankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerRankColumnSpec extends AFilterableRankColumnSpec {
	private int min;
	private int max;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		final IntegerRankColumnModel m = (IntegerRankColumnModel) model;
		m.setFilter(min, max);
		m.setIsRankIndependentFilter(isRankIndependentFilter);
		m.setGlobalFilter(isGlobalFilter);
	}

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);
		final IntegerRankColumnModel m = (IntegerRankColumnModel) model;
		this.min = m.getMin();
		this.max = m.getMax();
	}
}
