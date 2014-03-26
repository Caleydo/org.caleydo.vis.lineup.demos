/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.caleydo.vis.lineup.model.ABasicFilterableRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlSeeAlso({ CategoricalRankColumnSpec.class, DateRankColumnSpec.class, DoubleRankColumnSpec.class, IntegerRankColumnSpec.class,
		StringRankColumnSpec.class })
@XmlAccessorType(XmlAccessType.FIELD)
public class AFilterableRankColumnSpec extends ARankColumnSpec {

	protected boolean isGlobalFilter = false;
	protected boolean isRankIndependentFilter = false;

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);
		ABasicFilterableRankColumnModel m = (ABasicFilterableRankColumnModel) model;
		this.isGlobalFilter = m.isGlobalFilter();
		this.isRankIndependentFilter = m.isRankIndependentFilter();
	}

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);
		ABasicFilterableRankColumnModel m = (ABasicFilterableRankColumnModel) model;
		m.setIsRankIndependentFilter(isRankIndependentFilter);
		m.setGlobalFilter(isGlobalFilter);
	}
}
