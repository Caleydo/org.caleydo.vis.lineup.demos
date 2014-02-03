/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import javax.xml.bind.annotation.XmlSeeAlso;

import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
@XmlSeeAlso({ StandardViewSpec.class, ImportedViewSpec.class })
public class AViewSpec {
	private RankTableSpec tableSpec;

	/**
	 *
	 */
	public AViewSpec() {
		// TODO Auto-generated constructor stub
	}

	public AViewSpec(RankTableSpec tableSpec) {
		this.tableSpec = tableSpec;
	}

	/**
	 * @return the tableSpec, see {@link #tableSpec}
	 */
	public RankTableSpec getTableSpec() {
		return tableSpec;
	}

	/**
	 * @param tableSpec
	 *            setter, see {@link tableSpec}
	 */
	public void setTableSpec(RankTableSpec tableSpec) {
		this.tableSpec = tableSpec;
	}
}
