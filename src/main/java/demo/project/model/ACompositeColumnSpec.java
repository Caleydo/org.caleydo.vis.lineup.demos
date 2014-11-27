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

import org.caleydo.core.util.color.Color;
import org.caleydo.vis.lineup.model.ACompositeRankColumnModel;
import org.caleydo.vis.lineup.model.ARankColumnModel;
import org.caleydo.vis.lineup.model.GroupRankColumnModel;
import org.caleydo.vis.lineup.model.MaxRankColumnModel;
import org.caleydo.vis.lineup.model.NestedRankColumnModel;
import org.caleydo.vis.lineup.model.ScriptedRankColumnModel;
import org.caleydo.vis.lineup.model.StackedRankColumnModel;

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

	/**
	 * @param col
	 * @param headers
	 * @return
	 */
	public static ARankColumnModel create(ACompositeColumnSpec col) {
		ACompositeRankColumnModel c = null;
		if (col instanceof GroupRankColumnSpec)
			c = new GroupRankColumnModel("Group", Color.GRAY, new Color(0.95f, .95f, .95f));
		else if (col instanceof StackedRankColumnSpec)
			c = new StackedRankColumnModel();
		else if (col instanceof MaxRankColumnSpec)
			c = new MaxRankColumnModel();
		else if (col instanceof NestedRankColumnSpec)
			c = new NestedRankColumnModel();
		else if (col instanceof ScriptedRankColumnSpec)
			c = new ScriptedRankColumnModel();
		if (c == null)
			return null;
		return c;
	}
}
