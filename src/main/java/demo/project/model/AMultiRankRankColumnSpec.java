/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.caleydo.vis.lineup.model.AMultiRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlSeeAlso({ MaxRankColumnSpec.class, NestedRankColumnSpec.class, ScriptedRankColumnSpec.class, StackedRankColumnSpec.class })
@XmlAccessorType(XmlAccessType.FIELD)
public class AMultiRankRankColumnSpec extends ACompositeColumnSpec {
	private float filterMin = 0;
	private float filterMax = 1;
	private boolean isGlobalFilter = false;
	private boolean isRankIndependentFilter = false;
	private String title = null;
	private String description = "";

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);

		AMultiRankColumnModel m = (AMultiRankColumnModel) model;
		title = m.getLabel();
		description = m.getDescription();
		isGlobalFilter = m.isGlobalFilter();
		isRankIndependentFilter = m.isRankIndependentFilter();
		filterMin = m.getFilterMin();
		filterMax = m.getFilterMax();
	}

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		AMultiRankColumnModel m = (AMultiRankColumnModel) model;
		m.setFilter(filterMin, filterMax);
		m.setGlobalFilter(isGlobalFilter);
		m.setIsRankIndependentFilter(isRankIndependentFilter);
		m.setTitle(title);
		m.setDescription(description);
	}
}
