/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.caleydo.vis.lineup.model.ACompositeRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlSeeAlso({ AMultiRankRankColumnSpec.class, GroupRankColumnSpec.class })
@XmlAccessorType(XmlAccessType.FIELD)
public class ACompositeColumnSpec extends ARankColumnSpec implements Iterable<ARankColumnSpec> {
	private List<ARankColumnSpec> children = new ArrayList<>();

	@Override
	public void save(ARankColumnModel model) {
		super.save(model);

		ACompositeRankColumnModel m = (ACompositeRankColumnModel) model;
		for (int i = 0; i < m.size(); ++i)
			children.get(i).save(m.get(i));
	}

	public void addChild(ARankColumnSpec spec) {
		this.children.add(spec);
	}

	@Override
	public void load(ARankColumnModel model) {
		super.load(model);

		ACompositeRankColumnModel m = (ACompositeRankColumnModel) model;
		for (int i = 0; i < m.size(); ++i)
			children.get(i).load(m.get(i));
	}

	@Override
	public Iterator<ARankColumnSpec> iterator() {
		return children.iterator();
	}
}
