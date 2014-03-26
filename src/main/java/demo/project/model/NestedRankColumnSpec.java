/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.NestedRankColumnModel;


/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NestedRankColumnSpec extends AMultiRankRankColumnSpec {
	private boolean compressed;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);

		NestedRankColumnModel m = (NestedRankColumnModel) model;
		m.setCompressed(compressed);

	}

	@Override
	public void save(ARankColumnModel model) {
		NestedRankColumnModel m = (NestedRankColumnModel) model;
		this.compressed = m.isCompressed();
		super.save(model);
	}
}
