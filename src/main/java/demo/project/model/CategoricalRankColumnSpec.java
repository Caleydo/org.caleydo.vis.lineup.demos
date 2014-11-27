/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.CategoricalRankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoricalRankColumnSpec extends AFilterableRankColumnSpec {
	private Set<String> selection;
	private boolean filterNA = false;

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		@SuppressWarnings("unchecked")
		CategoricalRankColumnModel<String> m = (CategoricalRankColumnModel<String>) model;
		m.setFilter(selection, filterNA, isGlobalFilter, isRankIndependentFilter);
	}

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);
		@SuppressWarnings("unchecked")
		CategoricalRankColumnModel<String> m = (CategoricalRankColumnModel<String>) model;
		this.filterNA = m.isFilterNA();
		this.selection = m.getSelection();
	}

}
