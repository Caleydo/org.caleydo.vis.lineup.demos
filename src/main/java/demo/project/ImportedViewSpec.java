/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import generic.ImportSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class ImportedViewSpec extends AViewSpec {
	private ImportSpec spec;

	/**
	 *
	 */
	public ImportedViewSpec() {
		// TODO Auto-generated constructor stub
	}

	public ImportedViewSpec(ImportSpec spec) {
		this.spec = spec;
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
