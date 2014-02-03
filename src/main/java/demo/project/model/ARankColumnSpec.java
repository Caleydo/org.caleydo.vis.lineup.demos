/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.caleydo.vis.lineup.model.ARankColumnModel;

/**
 * @author Samuel Gratzl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ ACompositeColumnSpec.class, AFilterableRankColumnSpec.class, RankRankColumnSpec.class, OrderRankColumnSpec.class })
public class ARankColumnSpec {
	@XmlAttribute
	private String data;

	@XmlAttribute
	private boolean collapsed;

	@XmlAttribute
	private float width;

	/**
	 * @return the data, see {@link #data}
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            setter, see {@link data}
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @param model
	 */
	public void load(ARankColumnModel model) {
		model.setCollapsed(collapsed);
		model.setWidth(width);
	}

	public void save(ARankColumnModel model) {
		this.collapsed = model.isCollapsed();
		this.width = model.getWidth();
	}
}
