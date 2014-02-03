/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.StringRankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StringRankColumnSpec extends AFilterableRankColumnSpec {
	private String filter;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		((StringRankColumnModel) model).setFilter(filter, isGlobalFilter, isRankIndependentFilter);
	}

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);
		StringRankColumnModel m = (StringRankColumnModel) model;
		this.filter = m.getFilter();
	}
}
