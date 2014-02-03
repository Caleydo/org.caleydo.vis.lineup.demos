/*******************************************************************************
 * Caleydo - Visualization for Molecular Biology - http://caleydo.org
 * Copyright (c) The Caleydo Team. All rights reserved.
 * Licensed under the new BSD license, available at http://caleydo.org/license
 *******************************************************************************/
package demo.project;

import demo.project.model.RankTableSpec;

/**
 * @author Samuel Gratzl
 *
 */
public class StandardViewSpec extends AViewSpec {
	private String viewId;

	/**
	 *
	 */
	public StandardViewSpec() {

	}

	public StandardViewSpec(String viewId, RankTableSpec spec) {
		super(spec);
		this.viewId = viewId;
	}

	/**
	 * @param viewId
	 *            setter, see {@link viewId}
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	/**
	 * @return the viewId, see {@link #viewId}
	 */
	public String getViewId() {
		return viewId;
	}
}
