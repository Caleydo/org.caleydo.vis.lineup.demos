/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import generic.ImportSpec;
import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class ImportedViewSpec extends AViewSpec {
	private ImportSpec spec;

	private RankTableSpec tableSpec;

	/**
	 *
	 */
	public ImportedViewSpec() {
		// TODO Auto-generated constructor stub
	}

	public ImportedViewSpec(ImportSpec spec, RankTableSpec tableSpec) {
		this.spec = spec;
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

	/**
	 * @return the spec, see {@link #spec}
	 */
	public ImportSpec getSpec() {
		return spec;
	}

	/**
	 * @param spec
	 *            setter, see {@link spec}
	 */
	public void setSpec(ImportSpec spec) {
		this.spec = spec;
	}
}
